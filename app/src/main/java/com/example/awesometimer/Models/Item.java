package com.example.awesometimer.Models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Item implements Parcelable {

    public Item(int duration, int id_sequence, @NonNull String phase) {
        this.duration = duration;
        this.id_sequence = id_sequence;
        this.phase = phase;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int duration;

    @NonNull
    public String phase;

    public int id_sequence;

    @Ignore
    protected Item(Parcel in) {
        duration = in.readInt();
        id = in.readInt();
        id_sequence = in.readInt();
        phase = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.duration);
        dest.writeInt(this.id);
        dest.writeInt(this.id_sequence);
        dest.writeString(this.phase);
    }
}
