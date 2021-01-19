package com.example.awesometimer.ViewModels;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Repos.SequenceRepo;

public class AddSequenceViewModel extends AndroidViewModel {
    private SequenceRepo mSeqRepo;

    private Sequence sequence;

    public AddSequenceViewModel(@NonNull Application application) {
        super(application);
        sequence = new Sequence("None", Color.RED);
        mSeqRepo = new SequenceRepo(application);
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

    public void save() {
        mSeqRepo.insert(sequence);
    }
}
