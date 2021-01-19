package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Repos.ItemRepo;
import com.example.awesometimer.Repos.SequenceRepo;

import java.util.List;

public class EditSequenceViewModel extends AndroidViewModel {

    private ItemRepo mItemRepo;
    private SequenceRepo mSeqRepo;

    private Sequence sequence;
    private int id;

    private LiveData<List<Item>> mItems;
    public LiveData<List<Item>> getItems() { return mItems; }

    public EditSequenceViewModel(@NonNull Application application, int param) {
        super(application);
        mItemRepo = new ItemRepo(application, param);
        mSeqRepo = new SequenceRepo(application);
        mItems = mItemRepo.getItems();
        sequence = new Sequence();
        sequence.id = param;
    }

    public void insert(Item item) {
        mItemRepo.insert(item);
    }

    public void delete(Item item) { mItemRepo.delete(item); }

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

    public int id_seq() {return sequence.id;}

    public Boolean save(String title) {
        if (title.isEmpty()){
            return false;
        }
        sequence.title = title;
        mSeqRepo.update(sequence);
        return true;
    }
}
