package com.edgh.accountservice.service.impl;

import com.edgh.accountservice.constants.Constants;
import com.edgh.accountservice.enums.CurrencyEnum;
import com.edgh.accountservice.exception.BusinessException;
import com.edgh.accountservice.model.DTO.RateDTO;
import com.edgh.accountservice.model.entity.Account;
import com.edgh.accountservice.model.repository.AccountRepository;
import com.edgh.accountservice.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class AccountServiceImpl implements AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountRepository accountRepository;

    private ExchangeRatesServiceImpl ratesService;

    @Value("${exchangerates.api.key}")
    private String exchangeratesApiKey;

    @Value("${exchangerates.api.url}")
    private String exchangeRatesApiUrl;

    public AccountServiceImpl(ExchangeRatesServiceImpl ratesService, AccountRepository accountRepository) {
        this.ratesService = ratesService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountByIBAN(String iban) throws BusinessException, JsonProcessingException {

        if (iban != null) {
            Account account = accountRepository.findByIBAN(iban);
            if(account != null){
                Double conversionRate = this.getConversionRate(CurrencyEnum.valueOf(account.getCurrency()));
                if(conversionRate != null){
                    if(account.getCurrency().equals(CurrencyEnum.valueOf(account.getCurrency()).name())) {
                        account.setBalance(new BigDecimal(account.getBalance().doubleValue() / conversionRate, MathContext.DECIMAL64));
                        account.setCurrency(CurrencyEnum.EUR.name());
                    }
                }
                logger.debug("getAccountByIBAN -> Conversion rate not found");
            } else {
                logger.debug("getAccountByIBAN -> Account is null");
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No account found for the following IBAN: " + iban
                );
            }
            return account;
        }
        logger.debug("getAccountByIBAN -> IBAN can't be null");
        throw new BusinessException("getAccountByIBAN -> IBAN can't be null");
    }

    private Double getConversionRate(CurrencyEnum currencyEnum) {

        Mono<RateDTO> exchangeRates = this.ratesService.getRatesFromAPI();

        if(exchangeRates != null) {

            LinkedHashMap rates = (LinkedHashMap) Objects.requireNonNull(exchangeRates.block()).getRates();
            if(rates != null && rates.size() > 0){
                return (Double) rates.get(currencyEnum.name());
            }
            logger.debug("getRates -> No rates");

        }
        logger.debug("getAccountByIBAN -> No response from rates API");


        return null;
    }

}
