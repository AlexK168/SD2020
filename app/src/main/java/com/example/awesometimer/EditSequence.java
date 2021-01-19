package com.example.awesometimer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.awesometimer.Adapters.ItemAdapter;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.ViewModels.EditSeqViewModelFactory;
import com.example.awesometimer.ViewModels.EditSequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.function.LongFunction;

public class EditSequence extends AppCompatActivity implements ColorPickerDialogListener {
    public static final int NEW_ITEM_ACTIVITY_REQUEST_CODE = 1;
    private static final int firstId = 1;
    private EditSequenceViewModel mEditSequenceViewModel;
    private EditText mEditWordView;
    private FloatingActionButton fab;
    private Button pickColor;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sequence);
        fab = findViewById(R.id.fab);
        button = findViewById(R.id.button_save);
        pickColor = findViewById(R.id.pick_color);
        mEditWordView = findViewById(R.id.edit_word);
        int id_sequence = intent.getIntExtra(MainActivity.SEQUENCE_ID, 0);
        int color = intent.getIntExtra(MainActivity.SEQUENCE_COLOR, Color.RED);
        String title = intent.getStringExtra(MainActivity.SEQUENCE_TITLE);
        mEditSequenceViewModel = new ViewModelProvider(this, new EditSeqViewModelFactory(this.getApplication(), id_sequence)).get(EditSequenceViewModel.class);
        mEditSequenceViewModel.setColor(color);
        mEditSequenceViewModel.setTitle(title);
        mEditWordView.setBackgroundColor(color);
        mEditWordView.setText(title);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent newIntent = new Intent(EditSequence.this, Stages.class);
            startActivityForResult(newIntent, NEW_ITEM_ACTIVITY_REQUEST_CODE);
        });

        button.setOnClickListener(v -> {
            if (mEditSequenceViewModel.save(mEditWordView.getText().toString())){
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ItemAdapter adapter = new ItemAdapter(this);
        adapter.setOnItemClickListener((position, v) -> {
            Log.e("Edit Sequence", "Edit item clicked");
            //
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Item item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "Deleting " +
                        item.phase, Toast.LENGTH_LONG).show();
                mEditSequenceViewModel.delete(item);
            }
         });

        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEditSequenceViewModel.getItems().observe(this, adapter::setItems);

        pickColor.setOnClickListener(v -> createColorPickerDialog(firstId));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String phase = data.getStringExtra(AddStage.PHASE_EXTRA_REPLY);
            int time = 30;
            Item item = new Item(time, mEditSequenceViewModel.id_seq(), phase);
            mEditSequenceViewModel.insert(item);
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        mEditSequenceViewModel.setColor(color);
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