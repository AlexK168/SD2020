package com.example.awesometimer.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Sequence {

    public Sequence(@NonNull String title, int color) {this.title = title; this.color = color;}

    @Ignore
    public Sequence(){this.title = "None";}

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public int color;
}
