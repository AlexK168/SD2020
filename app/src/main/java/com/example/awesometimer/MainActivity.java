package com.example.awesometimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.awesometimer.Adapters.SequenceAdapter;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.ViewModels.SequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private SequenceViewModel mSeqViewModel;
    private FloatingActionButton addSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeqViewModel = ViewModelProviders.of(this).get(SequenceViewModel.class);
        addSeq = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SequenceAdapter adapter = new SequenceAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSeqViewModel.getAllSequences().observe(this, adapter::setSequences);

        Button.OnClickListener onFabClicked = v -> {
            Intent intent = new Intent(MainActivity.this, AddSequence.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        };

        addSeq.setOnClickListener(onFabClicked);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Sequence seq = new Sequence(data.getStringExtra(AddSequence.EXTRA_REPLY), "HexVal");
            mSeqViewModel.insert(seq);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


}