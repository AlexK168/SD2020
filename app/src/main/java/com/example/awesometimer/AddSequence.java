package com.example.awesometimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awesometimer.Adapters.ItemAdapter;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.ViewModels.AddSequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

public class AddSequence extends AppCompatActivity implements ColorPickerDialogListener {
    public static final String COLOR_EXTRA_REPLY = "com.example.android.awesomeTimer.COLOR_REPLY";
    public static final String TITLE_EXTRA_REPLY = "com.example.android.awesomeTimer.TITLE_REPLY";
    public static final int NEW_ITEM_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_ITEM_ACTIVITY_REQUEST_CODE = 2;
    private static final int firstId = 1;


    private EditText mEditWordView;
    private FloatingActionButton fab;
    private AddSequenceViewModel mAddSequenceViewModel;
    private Button pickColor;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sequence);

        mAddSequenceViewModel = ViewModelProviders.of(this).get(AddSequenceViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ItemAdapter adapter = new ItemAdapter(this);
        adapter.setOnItemClickListener(new ItemAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                mAddSequenceViewModel.id_phase = adapter.getItem(position).id;
                Intent intent = new Intent(AddSequence.this, AddPhase.class);
                startActivityForResult(intent, EDIT_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEditWordView = findViewById(R.id.edit_word);
        mEditWordView.setBackgroundColor(Color.RED);
        fab = findViewById(R.id.fab);
        button = findViewById(R.id.button_save);
        pickColor = findViewById(R.id.pick_color);

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String title = mEditWordView.getText().toString();
                mAddSequenceViewModel.setTitle(title);
                mAddSequenceViewModel.save();
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        pickColor.setOnClickListener(v -> createColorPickerDialog(firstId));

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AddSequence.this, AddPhase.class);
            startActivityForResult(intent, NEW_ITEM_ACTIVITY_REQUEST_CODE);
        });

        mAddSequenceViewModel.getItems().observe(this, adapter::setItems);
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
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String phase = data.getStringExtra(AddPhase.PHASE_EXTRA_REPLY);
            int time = data.getIntExtra(AddPhase.TIME_EXTRA_REPLY, 30);
            if (requestCode == NEW_ITEM_ACTIVITY_REQUEST_CODE) {
                Item item = new Item(time, mAddSequenceViewModel.id_sequence(), phase);
                mAddSequenceViewModel.addItem(item);
            }
            else if (requestCode == EDIT_ITEM_ACTIVITY_REQUEST_CODE) {
                mAddSequenceViewModel.update(phase, time);
            }
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        mAddSequenceViewModel.setColor(color);
        mEditWordView.setBackgroundColor(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
    }
}