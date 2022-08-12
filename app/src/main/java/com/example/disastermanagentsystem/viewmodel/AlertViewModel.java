package com.example.disastermanagentsystem.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.repository.AlertRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AlertViewModel extends ViewModel {

    AlertRepository alertRepository;

    @Inject
    public AlertViewModel(AlertRepository alertRepository){
        this.alertRepository=alertRepository;
    }

    public MutableLiveData<Boolean> createAlert(String tag,Double lat,Double lang,String mAddress){
        return alertRepository.postAlert(tag, lat, lang, mAddress);
    }

    public MutableLiveData<List<Alert>> getAllAlerts(){
        return alertRepository.getAllAlerts();
    }

    public MutableLiveData<Boolean> deleteAlerts(String alertId){
        return alertRepository.deleteAlert(alertId);
    }
}
