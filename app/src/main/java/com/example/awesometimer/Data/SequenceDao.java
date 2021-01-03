package com.example.awesometimer.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.awesometimer.Models.Phase;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Models.SequenceWithItems;

import java.util.List;

@Dao
public interface SequenceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Sequence seq); // create sequence

    @Delete
    void delete(Sequence seq); // delete sequence

    @Update
    void update(Sequence seq); // update sequence

    @Query("DELETE FROM Sequence") // clear all sequences
    void deleteAll();

    @Query("SELECT * from Sequence ORDER BY title ASC")  // get all sequences
    LiveData<List<Sequence>> getAllSequences();

    @Query("SELECT * from Sequence WHERE id = :id") // get sequence with specified id
    LiveData<Sequence> getSequence(int id);

    @Transaction                                    // get Sequences with items
    @Query("SELECT * FROM Sequence")
    public LiveData<List<SequenceWithItems>> getSequencesWithItems();

    @Transaction                                    // get Sequence with specified id with items
    @Query("SELECT * FROM Sequence WHERE id = :id")
    public LiveData<SequenceWithItems> getSequenceWithItems(int id);

}
