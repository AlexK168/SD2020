package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Data.Repository;
import com.example.awesometimer.Models.Sequence;

import java.util.List;

public class SequenceViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Sequence>> mAllSequences;
    private Sequence currentSequence;

    public SequenceViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllSequences = mRepository.getAllSequences();
    }

    public LiveData<List<Sequence>> getAllSequences() { return mAllSequences; }

    public void insert(Sequence seq) { mRepository.insert(seq); }
    public void delete(Sequence seq) { mRepository.delete(seq); }
    public void setSelectedSeq(Sequence seq) {currentSequence = seq;}
    public void updateSelectedSeq(String newTitle, int newColor) {
        currentSequence.color = newColor;
        currentSequence.title = newTitle;
        mRepository.update(currentSequence);
    }
}
