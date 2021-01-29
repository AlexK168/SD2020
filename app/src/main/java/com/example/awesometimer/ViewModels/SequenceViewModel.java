package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Repos.ItemRepo;
import com.example.awesometimer.Repos.SequenceRepo;

import java.util.List;

public class SequenceViewModel extends AndroidViewModel {

    private SequenceRepo mRepository;

    private LiveData<List<Sequence>> mAllSequences;

    public SequenceViewModel (Application application) {
        super(application);
        mRepository = new SequenceRepo(application);
        mAllSequences = mRepository.getAllSequences();
    }

    public LiveData<List<Sequence>> getAllSequences() { return mAllSequences; }

    public LiveData<List<Item>> getItems(int id) {
        return mRepository.getItems(id);
    }
    public void insert(Sequence seq) { mRepository.insert(seq); }
    public void delete(Sequence seq) { mRepository.delete(seq); }
}
