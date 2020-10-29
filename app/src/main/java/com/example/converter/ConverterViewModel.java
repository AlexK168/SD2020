package com.example.converter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ConverterViewModel extends ViewModel {
    public MutableLiveData<String> input = new MutableLiveData<>();
    public MutableLiveData<String> output = new MutableLiveData<>();
    private static final Integer LIMIT = 14;

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

    void convert() {
        String input_s = input.getValue();
        if (input_s.isEmpty() || input_s.equals(".")) {
            output.setValue("");
        }
        else {
            double tmp = Double.parseDouble(input_s);
            output.setValue(Double.toString(someFunc(tmp)));
        }
    }

}
