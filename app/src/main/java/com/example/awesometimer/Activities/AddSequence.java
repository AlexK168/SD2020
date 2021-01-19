package com.example.awesometimer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awesometimer.R;
import com.example.awesometimer.ViewModels.AddSequenceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

public class AddSequence extends AppCompatActivity implements ColorPickerDialogListener {
    public static final String COLOR_EXTRA_REPLY = "com.example.android.awesomeTimer.COLOR_REPLY";
    public static final String TITLE_EXTRA_REPLY = "com.example.android.awesomeTimer.TITLE_REPLY";
    private static final int firstId = 1;

    private EditText mEditWordView;
    private Button pickColor;
    private Button button;

    private AddSequenceViewModel mAddSequenceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sequence);

        mAddSequenceViewModel = ViewModelProviders.of(this).get(AddSequenceViewModel.class);
        mEditWordView = findViewById(R.id.edit_word);
        mEditWordView.setBackgroundColor(Color.RED);
        button = findViewById(R.id.button_save);
        pickColor = findViewById(R.id.pick_color);

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
                Toast.makeText(getApplicationContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
            } else {
                String title = mEditWordView.getText().toString();
                mAddSequenceViewModel.setTitle(title);
                mAddSequenceViewModel.save();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        pickColor.setOnClickListener(v -> createColorPickerDialog(firstId));
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