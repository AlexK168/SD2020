package com.example.game.Models;

public class Ship {
    public int x;
    public int y;
    public int length;
    public boolean orientation;
    public Ship (int x, int y, int length, boolean orientation) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.orientation = orientation;
    }

    public boolean intersect(Ship ship){
        int leftUpperX = x - 1;
        int leftUpperY = y - 1;
        int rightLowerX = x + 1 + (orientation ? length - 1 : 0);
        int rightLowerY = y + 1 + (orientation ? 0 : length - 1);

        // frame of the ship and surrounding area
        leftUpperX += leftUpperX < 0 ? 1 : 0;
        leftUpperY += leftUpperY < 0 ? 1 : 0;
        rightLowerX -= rightLowerX > 9 ? 1 : 0;
        rightLowerY -= rightLowerY > 9 ? 1 : 0;

        int x = ship.x;
        int y = ship.y;
        int length = ship.length;
        boolean orientation = ship.orientation;
        for (int i = 0; i < length; i++) {
            if (x <= rightLowerX && x >= leftUpperX && y >= leftUpperY && y <= rightLowerY) {
                return true;
            }
            if (orientation) {
                x++;
            } else {
                y++;
            }
        }
        return false;
    }

    public Ship(){}
}
