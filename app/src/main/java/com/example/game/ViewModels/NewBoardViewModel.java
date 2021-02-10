package com.example.game.ViewModels;

import android.app.Application;
import android.app.PendingIntent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.game.Models.Board;
import com.example.game.Models.CellStates;
import com.example.game.Models.Ship;

import java.util.ArrayList;

public class NewBoardViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<Ship>> mShips;
    private final int[] counter;
    private Ship currentShip;

    public MutableLiveData<ArrayList<Ship>> getShips() {return mShips;}

    public NewBoardViewModel(@NonNull Application application) {
        super(application);
        counter = new int[4];
        for (int i : counter) {
            i = 0;
        }
        mShips = new MutableLiveData<>(new ArrayList<>());
    }

    public void addShip(Ship s) {
        ArrayList<Ship> collection = mShips.getValue();
        if (canPlaceShip(s, collection)) {
            collection.add(s);
            mShips.setValue(collection);
            counter[s.length - 1]++;
        }
    }

    public void setCurrentShip(Ship s) {
        currentShip = s;

    }

    public void rotate() {
        if (currentShip == null) {
            Toast.makeText(getApplication(), "Pick ship to move first", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Ship> collection = mShips.getValue();
        int index = collection.indexOf(currentShip);
        ArrayList<Ship> copiedCollection = new ArrayList<>(collection);
        copiedCollection.remove(index);

        currentShip.orientation = !currentShip.orientation;
        counter[currentShip.length - 1]--;
        if (canPlaceShip(currentShip, copiedCollection)) {
            collection.set(index, currentShip);
            mShips.setValue(collection);
        } else {
            currentShip.orientation = !currentShip.orientation;
            Toast.makeText(getApplication(), "Can't move there, sir", Toast.LENGTH_SHORT).show();
        }
    }

    public void move(int dx, int dy) {
        if (currentShip == null) {
            Toast.makeText(getApplication(), "Pick ship to move first", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Ship> collection = mShips.getValue();
        int index = collection.indexOf(currentShip);
        ArrayList<Ship> copiedCollection = new ArrayList<>(collection);
        copiedCollection.remove(index);

        currentShip.x += dx;
        currentShip.y += dy;

        counter[currentShip.length - 1]--;
        if (canPlaceShip(currentShip, copiedCollection)) {
            collection.set(index, currentShip);
            mShips.setValue(collection);
        } else {
            currentShip.x -= dx;
            currentShip.y -= dy;
            Toast.makeText(getApplication(), "Can't move there, sir", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteShip(Ship s) {
        ArrayList<Ship> collection = mShips.getValue();
        if (currentShip == s) {
            currentShip = null;
        }
        collection.remove(s);
        mShips.setValue(collection);
        counter[s.length - 1]--;
    }

    private Boolean intersect(Ship ship, ArrayList<Ship> collection) {
        for (Ship s : collection) {
            if (s.intersect(ship)) {
                return true;
            }
        }
        return false;
    }

    private Boolean canPlaceShip(Ship ship, ArrayList<Ship> collection) {
        int length = ship.length;
        int x = ship.x;
        int y = ship.y;
        boolean isHorizontal = ship.orientation;

        if (length + counter[length - 1] > 4) {
            Toast.makeText(getApplication(), "Can't place more ships of that type", Toast.LENGTH_SHORT).show();
            return false;
        }

        int x_co = x, y_co = y;
        for (int i = 0; i < length; i++) {
            if (x_co > 9 || x_co < 0 || y_co > 9 || y_co < 0) {
                Toast.makeText(getApplication(), "Can't place the ship out of playing field", Toast.LENGTH_SHORT).show();
                return false;
            }
            x_co += isHorizontal ? 1 : 0;
            y_co += isHorizontal ? 0 : 1;
        }

        if (intersect(ship, collection)) {
            Toast.makeText(getApplication(), "Can't place the ship here", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public Board saveBoard() {
        if (mShips.getValue().size() != 10) {
            Toast.makeText(getApplication(), "There are more ships to place", Toast.LENGTH_SHORT).show();
            return null;
        }
        Board board = new Board();
        ArrayList<Ship> collection = mShips.getValue();
        for (Ship s : collection) {
            int tmpX = s.x;
            int tmpY = s.y;
            boolean orientation = s.orientation;
            int length = s.length;
            for (int i = 0; i < length; i++) {
                board.cells.get(tmpY).set(tmpX, CellStates.SHIP);
                if (orientation) {
                    tmpX++;
                }
                else {
                    tmpY++;
                }
            }
        }
        return board;
    }
}
