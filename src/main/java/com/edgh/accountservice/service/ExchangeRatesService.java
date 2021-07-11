package com.edgh.accountservice.service;

import com.edgh.accountservice.model.DTO.RateDTO;
import reactor.core.publisher.Mono;

public interface ExchangeRatesService {

    Mono<RateDTO> getRatesFromAPI();
}
