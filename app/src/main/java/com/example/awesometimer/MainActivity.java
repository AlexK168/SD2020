package com.example.awesometimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSeqViewModel = ViewModelProviders.of(this).get(SequenceViewModel.class);
        addSeq = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        final SequenceAdapter adapter = new SequenceAdapter(this);
        adapter.setOnItemClickListener(new SequenceAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // start timer here
//                Sequence selectedSeq  = adapter.getItem(position);
//                mSeqViewModel.setSelectedSeq(selectedSeq);
//                Intent intent = new Intent(MainActivity.this, AddSequence.class);
//                intent.putExtra(SEQUENCE_COLOR, selectedSeq.color);
//                intent.putExtra(SEQUENCE_TITLE, selectedSeq.title);
//                intent.putExtra(SEQUENCE_ID, selectedSeq.id);
//                startActivityForResult(intent, EDIT_SEQUENCE_ACTIVITY_REQUEST_CODE);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSeqViewModel.getAllSequences().observe(this, adapter::setSequences);

        Button.OnClickListener onFabClicked = v -> {
            Intent intent = new Intent(MainActivity.this, AddSequence.class);
            startActivityForResult(intent, NEW_SEQUENCE_ACTIVITY_REQUEST_CODE);
        };

        addSeq.setOnClickListener(onFabClicked);

        mSeqViewModel.getAllSequences().observe(this, adapter::setSequences);

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        int newColor = data.getIntExtra(AddSequence.COLOR_EXTRA_REPLY, 1);
//        String newTitle = data.getStringExtra(AddSequence.TITLE_EXTRA_REPLY);
//        if (requestCode == NEW_SEQUENCE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Sequence seq = new Sequence(newTitle, newColor);
//            mSeqViewModel.insert(seq);
//        } else if (requestCode == EDIT_SEQUENCE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            mSeqViewModel.updateSelectedSeq(newTitle, newColor);
//        }
    }
}