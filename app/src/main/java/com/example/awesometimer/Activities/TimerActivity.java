package com.example.awesometimer.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.awesometimer.Adapters.TimerItemAdapter;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.R;
import com.example.awesometimer.TimerService;
import com.example.awesometimer.ViewModels.TimerViewModel;

import java.util.ArrayList;

import static android.Manifest.permission.FOREGROUND_SERVICE;

public class TimerActivity extends AppCompatActivity {

    public static final String TIME = "com.example.android.awesomeTimer.TIME";
    public static final String ITEM_LIST = "com.example.android.awesomeTimer.ITEM_LIST";
    public static final String PHASE = "com.example.android.awesomeTimer.PHASE";
    private TimerViewModel mTimerViewModel;
    private TextView mPhaseTextView;
    private TextView mTimeTextView;
    private ImageButton forward;
    private ImageButton backward;
    private ImageButton playButton;
    private ImageButton stopButton;
    private TimerService mService;
    private TimerItemAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ActivityCompat.requestPermissions(this, new String[]{FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

        mTimerViewModel = ViewModelProviders.of(this).get(TimerViewModel.class);

        mPhaseTextView = findViewById(R.id.itemTextView);
        mTimeTextView = findViewById(R.id.timeRemainTextView);
        playButton = findViewById(R.id.pauseButton);
        forward = findViewById(R.id.nextButton);
        backward = findViewById(R.id.prevButton);
        stopButton = findViewById(R.id.stopButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        adapter = new TimerItemAdapter(this);
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra(MainActivity.SEQUENCE_ID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // starting service
        Intent startIntent = new Intent(TimerActivity.this, TimerService.class);
        Bundle bundle = new Bundle();
        ArrayList<Item> listToParse = new ArrayList<>(items);
        bundle.putParcelableArrayList(ITEM_LIST, listToParse);
        startIntent.putExtras(bundle);
        startService(startIntent);

        // bind to service
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // updating UI according to model
        mTimerViewModel.getPhase().observe(this, s -> mPhaseTextView.setText(s));
        mTimerViewModel.getTimeRemain().observe(this, i -> mTimeTextView.setText(i.toString()));
        mTimerViewModel.isPaused().observe(this, aBoolean -> {
            playButton.setImageResource(aBoolean ? R.drawable.ic_play : R.drawable.ic_pause);
        });

        // service handles button clicks
        forward.setOnClickListener(v -> mService.forward()); // next stage
        backward.setOnClickListener(v -> mService.backward()); // prev stage
        stopButton.setOnClickListener(v -> { // stop and quit
            mService.stopSequence();
            finish();
        });
        playButton.setOnClickListener(v -> {
            Boolean state = mTimerViewModel.isPaused().getValue();
            mTimerViewModel.flip();
            if(state){
                mService.startSequence(); // play
            } else {
                mService.pauseSequence(); // pause
            }
        });

        // updating model when getting message from service //
        IntentFilter updateFilter = new IntentFilter();
        updateFilter.addAction(TimerService.UPDATE_ACTION);
        BroadcastReceiver updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int timeRemaining = intent.getIntExtra(TIME, -1);
                if (timeRemaining != -1) {
                    mTimerViewModel.setTimeRemain(timeRemaining);
                }
                String phase = intent.getStringExtra(PHASE);
                if (phase != null) {
                    mTimerViewModel.setPhase(phase);
                }
            }
        };
        registerReceiver(updateReceiver, updateFilter);

        IntentFilter finishFilter = new IntentFilter();
        finishFilter.addAction(TimerService.FINISH_ACTION);
        BroadcastReceiver finishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTimerViewModel.setIsPaused(true);
            }
        };
        registerReceiver(finishReceiver, finishFilter);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            TimerService.LocalBinder binder = (TimerService.LocalBinder) service;
            mService = binder.getService();
            if (mService.isStarted()) {
                adapter.setItems(mService.getItems());
                mTimerViewModel.init(mService.currentPhase(), mService.timeRemain());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) { }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}