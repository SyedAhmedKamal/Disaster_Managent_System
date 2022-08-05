package com.example.disastermanagentsystem.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.disastermanagentsystem.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class AuthRepository {

    private static final String TAG = "AuthRepository";

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    MutableLiveData<Object> getMutableLiveDataResponse;
    @Inject
    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        this.getMutableLiveDataResponse = new MutableLiveData<>();
    }

    public MutableLiveData<Object> signUp(User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && firebaseAuth.getUid() != null) {
                            databaseReference
                                    .child(firebaseAuth.getUid())
                                    .child("Profile_Information")
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "onComplete: " + task.isSuccessful());
                                                getMutableLiveDataResponse.postValue(task.getResult());
                                            } else {
                                                Log.d(TAG, "onComplete: " + task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
        return getMutableLiveDataResponse;
    }
}
