package com.example.disastermanagentsystem.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.databinding.FragmentLoginBinding;
import com.example.disastermanagentsystem.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private View mView;
    private AuthViewModel authViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;

        String email = binding.edUsernameTxt.getText().toString().trim();
        String password = binding.edPasswordTxt.getText().toString().trim();

        if (email.isEmpty()){
            binding.edUsernameTxt.setError("*Required");
        }
        else if(password.isEmpty()){
            binding.edPasswordTxt.setError("*Required");
        }
        else{
            authViewModel.logIn(email, password)
                    .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean response) {
                            if (!response){
                                Log.d(TAG, "onChanged: something went wrong");
                                return;
                            }

                            NavDirections directions= LoginFragmentDirections.actionLoginFragmentToMainFragment();
                            Navigation.findNavController(mView).navigate(directions);
                        }
                    });
        }
    }
}