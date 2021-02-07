package com.example.awesometimer.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Stage {
    @PrimaryKey
    @NonNull
    public String name;

    public Stage(@NonNull String name) {
        this.name = name;
    }
}
