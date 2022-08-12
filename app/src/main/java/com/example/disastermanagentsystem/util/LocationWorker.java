package com.example.disastermanagentsystem.util;

import static com.example.disastermanagentsystem.App.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationWorker extends Worker {

    private static final String TAG = "LocationWorkers";
    Context context;

    private FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: called");
        checkLocation();
        return Result.retry();
    }

    private void checkLocation() {

        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "onSuccess: LOCATION - " + location);
                            checkUserVicinity(location.getLatitude(), location.getLongitude());
                        }
                    });
        } catch (SecurityException se) {
            se.printStackTrace();
        }

    }

    private void checkUserVicinity(double latitude, double longitude) {
        Log.d(TAG, "checkUserVicinity: called");
        databaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            for (DataSnapshot snapshot2 : snapshot1.child("Alerts").getChildren()) {
                                Alert alert = snapshot2.getValue(Alert.class);
                                float[] result = new float[1];

                                Location.distanceBetween(alert.getLat(), alert.getLang(), latitude, longitude, result);

                                int dis = (int) Math.round(result[0]);

                                if (dis < 50) {
                                    Log.d(TAG, "onDataChange: DANDER - " + dis + "m");
                                    createNotification(dis);

                                    databaseReference
                                            .child(firebaseAuth.getUid())
                                            .child("Profile_Information")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                                    if (userSnapshot.exists()) {
                                                        User user = userSnapshot.getValue(User.class);
                                                        Log.d(TAG, "onDataChange: called___________");
                                                        sendSms(user.getEmNum1(), user.getEmNum2());
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getDetails());
                    }
                });
    }

    private void sendSms(String emNum1, String emNum2) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> num = new ArrayList<>();
            num.add(emNum1);
            num.add(emNum2);
            for (String desNumber : num) {
                Log.d(TAG, "sendSms: to "+desNumber);
                smsManager.sendTextMessage(desNumber, null, "hello", null, null);
            }
        } catch (Exception e) {
            Log.d(TAG, "sendSms: "+e.getLocalizedMessage());
        }
    }

    private void createNotification(int dis) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("CAUTION")
                .setContentInfo("Danger close")
                .setContentText("Danger close - " + dis + "m vicinity")
                .setSmallIcon(R.drawable.main_logo)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(100, notification);

    }
}
