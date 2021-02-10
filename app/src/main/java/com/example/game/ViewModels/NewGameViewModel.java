package com.example.game.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class NewGameViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> opponentConnected;
    public boolean opponentBoardExists;
    public boolean selfBoardExists;
    public String opponentId;

    public boolean ready() {
        return opponentBoardExists && selfBoardExists && opponentConnected.getValue();
    }
    public NewGameViewModel(@NonNull Application application) {
        super(application);

        opponentId = null;
        opponentBoardExists = false;
        opponentConnected = new MutableLiveData<>(false);
        selfBoardExists = false;
    }
}
