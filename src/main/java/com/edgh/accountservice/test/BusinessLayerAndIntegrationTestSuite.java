package com.edgh.accountservice.test;

import com.edgh.accountservice.constants.Constants;
import com.edgh.accountservice.enums.CurrencyEnum;
import com.edgh.accountservice.model.DTO.RateDTO;
import com.edgh.accountservice.model.entity.Account;
import com.edgh.accountservice.model.repository.AccountRepository;
import com.edgh.accountservice.service.AccountService;
import com.edgh.accountservice.service.ExchangeRatesService;
import com.edgh.accountservice.service.utils.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessLayerAndIntegrationTestSuite {

    private final String URL = "http://localhost:8080/";
    private final String GOOD_IBAN = "RO16RZBR0000060000232323";
    private final String BAD_IBAN = "RO16RZBR0000060000232322";

    @Resource
    private AccountService accountService;
    @Resource
    private CacheService cacheService;
    @Resource
    private AccountRepository accountRepository;
    @Mock
    private ExchangeRatesService exchangeRatesServiceMock;


    @Value("${exchangerates.api.key}")
    private String exchangeratesApiKey;

    @Value("${exchangerates.api.url}")
    private String exchangeRatesApiUrl;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testErrorForAccountNotFound() {

        TestRestTemplate restTemplate = new TestRestTemplate();

        assertThat(restTemplate.getForObject(this.URL + "account/get?iban=" + this.BAD_IBAN,
                String.class).contains(HttpStatus.NOT_FOUND.toString()));
    }

    @Test
    public void testSuccessForAccountFound() {

        TestRestTemplate restTemplate = new TestRestTemplate();

        assertThat(restTemplate.getForObject(this.URL + "account/get?iban=" + this.GOOD_IBAN,
                String.class).contains(HttpStatus.OK.toString()));
    }


    @Test
    public void testRateDTOMapping() throws JsonProcessingException {

        TestRestTemplate restTemplate = new TestRestTemplate();

        RateDTO rateDTO = restTemplate.getForObject(exchangeRatesApiUrl + "?access_key=" + exchangeratesApiKey, RateDTO.class);
        String rateString = restTemplate.getForObject(exchangeRatesApiUrl + "?access_key=" + exchangeratesApiKey, String.class);


        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(Objects.requireNonNull(rateString), Map.class);

        LinkedHashMap rates = (LinkedHashMap) map.get(Constants.RATES);
        boolean success = (boolean) map.get(Constants.SUCCESS);
        Integer timestamp = (Integer) map.get(Constants.TIMESTAMP);
        String base = (String) map.get(Constants.BASE);


        assertThat(base.equals(rateDTO.getBase())
                && success == rateDTO.isSuccess()
                && timestamp.equals(rateDTO.getTimestamp())
                && rates.equals(rateDTO.getRates()));

    }


    @Test
    public void testGetAccountWithCachedRates() throws Exception {

        //BE SURE CACHE HAS A VALUe
        Cache ratesCache = this.cacheService.cacheManager.getCache(Constants.RATES);
        ratesCache.put(Constants.RATES_FUNCTION_NAME, this.populateCacheWithDefault());

        Account expectedAccount = this.accountRepository.findByIBAN(GOOD_IBAN);

        TestRestTemplate restTemplate = new TestRestTemplate();
        RateDTO rateDTO = restTemplate.getForObject(exchangeRatesApiUrl + "?access_key=" + exchangeratesApiKey, RateDTO.class);


        expectedAccount.setBalance(new BigDecimal(expectedAccount.getBalance().doubleValue() / rateDTO.getRates().get(CurrencyEnum.RON.name())));

        Account actualAccount = accountService.getAccountByIBAN(this.GOOD_IBAN);


        assertThat(expectedAccount.getBalance().equals(actualAccount.getBalance()));

    }

    @Test
    public void testGetAccountWithoutCachedRates() throws Exception {

        //BE SURE CACHE IS EMPTY
        this.cacheService.evitRatesCache();

        Account expectedAccount = this.accountRepository.findByIBAN(GOOD_IBAN);

        TestRestTemplate restTemplate = new TestRestTemplate();
        RateDTO rateDTO = restTemplate.getForObject(exchangeRatesApiUrl + "?access_key=" + exchangeratesApiKey, RateDTO.class);


        expectedAccount.setBalance(new BigDecimal(expectedAccount.getBalance().doubleValue() / rateDTO.getRates().get(CurrencyEnum.RON.name())));

        Account actualAccount = accountService.getAccountByIBAN(this.GOOD_IBAN);


        assertThat(expectedAccount.getBalance().equals(actualAccount.getBalance()));

    }

    private Mono<RateDTO> populateCacheWithDefault() {
        RateDTO rateDTO = new RateDTO();
        rateDTO.setBase(Constants.DEFAULT_BASE);
        rateDTO.setRates(Constants.DEFAULT_RATES);
        rateDTO.setTimestamp(Constants.DEFAULT_TIMESTAMP);
        rateDTO.setSuccess(Constants.DEFAULT_SUCCESS);
        rateDTO.setCacheable(false);

        return Mono.just(rateDTO);
    }

}