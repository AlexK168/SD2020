package com.example.converter;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import static java.util.Map.entry;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ConverterViewModel extends ViewModel {
    public MutableLiveData<String> input = new MutableLiveData<>();
    public MutableLiveData<String> output = new MutableLiveData<>();

    private static final Integer LIMIT = 14;

    private String inputUnit;
    private String outputUnit;

    public static final String[] length_units = {
            "in", "ft", "yd", "mi",
            "mm", "cm", "m", "km"
    };

    public static final String[] mass_units = {
            "mg", "g", "kg", "t",
            "oz", "lb", "st", "t(US)"
    };

    public static final Map<String, Double> weights = Map.ofEntries(
            entry("in", 63360.0),
            entry("ft", 5280.0),
            entry("yd", 1760.0),
            entry("mi", 1.0),
            entry("mm", 1609344.0),
            entry("cm", 160934.4),
            entry("m", 1609.344),
            entry("km", 1.609344),

            entry("t(US)", 1.0),
            entry("st", 142.857142857),
            entry("lb", 2000.0),
            entry("oz", 32000.0),
            entry("mg", 907183761.411),
            entry("g", 907183.761411),
            entry("kg", 907.183761411),
            entry("t", 0.907183761411)
    );


    // kinda conversion
    private double someFunc(double inputValue) {
        return inputValue + 1;
    }

    public ConverterViewModel() {
        input.setValue("");

        output.setValue("");
    }

    void clear() {
        input.setValue("");
        output.setValue("");
        convert();
    }

    void erase() {
        String tmp = input.getValue();
        input.setValue(Utils.removeLastCharacter(tmp));
        convert();
    }

    void addDigit(String d) {
        String tmp = input.getValue();
        if (tmp.length() < LIMIT) {
            input.setValue(tmp + d);
            convert();
        }
    }

    void addDot() {
        String tmp = input.getValue();
        if (tmp.length() < LIMIT) {
            if (!tmp.contains(".")) {
                input.setValue(tmp + '.');
            }
            convert();
        }
    }

    void setInputUnit(String unit) {
        inputUnit = unit;
        convert();
    }

    void setOutputUnit(String unit) {
        outputUnit = unit;
        convert();
    }

    void convert() {
        String inputString = input.getValue();
        if (inputString.isEmpty() || inputString.equals(".")) {
            output.setValue("");
        }
        else {
            double tmp = Double.parseDouble(inputString);
            double in_koeff = weights.get(inputUnit);
            double out_koeff = weights.get(outputUnit);
            output.setValue(Double.toString(out_koeff / in_koeff * tmp));
        }
    }

}
