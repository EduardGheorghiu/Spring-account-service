package com.edgh.accountservice.service.impl;

import com.edgh.accountservice.constants.Constants;
import com.edgh.accountservice.model.DTO.RateDTO;
import com.edgh.accountservice.service.ExchangeRatesService;
import com.edgh.accountservice.service.utils.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static Logger logger = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);


    @Value("${exchangerates.api.key}")
    private String exchangeratesApiKey;

    @Value("${exchangerates.api.url}")
    private String exchangeRatesApiUrl;

    @Resource
    private CacheService cacheService;

    private final CircuitBreakerFactory circuitBreakerFactory;


    public ExchangeRatesServiceImpl(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Bean
    public RestTemplate restTemplate() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        factory.setReadTimeout(Constants.READ_TIMEOUT);
        return new RestTemplate(factory);
    }

    @Override
    @Cacheable(value = Constants.RATES, key = "#root.methodName", unless = "#result.block().cacheable==false")
    public Mono<RateDTO> getRatesFromAPI() {

        final String url = exchangeRatesApiUrl + "?access_key=" + exchangeratesApiKey;

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(Constants.CIRCUIT_BREAKER);

        RestTemplate restTemplate = new RestTemplate();

        return circuitBreaker.run(() -> Mono.justOrEmpty(restTemplate.getForObject(url, RateDTO.class)),
                throwable -> getDefaultRates());

    }

    private Mono<RateDTO> getDefaultRates() {
        logger.debug("CIRCUIT_BREAKER -> Default rates used");

        //evict default values from cache

        RateDTO rateDTO = new RateDTO();
        rateDTO.setBase(Constants.DEFAULT_BASE);
        rateDTO.setRates(Constants.DEFAULT_RATES);
        rateDTO.setTimestamp(Constants.DEFAULT_TIMESTAMP);
        rateDTO.setSuccess(Constants.DEFAULT_SUCCESS);
        rateDTO.setCacheable(false);

        return Mono.just(rateDTO);
    }

}
