package com.example.awesometimer.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SequenceWithItems {
    @Embedded
    public Sequence sequence;
    @Relation(
            parentColumn = "id",
            entityColumn = "id_sequence"
    )
    public List<Item> items;
}
