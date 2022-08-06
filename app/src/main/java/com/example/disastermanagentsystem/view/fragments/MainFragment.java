package com.example.disastermanagentsystem.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.databinding.FragmentMainBinding;

import dagger.hilt.android.AndroidEntryPoint;
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_DefaultViewModelFactories_ActivityEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        binding.bottomSheetFab.setOnClickListener(view1 -> {
            NavDirections directions = MainFragmentDirections.actionMainFragmentToBottomSheetFragment();
            Navigation.findNavController(mView).navigate(directions);
        });
    }
}