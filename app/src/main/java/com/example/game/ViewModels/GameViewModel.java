package com.example.game.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.game.Models.Board;

public class GameViewModel extends AndroidViewModel {
    private MutableLiveData<Board> enemyBoard;
    private MutableLiveData<Board> myBoard;
    public GameViewModel(@NonNull Application application) {
        super(application);
        enemyBoard = new MutableLiveData<>();
        myBoard = new MutableLiveData<>();
    }

    public void setBoards(Board enemyBoard, Board myBoard) {
        this.enemyBoard.setValue(enemyBoard);
        this.myBoard.setValue(myBoard);
    }

    public void shoot(int x, int y) {

    }
}
