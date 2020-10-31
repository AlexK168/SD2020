package com.example.converter.categories;

import java.util.Map;

public interface Category {
    String[] units();
    Map<String, Double> rates();
    Double get(String key);
}
