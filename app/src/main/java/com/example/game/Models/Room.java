package com.example.game.Models;

public class Room {
    public Room(){}
    public Room(String hostUId) {
        this.hostUId = hostUId;
        guestUId = null;
        guestReady = false;
        isReady = false;
        isHostTurn = true;
    }

    public String hostUId;
    public String guestUId;

    public boolean guestReady;
    public boolean isReady;

    public boolean isHostTurn;
}
