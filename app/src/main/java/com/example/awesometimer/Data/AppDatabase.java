package com.example.awesometimer.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Models.Stage;

@Database(entities = {Sequence.class, Item.class, Stage.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SequenceDao SequenceDao();
    public abstract ItemDao ItemDao();
    public abstract StageDao StageDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}