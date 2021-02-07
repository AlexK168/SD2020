package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.awesometimer.Models.Stage;

import java.util.List;

@Dao
public interface StageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Stage stage); // create stage

    @Delete
    void delete(Stage stage); // delete stage

    @Update
    void update(Stage stage); // update stage

    @Query("SELECT * from Stage ORDER BY name ASC")  // get all stages
    LiveData<List<Stage>> getAllStages();
}
