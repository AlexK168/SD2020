package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.awesometimer.Models.Stage;
import com.example.awesometimer.Repos.StageRepo;

import java.util.List;

public class StageViewModel extends AndroidViewModel {
    private StageRepo mRepository;

    private LiveData<List<Stage>> mAllStages;

    public StageViewModel (Application application) {
        super(application);
        mRepository = new StageRepo(application);
        mAllStages = mRepository.getAllStages();
    }

    public LiveData<List<Stage>> getAllStages() { return mAllStages; }

    public void insert(Stage seq) { mRepository.insert(seq); }
    public void delete(Stage seq) { mRepository.delete(seq); }
}
