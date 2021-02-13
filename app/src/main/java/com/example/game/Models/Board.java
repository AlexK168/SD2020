package com.example.game.Models;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board {
    public Board() {
        cells = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                row.add(CellStates.EMPTY);
            }
            cells.add(row);
        }
    }
    public ArrayList<ArrayList<Integer>> cells;

    @Exclude
    public void checkIfKilled(int x, int y) {
        HashSet<Point> visitedPoints = new HashSet<>();
        Boolean[] isDead = new Boolean[1];
        isDead[0] = true;
        // we assume ship is dead, thus every cell of it is hit
        visitCell(x, y, visitedPoints, isDead);
        if (isDead[0]) {
            HashSet<Point> visited = new HashSet<>();
            markCellsAround(x, y, visited);
        }
    }

    @Exclude
    private void markCellsAround(int x, int y, HashSet<Point> visitedPoints) {
        Point current = new Point(x, y);
        if (visitedPoints.contains(current)) { // if this point is visited - return
            return;
        }
        visitedPoints.add(current);
        int[] XPoints = {x-1, x, x+1, x+1, x+1, x, x-1, x-1};
        int[] YPoints = {y-1, y-1, y-1, y, y+1, y+1, y+1, y};
        for (int i = 0; i < 8; i++) {
            int X = XPoints[i];
            int Y = YPoints[i];
            if (isEmpty(X, Y)) {
                cells.get(Y).set(X, CellStates.MISS);
            }
            if (isHit(X, Y)) {
                markCellsAround(X, Y, visitedPoints);
            }
        }
    }


    @Exclude
    private void visitCell(int x, int y, HashSet<Point> visitedPoints, Boolean[] isDead) {
        Point current = new Point(x, y);
        if (visitedPoints.contains(current)) { // if this point is visited - return
            return;
        }
        visitedPoints.add(current);
        // points around current point
        int[] XPoints = {x, x+1, x, x-1};
        int[] YPoints = {y-1, y, y+1, y};
        for (int i = 0; i < 4; i++) {
            int X = XPoints[i];
            int Y = YPoints[i];
            // if any of points around is at SHIP state - this ship is definitely alive
            if (isShip(X, Y)) {
                isDead[0] = false;
                return;
            }
            // if point nearby is hit - go there and check
            if (isHit(X, Y)) {
                visitCell(X, Y, visitedPoints, isDead);
            }
        }
    }

    @Exclude
    private boolean isOutOfBoard(int x, int y) {
        return x < 0 || x > 9 || y < 0 || y > 9;
    }

    @Exclude
    private boolean isEmpty(int x, int y) {
        if (isOutOfBoard(x, y)) {
            return false;
        }
        return cells.get(y).get(x).equals(CellStates.EMPTY);
    }

    @Exclude
    private boolean isShip(int x, int y) {
        if (isOutOfBoard(x, y)) {
            return false;
        }
        return cells.get(y).get(x).equals(CellStates.SHIP);
    }

    @Exclude
    private boolean isHit(int x, int y) {
        if (isOutOfBoard(x, y)) {
            return false;
        }
        return cells.get(y).get(x).equals(CellStates.HIT);
    }

    @Exclude
    public boolean isFinished() {
        int counter = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (cells.get(i).get(j).equals(CellStates.HIT)) {
                    counter++;
                }
            }
        }
        return counter >= 20;
    }
}
