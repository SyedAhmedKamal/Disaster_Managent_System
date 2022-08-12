package com.example.disastermanagentsystem.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.adapter.AlertMarkerAdapter;
import com.example.disastermanagentsystem.databinding.FragmentAllMarkerBinding;
import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.util.AdapterClickListener;
import com.example.disastermanagentsystem.viewmodel.AlertViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AllMarkerFragment extends Fragment implements AdapterClickListener {

    private FragmentAllMarkerBinding binding;
    private AlertViewModel alertViewModel;
    private AlertMarkerAdapter adapter;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllMarkerBinding.inflate(inflater, container, false);
        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;

        adapter = new AlertMarkerAdapter(Alert.itemCallback, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        alertViewModel.getAllAlerts().observe(getViewLifecycleOwner(), new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                initList(alerts);
            }
        });
    }

    private void initList(List<Alert> alerts) {
        if (alerts.size()>0){
            ArrayList<Alert> alertList = new ArrayList<>(alerts);
            adapter.submitList(alertList);
        }
    }

    @Override
    public void onDelete(Alert alert) {
        alertViewModel.deleteAlerts(alert.getAlertPushId()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean deleted) {
                if (deleted){
                    Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                    ArrayList<Alert> alerts = new ArrayList<>(adapter.getCurrentList());
                    alerts.remove(alert);
                    adapter.submitList(alerts);
                    Navigation.findNavController(mView).popBackStack();
                }
                else{
                    Toast.makeText(requireContext(), "Something went wrng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}