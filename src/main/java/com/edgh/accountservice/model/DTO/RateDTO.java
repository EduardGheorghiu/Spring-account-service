package com.edgh.accountservice.model.DTO;

import java.util.Map;

public class RateDTO {

    private boolean success;
    private Integer timestamp;
    private String base;
    private Map<String, Double> rates;
    private boolean cacheable;

    public RateDTO(boolean succes, Integer timestamp, String base, Map<String, Double> rates) {
        this.success = succes;
        this.timestamp = timestamp;
        this.base = base;
        this.rates = rates;
        this.cacheable = true;
    }

    public RateDTO() {
        this.cacheable = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean succes) {
        this.success = succes;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "succes=" + success +
                ", timestamp=" + timestamp +
                ", base='" + base + '\'' +
                ", rates=" + rates +
                '}';
    }
}
