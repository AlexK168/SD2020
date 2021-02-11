package com.example.game.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinGameViewModel extends AndroidViewModel {

    private DatabaseReference mDatabase;
    private MutableLiveData<DatabaseReference> assumedRoom;
    public MutableLiveData<DatabaseReference> getAssumedRoom() {return assumedRoom; }
    private MutableLiveData<Boolean> boardCreated;
    public MutableLiveData<Boolean> getBoardCreated() {return boardCreated;}
    private String roomId;
    public String getRoomId() {return roomId;}

    public void setBoardCreated(boolean value) {
        boardCreated.setValue(value);
    }

    public void setRoomId(String id) {
        roomId = id;
    }

    public void setAssumedRoom(DatabaseReference room) {
        assumedRoom.setValue(room);
    }

    public JoinGameViewModel(@NonNull Application application) {
        super(application);
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        assumedRoom = new MutableLiveData<>();
        boardCreated = new MutableLiveData<>(false);
        roomId = null;
    }

}
