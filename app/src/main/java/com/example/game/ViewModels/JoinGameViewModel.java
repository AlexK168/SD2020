package com.example.game.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class JoinGameViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> isConnected;

    public JoinGameViewModel(@NonNull Application application) {
        super(application);
        isConnected = new MutableLiveData<>(false);
    }
}
