package com.example.disastermanagentsystem.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.disastermanagentsystem.model.Alert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlertRepository {

    private static final String TAG = "AlertRepository";

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    MutableLiveData<List<Alert>> alertResponseMutableLiveData;
    MutableLiveData<Boolean> createAlertMutableLiveData;
    private ArrayList<Alert> alertArrayList;

    @Inject
    public AlertRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        alertResponseMutableLiveData = new MutableLiveData<>();
        createAlertMutableLiveData = new MutableLiveData<>();
        alertArrayList = new ArrayList<>();
    }

    public MutableLiveData<Boolean> postAlert(Alert alert){
        String id = databaseReference.push().getKey();
        databaseReference
                .child(firebaseAuth.getUid())
                .child("Alerts")
                .child(id)
                .setValue(alert)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            createAlertMutableLiveData.postValue(task.isSuccessful());
                            Log.d(TAG, "onComplete: "+task.isSuccessful());
                        }
                        else{
                            createAlertMutableLiveData.postValue(false);
                            Log.d(TAG, "onComplete: false");
                        }
                    }
                });
        return createAlertMutableLiveData;
    }

    public MutableLiveData<List<Alert>> getAllAlerts(){
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshot1:snapshot.getChildren()) {

                            Log.d(TAG, "onDataChange1: "+snapshot.getValue());

                            for (DataSnapshot snapshot2:snapshot1.child("Alerts").getChildren()) {
                                Log.d(TAG, "onDataChange2: "+snapshot2.getValue());
                                Alert alert = snapshot2.getValue(Alert.class);

                                alertArrayList.add(alert);
                                alertResponseMutableLiveData.postValue(alertArrayList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: "+error.getDetails());
                    }
                });
        return alertResponseMutableLiveData;
    }
}
