package com.example.game.Models;

public class Game {
    public Game() {}
    public Game(String hostUId, String guestUId) {
        waitingForMove = true;
        this.hostUId = hostUId;
        this.guestUId = guestUId;
        isHostTurn = true;
        winner = null;
    }

    public String hostUId;
    public String guestUId;

    public int targetX;
    public int targetY;
    public boolean isHostTurn;
    public boolean waitingForMove;
    public String winner;
}
