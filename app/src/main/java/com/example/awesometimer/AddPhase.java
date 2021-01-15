package com.example.awesometimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPhase extends AppCompatActivity {

    public static final String PHASE_EXTRA_REPLY =
            "com.example.android.awesomeTimer.PHASE_REPLY";

    public static final String TIME_EXTRA_REPLY =
            "com.example.android.awesomeTimer.TIME_REPLY";

    private EditText mEditWordView;
    private EditText mEditTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phase);

        mEditWordView = findViewById(R.id.edit_word);
        mEditTimeView = findViewById(R.id.editTextTime2);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String phase = mEditWordView.getText().toString();
                int time = Integer.parseInt(mEditTimeView.getText().toString());
                replyIntent.putExtra(PHASE_EXTRA_REPLY, phase);
                replyIntent.putExtra(TIME_EXTRA_REPLY, time);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}