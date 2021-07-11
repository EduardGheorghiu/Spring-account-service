package com.edgh.accountservice.service.utils;

import com.edgh.accountservice.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheService.class);

    public final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    //clear cache every 1 hour
    @Scheduled(fixedRate =  60 * 1000)
    public void evitRatesCache() {
        logger.debug("evitRatesCache -> Rates cache cleared");
        Objects.requireNonNull(cacheManager.getCache(Constants.RATES)).clear();
    }

}