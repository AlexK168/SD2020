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

    public static final String EXTRA_REPLY =
            "com.example.android.awesomeTimer.REPLY";

    private EditText mEditWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phase);

        mEditWordView = findViewById(R.id.edit_word);
        String hint = getIntent().getStringExtra(PhasesActivity.HINT_VALUE);
        if (hint != null) {
            mEditWordView.setHint(hint);
        }

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String phase = mEditWordView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, phase);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}