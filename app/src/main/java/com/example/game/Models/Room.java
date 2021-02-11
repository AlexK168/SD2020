package com.example.game.Models;

public class Room {
    public Room(String hostUId) {
        this.hostUId = hostUId;
        this.guestUId = null;
    }
    public Room(){}
    public String hostUId;
    public String guestUId;
    public boolean isReady;
}
