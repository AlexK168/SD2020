package com.example.converter.categories;

import java.util.HashMap;
import java.util.Map;

public class LengthCategory implements Category {

    private final String[] units = {
            "in", "ft", "yd", "mi",
            "mm", "cm", "m", "km"
    };

    private final Map<String, Double> rates;

    public LengthCategory() {
        rates = new HashMap<>();
        rates.put("in", 63360.0);
        rates.put("ft", 5280.0);
        rates.put("yd", 1760.0);
        rates.put("mi", 1.0);
        rates.put("mm", 1609344.0);
        rates.put("cm", 160934.4);
        rates.put("m", 1609.344);
        rates.put("km", 1.609344);
    }

    @Override
    public String[] units() {
        return units;
    }

    @Override
    public Map<String, Double> rates() {
        return rates;
    }

    @Override
    public Double get(String key) {
        return rates.get(key);
    }
}
