package com.example.awesometimer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.awesometimer.Adapters.SequenceAdapter;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.R;
import com.example.awesometimer.ViewModels.SequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String SEQUENCE_ID = "com.example.android.awesomeTimer.SEQUENCE_ID";
    public static String SEQUENCE_COLOR = "com.example.android.awesomeTimer.SEQUENCE_COLOR";
    public static String SEQUENCE_TITLE = "com.example.android.awesomeTimer.SEQUENCE_TITLE";

    public static final int NEW_SEQUENCE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_SEQUENCE_ACTIVITY_REQUEST_CODE = 2;

    private SequenceViewModel mSeqViewModel;
    private FloatingActionButton addSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AwesomeTimer);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSeqViewModel = ViewModelProviders.of(this).get(SequenceViewModel.class);
        addSeq = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        final SequenceAdapter adapter = new SequenceAdapter(this);
        adapter.setOnItemClickListener(new SequenceAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int id = adapter.getItem(position).id;
                    mSeqViewModel.getItems(id).observe(MainActivity.this, items -> {
                    ArrayList<Item> listToParse = new ArrayList<>(items);
                    Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                    intent.putParcelableArrayListExtra(SEQUENCE_ID, listToParse);
                    startActivity(intent);
                });
            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(MainActivity.this, EditSequence.class);
                Sequence s = adapter.getItem(position);
                intent.putExtra(SEQUENCE_ID, s.id);
                intent.putExtra(SEQUENCE_TITLE, s.title);
                intent.putExtra(SEQUENCE_COLOR, s.color);
                startActivityForResult(intent, EDIT_SEQUENCE_ACTIVITY_REQUEST_CODE);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSeqViewModel.getAllSequences().observe(this, adapter::setSequences);

        addSeq.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddSequence.class);
            startActivityForResult(intent, NEW_SEQUENCE_ACTIVITY_REQUEST_CODE);
        });

        mSeqViewModel.getAllSequences().observe(this, adapter::setSequences);

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Sequence sequence = adapter.getItem(position);
                        Toast.makeText(MainActivity.this, "Deleting " +
                                sequence.title, Toast.LENGTH_LONG).show();
                        mSeqViewModel.delete(sequence);
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }
}