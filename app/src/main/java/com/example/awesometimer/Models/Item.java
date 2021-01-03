package com.example.awesometimer.Models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    public Item(@NonNull int duration, @NonNull int id_phase, @NonNull int id_sequence) {
        this.duration = duration;
        this.id_phase = id_phase;
        this.id_sequence = id_sequence;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public int duration;

    @NonNull
    public int id_phase;

    @NonNull
    public int id_sequence;
}
