package com.example.converter.categories;
import java.util.HashMap;
import java.util.Map;

public class SpeedCategory implements Category{
    private final String[] units = {
            "m/s", "km/h", "mi/h", "ft/s"
    };

    private final Map<String, Double> rates;

    public SpeedCategory() {
        rates = new HashMap<>();
        rates.put("m/s", 0.44704);
        rates.put("km/h",1.609344);
        rates.put("mi/h", 1.0);
        rates.put("ft/s", 1.466666666667);
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
