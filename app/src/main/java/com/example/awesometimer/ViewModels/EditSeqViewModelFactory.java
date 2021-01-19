package com.example.awesometimer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditSeqViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mParam;


    public EditSeqViewModelFactory(Application application, int param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditSequenceViewModel(mApplication, mParam);
    }
}