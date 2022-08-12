package com.example.disastermanagentsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.disastermanagentsystem.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setVisibility(View.VISIBLE);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.splashFragment, R.id.mainFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController,
                                             @NonNull NavDestination navDestination,
                                             @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.loginFragment ||
                        navDestination.getId() == R.id.siginUpFragment ||
                        navDestination.getId() == R.id.splashFragment) {
                    binding.toolbar.setVisibility(View.GONE);
                } else {
                    binding.toolbar.setVisibility(View.VISIBLE);
                }

                if (navDestination.getId() == R.id.mainFragment){
                    setUserNameAtAppBar();
                }
                else{
                    binding.toolbar.setTitle(navDestination.getLabel());
                }
            }
        });
    }

    private void setUserNameAtAppBar() {
        if (auth.getCurrentUser().getDisplayName() != null){

            databaseReference
                    .child(auth.getUid())
                    .child("Profile_Information")
                    .child("username")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Log.d(TAG, "onDataChange: "+snapshot.getValue());
                                String name = snapshot.getValue().toString();
                                String[] first_name = name.split(" ");
                                binding.toolbar.setTitle("Hello,\n"+first_name[0].toUpperCase());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: "+error);
                        }
                    });

        }
    }
}