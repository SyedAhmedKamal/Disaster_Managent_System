package com.example.disastermanagentsystem.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.databinding.FragmentSplashBinding;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FragmentSplashBinding binding;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        if (firebaseAuth.getCurrentUser() != null){

            binding.btnSignupSp.setVisibility(View.INVISIBLE);
            binding.btnLoginSp.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NavDirections directions = SplashFragmentDirections.actionSplashFragmentToMainFragment();
                    Navigation.findNavController(mView).navigate(directions);
                }
            }, 1000);
        }

        binding.btnSignupSp.setOnClickListener(view1 -> {
            NavDirections directions = SplashFragmentDirections.actionSplashFragmentToSiginUpFragment();
            Navigation.findNavController(view).navigate(directions);
        });

        binding.btnLoginSp.setOnClickListener(view1 -> {
            NavDirections directions = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
            Navigation.findNavController(view).navigate(directions);
        });
    }
}