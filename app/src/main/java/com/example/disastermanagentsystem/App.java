package com.example.disastermanagentsystem;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.disastermanagentsystem.util.LocationWorker;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
        setUpWork();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
    }

    private void setUpWork() {
//        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                LocationWorker.class,
                15,
                TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "FetchLocation",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        periodicWorkRequest
                );
    }
}
