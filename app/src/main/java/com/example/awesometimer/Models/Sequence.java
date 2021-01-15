package com.example.awesometimer.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sequence {

    public Sequence(@NonNull String title, @NonNull int color) {this.title = title; this.color = color;}

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public int color;
}
