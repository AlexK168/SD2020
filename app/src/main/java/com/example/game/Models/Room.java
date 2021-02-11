package com.example.game.Models;

public class Room {
    public Room(String hostUId) {
        this.hostUId = hostUId;
        guestUId = null;
        hostTurn = true;
        guestReady = false;
        isReady = false;
    }
    public Room(){}
    public String hostUId;
    public String guestUId;

    public boolean guestReady;
    public boolean hostTurn;
    public boolean isReady;
}
