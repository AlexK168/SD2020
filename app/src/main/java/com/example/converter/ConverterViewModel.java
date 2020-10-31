package com.example.converter;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.converter.categories.Category;
import com.example.converter.categories.LengthCategory;
import com.example.converter.categories.MassCategory;
import com.example.converter.categories.SpeedCategory;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ConverterViewModel extends ViewModel {

    private final MutableLiveData<String> input = new MutableLiveData<>();
    private final MutableLiveData<String> output = new MutableLiveData<>();
    private final MutableLiveData<Category> category = new MutableLiveData<>();

    private static final Integer LIMIT = 14;
    public static final String LENGTH = "length";
    public static final String SPEED = "currency";
    public static final String MASS = "mass";

    private static String inputUnit;
    private static String outputUnit;

    public ConverterViewModel() {
        Log.d("ViewModel", "Constructor");
        input.setValue("");
        category.setValue(new MassCategory());
        output.setValue("");
    }

    public MutableLiveData<String> getInput() {
        return input;
    }

    public MutableLiveData<String> getOutput() {
        return output;
    }

    public MutableLiveData<Category> getCategory() {
        return category;
    }

    void clear() {
        Log.d("ViewModel", "clear method");
        input.setValue("");
        output.setValue("");
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
            double inputRate = category.getValue().get(inputUnit);
            double outputRate = category.getValue().get(outputUnit);
            output.setValue(Double.toString(outputRate / inputRate * tmp));
        }
    }

    String[] getUnitsList() {
        return category.getValue().units();
    }

    void setCategory(String option) {
        if (option.equals(MASS)) {
            category.setValue(new MassCategory());
        }else
        if(option.equals(LENGTH)) {
            category.setValue(new LengthCategory());
        }
        if(option.equals(SPEED)) {
            category.setValue(new SpeedCategory());
        }
    }

    void setDefaultUnits() {
        String[] tmp = category.getValue().units();
        inputUnit = tmp[0];
        outputUnit = tmp[0];
    }
}
