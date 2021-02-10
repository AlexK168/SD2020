package com.example.game.Models;

import java.util.ArrayList;
import java.util.List;

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

}
