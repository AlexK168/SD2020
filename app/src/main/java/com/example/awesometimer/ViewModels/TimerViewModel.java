package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TimerViewModel extends AndroidViewModel {
    private MutableLiveData<String> currentPhase;
    private MutableLiveData<Integer> timeRemain;
    private MutableLiveData<Boolean> isPaused;

    public TimerViewModel(@NonNull Application application) {
        super(application);
        currentPhase = new MutableLiveData<>("");
        timeRemain = new MutableLiveData<>(0);
        isPaused = new MutableLiveData<>(true);
    }

    public void init(String phase, int time) {
        setPhase(phase);
        setTimeRemain(time);
    }

    public void setPhase(String phase) { currentPhase.setValue(phase); }

    public void setTimeRemain(Integer time) { timeRemain.setValue(time);}

    public void setIsPaused(Boolean value) {isPaused.setValue(value);}

    public MutableLiveData<Boolean> isPaused(){return isPaused;}

    public MutableLiveData<String> getPhase() {
        return currentPhase;
    }

    public MutableLiveData<Integer> getTimeRemain() {
        return timeRemain;
    }

    public void flip() { isPaused.setValue(!isPaused.getValue()); }
}
