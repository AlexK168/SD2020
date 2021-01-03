package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Phase;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item); // create phase

    @Delete
    void delete(Item item); // delete phase

    @Update
    void update(Item item); // update phase

    @Query("DELETE FROM Item") // clear all phases
    void deleteAll();

    @Query("SELECT * from Item WHERE id = :id") // get item with specified id
    LiveData<Item> getItem(int id);
}
