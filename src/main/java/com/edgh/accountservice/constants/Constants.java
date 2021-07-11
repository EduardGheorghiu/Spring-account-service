package com.edgh.accountservice.constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Constants {

    public final static String RATES = "rates";
    public final static String SUCCESS = "success";
    public final static String TIMESTAMP = "timestamp";
    public final static String BASE = "base";
    public final static String CIRCUIT_BREAKER = "circuit_breaker";
    public final static String RATES_FUNCTION_NAME = "getRatesFromAPI";
    public final static Integer CONNECTION_TIMEOUT = 30000;
    public final static Integer READ_TIMEOUT = 30000;


    public final static boolean DEFAULT_SUCCESS = true;
    public final static Integer DEFAULT_TIMESTAMP = 1625919844;
    public final static String DEFAULT_BASE = "EUR";
    public final static Map<String, Double> DEFAULT_RATES = new LinkedHashMap<String, Double>() {{
        put("RON", 4.928009);
        put("USD", 1.1877);
    }};
}
