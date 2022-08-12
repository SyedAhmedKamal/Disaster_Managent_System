package com.example.disastermanagentsystem.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.databinding.FragmentBottomSheetBinding;
import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.viewmodel.AlertViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAlertFragment extends Fragment implements OnMapReadyCallback {

    // TODO: 8/7/2022 "Write permission code" 
    
    private static final String TAG = "BottomSheetFragment";

    private GoogleMap mMaps;
    private FragmentBottomSheetBinding binding;
    private View mView;
    private static double lat;
    private static double lang;
    private static String mAddress;
    private AlertViewModel alertViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initMap(view);

        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);

        binding.create.setOnClickListener(view1 -> {
            String tag = binding.edTagTxt.getText().toString().trim();

            if (tag.isEmpty()) {
                binding.edTagTxt.setError("*Required");
            } else if (String.valueOf(lang).isEmpty() || mAddress.isEmpty()) {
                binding.mapText.setTextColor(Color.RED);
            } else {
                //Alert alert = new Alert(tag, lat, lang, mAddress);
                alertViewModel.createAlert(tag, lat, lang, mAddress).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean response) {
                        if (response) {
                            Log.d(TAG, "onChanged: Alert created");
                            Navigation.findNavController(mView).popBackStack();
                        }
                    }
                });
            }
        });

    }

    private void initMap(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMaps = googleMap;

        mMaps.getUiSettings().setCompassEnabled(true);
        mMaps.getUiSettings().setZoomControlsEnabled(true);
        try {
            mMaps.setMyLocationEnabled(true);
        }
        catch (SecurityException se){
            Log.d(TAG, "onMapReady: "+se.getLocalizedMessage());
        }

        mMaps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMaps.addMarker(new MarkerOptions().position(latLng));
                lat = latLng.latitude;
                lang = latLng.longitude;

                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        mAddress = address.getAddressLine(0);
                        Log.d(TAG, "onMapClick: " + mAddress);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}