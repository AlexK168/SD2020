package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Data.Repository;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Models.SequenceWithItems;

import java.util.List;

public class SequenceViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Sequence>> mAllSequences;
    private LiveData<List<SequenceWithItems>> mSequencesWithItems;

    public SequenceViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllSequences = mRepository.getAllSequences();
        mSequencesWithItems = mRepository.getSequencesWithItems();
    }

    public LiveData<List<Sequence>> getAllSequences() { return mAllSequences; }
    public LiveData<List<SequenceWithItems>> getSequencesWithItems() { return mSequencesWithItems; }

    public void insert(Sequence seq) { mRepository.insert(seq); }
    public void delete(Sequence seq) { mRepository.delete(seq); }
}
