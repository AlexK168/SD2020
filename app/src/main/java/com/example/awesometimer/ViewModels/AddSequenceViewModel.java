package com.example.awesometimer.ViewModels;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.awesometimer.Data.Repository;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddSequenceViewModel extends AndroidViewModel {
    private Repository mRepository;

    private Sequence sequence;
    private MutableLiveData<ArrayList<Item>> items;
    public int id_phase;

    public AddSequenceViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        sequence = new Sequence("Timer", Color.RED);
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
    }

    public void setColor(int color) {
        sequence.color = color;
    }

    public void setTitle(String title) {
        sequence.title = title;
    }

    public int color() {
        return sequence.color;
    }

    public String title() {
        return sequence.title;
    }

    public int id_sequence() {
        return sequence.id;
    }

    public void addItem(Item item) {
        ArrayList<Item> newItems = items.getValue();
        newItems.add(item);
        items.setValue(newItems);
    }

    public void save(){
        mRepository.insert(sequence);
        mRepository.insert(items.getValue());
    }

    public void update(String phase, int time) {
        ArrayList<Item> list = items.getValue();
        for (int i= 0 ;i< 0; i++) {
            if (list.get(i).id == id_phase) {
                list.set(i, new Item(time, sequence.id, phase));
            }
        }
        items.setValue(list);
    }

    public LiveData<ArrayList<Item>> getItems() { return items; }

}
