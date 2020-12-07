package com.example.converter.categories;

import java.util.HashMap;
import java.util.Map;

public class MassCategory implements Category{

    private final String[] units = {
            "mg", "g", "kg", "t",
            "oz", "lb", "st", "t(US)"
    };

    private final Map<String, Double> rates;

    public MassCategory() {
        rates = new HashMap<>();
        rates.put("t(US)", 1.0);
        rates.put("st", 142.857142857);
        rates.put("lb", 2000.0);
        rates.put("oz", 32000.0);
        rates.put("mg", 907183761.411);
        rates.put("g", 907183.761411);
        rates.put("kg", 907.183761411);
        rates.put("t", 0.907183761411);
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
