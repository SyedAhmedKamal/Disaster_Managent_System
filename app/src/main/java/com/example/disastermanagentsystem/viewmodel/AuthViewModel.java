package com.example.disastermanagentsystem.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.disastermanagentsystem.model.User;
import com.example.disastermanagentsystem.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    AuthRepository authRepository;
    @Inject
    public AuthViewModel(AuthRepository authRepository){
        this.authRepository = authRepository;
    }

    public MutableLiveData<Boolean> signIn(User user){
        return authRepository.signUp(user);
    }

    public MutableLiveData<Boolean> logIn(String email, String password){
        return authRepository.login(email, password);
    }

}
