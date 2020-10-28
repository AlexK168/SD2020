package com.example.converter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConverterViewModel extends ViewModel {
    public MutableLiveData<Double> input = new MutableLiveData<>();
    public MutableLiveData<Double> output = new MutableLiveData<>();

    ConverterViewModel() {
        input.postValue((double) 0);
        output.postValue((double) 0);
    }

}
