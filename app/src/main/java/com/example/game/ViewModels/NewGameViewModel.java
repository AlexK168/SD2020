package com.example.game.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class NewGameViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> opponentConnected;

    private MutableLiveData<Boolean> boardCreated;
    public MutableLiveData<Boolean> getBoardCreated() {return boardCreated;}
    public MutableLiveData<Boolean> getOpponentConnected() { return opponentConnected; }

    public void setOpponentConnected(Boolean value) {
        opponentConnected.setValue(value);
    }

    public void setBoardCreated(Boolean value) {
        boardCreated.setValue(value);
    }

    public NewGameViewModel(@NonNull Application application) {
        super(application);

        boardCreated = new MutableLiveData<>(false);
        opponentConnected = new MutableLiveData<>(false);
    }
}
