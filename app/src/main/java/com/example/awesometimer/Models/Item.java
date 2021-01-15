package com.example.awesometimer.Models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    public Item(@NonNull int duration, @NonNull int id_sequence, @NonNull String phase) {
        this.duration = duration;
        this.id_sequence = id_sequence;
        this.phase = phase;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public int duration;

    @NonNull
    public String phase;

    @NonNull
    public int id_sequence;
}
