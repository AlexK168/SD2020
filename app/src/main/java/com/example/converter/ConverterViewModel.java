package com.example.converter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ConverterViewModel extends ViewModel {
    public MutableLiveData<Double> input = new MutableLiveData<>();
    public MutableLiveData<Double> output = new MutableLiveData<>();

    public ConverterViewModel() {
        input.setValue((double) 0);
        output.setValue((double) 0);
    }

    void clear() {
        input.setValue((double) 0);
        output.setValue((double) 0);
    }

    void erase() {
        String tmp = input.getValue().toString();
        input.setValue(Double.parseDouble(Utils.removeLastCharacter(tmp)));
    }

    void addDigit(String d) {
        String tmp = input.getValue().toString();
        input.setValue(Double.parseDouble(tmp + d));
    }

}
