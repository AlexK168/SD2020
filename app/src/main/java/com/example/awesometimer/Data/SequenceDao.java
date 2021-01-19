package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.awesometimer.Models.Sequence;

import java.util.List;

@Dao
public interface SequenceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Sequence seq); // create sequence

    @Delete
    void delete(Sequence seq); // delete sequence

    @Query("DELETE from Item WHERE id_sequence = :seq_id")
    void delete(int seq_id);

    @Update
    void update(Sequence seq); // update sequence

    @Query("SELECT * from Sequence ORDER BY title ASC")  // get all sequences
    LiveData<List<Sequence>> getAllSequences();

    @Query("SELECT * from Sequence WHERE id = :id LIMIT 1") // get sequence with specified id
    LiveData<Sequence> getSequence(int id);


}
