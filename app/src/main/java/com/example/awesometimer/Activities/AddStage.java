package com.example.awesometimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import com.example.awesometimer.R;

public class AddStage extends AppCompatActivity {

    public static final String PHASE_EXTRA_REPLY =
            "com.example.android.awesomeTimer.PHASE_REPLY";

    private EditText mEditWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySettingsActivity.SetConfigurations(this);
        setContentView(R.layout.activity_add_phase);

        mEditWordView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String phase = mEditWordView.getText().toString();
                replyIntent.putExtra(PHASE_EXTRA_REPLY, phase);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}