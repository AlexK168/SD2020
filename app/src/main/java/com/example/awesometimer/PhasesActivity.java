package com.example.awesometimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.awesometimer.Adapters.PhaseAdapter;
import com.example.awesometimer.Adapters.SequenceAdapter;
import com.example.awesometimer.Models.Phase;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.ViewModels.PhaseViewModel;
import com.example.awesometimer.ViewModels.SequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PhasesActivity extends AppCompatActivity {
    public static final int NEW_PHASE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_PHASE_ACTIVITY_REQUEST_CODE = 2;
    public static String HINT_VALUE = "com.example.android.awesomeTimer.HINT_VALUE";
    public static final String EXTRA_REPLY = "com.example.android.awesomeTimer.REPLY";

    private PhaseViewModel mPhaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phases);

        mPhaseViewModel = ViewModelProviders.of(this).get(PhaseViewModel.class);
        FloatingActionButton addPhase = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PhaseAdapter adapter = new PhaseAdapter(this);
        adapter.setOnItemClickListener(new PhaseAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int selectedPhaseId  = adapter.getItem(position).id;
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, selectedPhaseId);
                setResult(RESULT_OK, replyIntent);
            }

            @Override
            public void onEditItemClick(int position, View v) {
                Phase selectedPhase  = adapter.getItem(position);
                mPhaseViewModel.setSelectedPhase(selectedPhase);
                Intent intent = new Intent(PhasesActivity.this, AddPhase.class);
                intent.putExtra(HINT_VALUE, selectedPhase.name);
                startActivityForResult(intent, EDIT_PHASE_ACTIVITY_REQUEST_CODE);
            }
        });

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
                        Phase phase = adapter.getItem(position);
                        Toast.makeText(PhasesActivity.this, "Deleting " +
                                phase.name, Toast.LENGTH_LONG).show();
                        mPhaseViewModel.delete(phase);
                    }
                });

        helper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPhaseViewModel.getAllPhases().observe(this, adapter::setPhases);

        addPhase.setOnClickListener(v -> {
            Intent intent = new Intent(PhasesActivity.this, AddPhase.class);
            startActivityForResult(intent, NEW_PHASE_ACTIVITY_REQUEST_CODE);
        });

        mPhaseViewModel.getAllPhases().observe(this, adapter::setPhases);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PHASE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Phase phase = new Phase(data.getStringExtra(AddPhase.EXTRA_REPLY));
            mPhaseViewModel.insert(phase);
        } else if (requestCode == EDIT_PHASE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String newnName = data.getStringExtra(AddPhase.EXTRA_REPLY);
            mPhaseViewModel.updateSelectedPhase(newnName);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}