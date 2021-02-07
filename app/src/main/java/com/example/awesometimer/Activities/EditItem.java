package com.example.awesometimer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awesometimer.R;
import com.example.awesometimer.Utils.StringUtils;

public class EditItem extends AppCompatActivity {
    public final static String TIME_EXTRA_REPLY = "com.example.android.awesomeTimer.TIME_EXTRA_REPLY";
    public final static String PHASE_EXTRA_REPLY = "com.example.android.awesomeTimer.PHASE_EXTRA_REPLY";
    private TextView editItem;
    private TextView editHours;
    private TextView editMinutes;
    private TextView editSeconds;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySettingsActivity.SetConfigurations(this);
        setContentView(R.layout.activity_edit_item);
        editItem = findViewById(R.id.edit_item_name);
        editHours = findViewById(R.id.editTextHours);
        editMinutes = findViewById(R.id.editTextMinutes);
        editSeconds = findViewById(R.id.editTextSeconds);
        save = findViewById(R.id.button2);

        editItem.setText(getIntent().getStringExtra(EditSequence.ITEM_PHASE));
        int totalTime = getIntent().getIntExtra(EditSequence.ITEM_DURATION, 30);
        int h = totalTime / 3600;
        totalTime = totalTime % 3600;
        int m = totalTime / 60;
        totalTime = totalTime % 60;
        int s = totalTime;

        editSeconds.setText(String.valueOf(s));
        editMinutes.setText(String.valueOf(m));
        editHours.setText(String.valueOf(h));

        save.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            String hours = editHours.getText().toString();
            String mins = editMinutes.getText().toString();
            String secs = editSeconds.getText().toString();
            if(!StringUtils.isInteger(hours) || !StringUtils.isInteger(mins) || !StringUtils.isInteger(secs)) {
                Toast.makeText(getApplicationContext(), "Enter correct value!", Toast.LENGTH_SHORT).show();
                return;
            }
            int total = Integer.parseInt(hours) * 3600 + Integer.parseInt(mins) * 60 + Integer.parseInt(secs);
            if (editItem.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Name is empty dude", Toast.LENGTH_SHORT).show();
                return;
            }
            replyIntent.putExtra(PHASE_EXTRA_REPLY, editItem.getText().toString());
            replyIntent.putExtra(TIME_EXTRA_REPLY, total);
            setResult(RESULT_OK, replyIntent);
            finish();
        });
    }
}