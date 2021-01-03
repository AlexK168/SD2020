package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.awesometimer.Models.Phase;
import com.example.awesometimer.Models.Sequence;

import java.util.List;

@Dao
public interface PhaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Phase phase); // create phase

    @Delete
    void delete(Phase phase); // delete phase

    @Update
    void update(Phase phase); // update phase

    @Query("DELETE FROM Phase") // clear all phases
    void deleteAll();

    @Query("SELECT * from Phase ORDER BY name ASC") // get all phases
    LiveData<List<Phase>> getAllPhases();

    @Query("SELECT * from Phase WHERE id = :id") // get phase with specified id
    LiveData<Phase> getPhase(int id);
}
