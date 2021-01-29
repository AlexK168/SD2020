package com.example.awesometimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.awesometimer.Activities.MainActivity;
import com.example.awesometimer.Activities.TimerActivity;
import com.example.awesometimer.Models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    public static final String UPDATE_ACTION = "com.example.android.awesomeTimer.UPDATE_ACTION";
    public static final String FINISH_ACTION = "com.example.android.awesomeTimer.FINISH_ACTION";
    public static final String CHANNEL_ID = "com.example.android.awesomeTimer.CHANNEL_ID";
    public static final int NOTIFICATION_ID = 1;
    private final IBinder binder = new LocalBinder();
    private ArrayList<Item> items;
    private int currentIndex;
    private int size;
    private int timeRemain;
    private Timer timer;
    private Boolean isStarted;

    private void startNext(Integer direction) {
        sendNotification("Phase ended", items.get(currentIndex).phase);

        currentIndex += direction;
        Intent replyIntent = new Intent();
        replyIntent.setAction(UPDATE_ACTION);

        if (currentIndex >= size) {
            if (timer != null) {
                timer.cancel();
            }

            Intent finishIntent = new Intent();
            finishIntent.setAction(FINISH_ACTION);
            sendBroadcast(finishIntent);
            sendNotification("Sequence ended", "All phases accomplished");
            currentIndex = 0;
        }
        if (currentIndex < 0) {
            currentIndex = 0;
            if (timer != null) {
                timer.cancel();
            }
        }

        timeRemain = items.get(currentIndex).duration;

        replyIntent.putExtra(TimerActivity.PHASE, items.get(currentIndex).phase);
        replyIntent.putExtra(TimerActivity.TIME, items.get(currentIndex).duration);
        sendBroadcast(replyIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = false;
        items = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isStarted) {
            items = intent.getParcelableArrayListExtra(TimerActivity.ITEM_LIST);
            currentIndex = 0;
            size = items.size();
            timeRemain = 0;
            if (!items.isEmpty()) {
                timeRemain = items.get(0).duration;
            }
            isStarted = true;
        }
        sendNotification("Timer", "Waits for your click");
        return super.onStartCommand(intent, flags, startId);
    }

    public void startSequence(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Intent replyIntent = new Intent();
                replyIntent.setAction(UPDATE_ACTION);
                if (timeRemain <= 0) {
                    startNext(1);
                } else {
                    replyIntent.putExtra(TimerActivity.TIME, timeRemain);
                    sendBroadcast(replyIntent);
                    timeRemain--;
                }
            }
        }, 0, 1000);
    }

    public void pauseSequence() { timer.cancel(); }

    public void stopSequence(){
        if (timer != null) {
            timer.cancel();
        }
        stopSelf();
    }

    public void forward() { startNext(1); }

    public void backward() { startNext(-1); }

    public List<Item> getItems() { return items; }

    public Boolean isStarted() { return isStarted; }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return binder; }

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    public String currentPhase() {
        if (items.isEmpty()) {
            return "None";
        }
        return items.get(currentIndex).phase;
    }

    public int timeRemain() { return timeRemain; }

    private void sendNotification(String title, String text) {
        Intent notificationIntent = new Intent(TimerService.this, TimerActivity.class);
        notificationIntent.putParcelableArrayListExtra(MainActivity.SEQUENCE_ID, items);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(TimerService.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification =
                new Notification.Builder(TimerService.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .build();
        startForeground(NOTIFICATION_ID, notification);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Timer Service", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
