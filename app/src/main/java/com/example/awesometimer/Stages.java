package com.example.awesometimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.awesometimer.Adapters.StageAdapter;
import com.example.awesometimer.Models.Stage;
import com.example.awesometimer.ViewModels.StageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Stages extends AppCompatActivity {
    public static final int NEW_STAGE_ACTIVITY_REQUEST_CODE = 1;
    public static final String PHASE_EXTRA_REPLY = "com.example.android.awesomeTimer.PHASE_REPLY";
    private StageViewModel mStageViewModel;
    private FloatingActionButton addStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStageViewModel = ViewModelProviders.of(this).get(StageViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final StageAdapter adapter = new StageAdapter(this);
        adapter.setOnItemClickListener((position, v) -> {
            String name = adapter.getItem(position).name;
            Intent replyIntent = new Intent();
            replyIntent.putExtra(PHASE_EXTRA_REPLY, name);
            setResult(RESULT_OK, replyIntent);
            finish();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStageViewModel.getAllStages().observe(this, adapter::setStages);
        addStage = findViewById(R.id.fab);
        addStage.setOnClickListener(v -> {
            Intent intent = new Intent(Stages.this, AddStage.class);
            startActivityForResult(intent, NEW_STAGE_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String phase = data.getStringExtra(AddStage.PHASE_EXTRA_REPLY);
        if (requestCode == NEW_STAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Stage stage = new Stage(phase);
            mStageViewModel.insert(stage);
        }
    }
}