package com.example.awesometimer.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Phase {

    public Phase(@NonNull String name){
        this.name = name;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

}
