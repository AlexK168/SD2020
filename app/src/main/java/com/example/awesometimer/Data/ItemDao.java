package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.awesometimer.Models.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item); // create item

    @Delete
    void delete(Item item); // delete item

    @Update
    void update(Item item); // update item

    @Query("DELETE FROM Item") // clear all items
    void deleteAll();

    @Query("SELECT * from Item WHERE id = :id") // get item with specified id
    LiveData<Item> getItem(int id);

    @Query("SELECT * from Item") // get all items
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * from Item where id_sequence = :id") // get all items of sequence
    LiveData<List<Item>> getItems(int id);
}
