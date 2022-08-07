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
import android.widget.Toast;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.databinding.FragmentSiginUpBinding;
import com.example.disastermanagentsystem.model.User;
import com.example.disastermanagentsystem.viewmodel.AuthViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SiginUpFragment extends Fragment {

    private static final String TAG = "SiginUpFragment";
    
    private FragmentSiginUpBinding binding;
    private AuthViewModel authViewModel;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSiginUpBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        binding.registrationBtn.setOnClickListener(view1 -> {

            String email = binding.edUsernameTxtRes.getText().toString().trim();
            String password = binding.edPasswordTxtRes.getText().toString().trim();
            String c_password = binding.edConfirmPassTxtRes.getText().toString().trim();
            String name = binding.edNameTxtRes.getText().toString().trim();
            String phone = binding.edPhoneTxtRes.getText().toString().trim();
            String emNum1 = binding.edEmNumber1Res.getText().toString().trim();
            String emNum2 = binding.edEmNumber2Res.getText().toString().trim();
            String timeStamp = (new SimpleDateFormat("ddMMyyyyhhmmss")).format(new Date());

            if (email.isEmpty()) {
                binding.edUsernameRes.setError("Required");
            } else if (password.isEmpty()) {
                binding.edPasswordRes.setError("Required");
            } else if (c_password.isEmpty()) {
                binding.edConfirmPassRes.setError("Required");
            } else if (name.isEmpty()) {
                binding.edUsernameRes.setError("Required");
            } else if (phone.isEmpty()) {
                binding.edPhoneRes.setError("Required");
            } else if (emNum1.isEmpty()) {
                binding.edEmNumber1Res.setError("Required");
            } else if (emNum2.isEmpty()) {
                binding.edEmNumber2Res.setError("Required");
            } else {
                authViewModel.signIn(new User(email, name, password, phone, emNum1, emNum2))
                        .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean response) {
                                if (!response){
                                    Log.d(TAG, "onChanged: something went wrong");
                                    return;
                                }

                                NavDirections directions = SiginUpFragmentDirections.actionSiginUpFragmentToLoginFragment();
                                Navigation.findNavController(view).navigate(directions);
                            }
                        });
            }
        });
    }
}