package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Data.Repository;
import com.example.awesometimer.Models.Phase;

import java.util.List;

public class PhaseViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Phase>> mAllPhases;
    private Phase mSelectedPhase;

    public PhaseViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPhases = mRepository.getAllPhases();
    }

    public void setSelectedPhase(Phase phase){
        mSelectedPhase = phase;
    }

    public LiveData<List<Phase>> getAllPhases() { return mAllPhases; }

    public void insert(Phase phase) { mRepository.insert(phase); }

    public void delete(Phase phase) {mRepository.delete(phase);}

    public void updateSelectedPhase(String newnName) {
        mSelectedPhase.name = newnName;
        mRepository.update(mSelectedPhase);
    }
}
