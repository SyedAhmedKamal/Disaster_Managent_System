package com.example.disastermanagentsystem.view.fragments;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.disastermanagentsystem.databinding.FragmentMainBinding;
import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.viewmodel.AlertViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_DefaultViewModelFactories_ActivityEntryPoint;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@AndroidEntryPoint
public class MainFragment extends Fragment implements
        OnMapReadyCallback,
        EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainFragment";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FragmentMainBinding binding;
    private View mView;
    private AlertViewModel alertViewModel;
    private GoogleMap mMaps;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");

        binding = FragmentMainBinding.inflate(inflater, container, false);
        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: called");

        mView = view;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        hasPermission();
        initMap(view);


        binding.bottomSheetFab.setOnClickListener(view1 -> {
            NavDirections directions = MainFragmentDirections.actionMainFragmentToBottomSheetFragment();
            Navigation.findNavController(mView).navigate(directions);
        });

        binding.logoutLayout.setOnClickListener(view1 -> {
            auth.signOut();
            requireActivity().finish();
        });

        binding.deleteMarkerFab.setOnClickListener(view1 -> {
            NavDirections directions = MainFragmentDirections.actionMainFragmentToAllMarkerFragment();
            Navigation.findNavController(mView).navigate(directions);
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        mMaps.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void getAlerts() {
        alertViewModel.getAllAlerts().observe(getViewLifecycleOwner(), new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                mMaps.clear();
                ArrayList<Alert> list = new ArrayList<>(alerts);
                for (Alert alert : list) {
                    Log.d(TAG, "onChanged: " + alert.getAddress());
                    createAlertsOnMap(alert);
                }
            }
        });
    }

    private void createAlertsOnMap(Alert alert) {
        LatLng latLng = new LatLng(alert.getLat(), alert.getLang());
        mMaps.addMarker(new MarkerOptions().position(latLng));
        mMaps.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));

        mMaps.addCircle(new CircleOptions()
                .center(latLng)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.circle_fill))
                .strokeColor(ContextCompat.getColor(requireContext(), R.color.circle_stroke))
                .strokeWidth(.2f)
                .clickable(false)
                .radius(100) // 100 meter
        );
    }

    private void initMap(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMap_main);
        mapFragment.getMapAsync(this);
    }

    private void hasPermission() {

        String[] perms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.SEND_SMS};

        } else {
            perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SEND_SMS};
        }
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            Log.d(TAG, "Already has permissions");
            geLocation();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "Require Permission",
                    REQUEST_LOCATION_PERMISSION,
                    perms);
        }

    }

    private void geLocation() {

        try {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        Log.d(TAG, "Fused Location: " + task.getResult());
                        gotoLocation(location.getLatitude(), location.getLongitude());

                        Geocoder geocoder = new Geocoder(requireContext());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses.size() > 0) {
                                Address address = addresses.get(0);
                                binding.tvAddress.setText(address.getAddressLine(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (SecurityException se) {
            Log.d(TAG, "geLocation: " + se.getLocalizedMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMaps = googleMap;
        try {
            mMaps.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            se.getLocalizedMessage();
        }
        setUISettings(mMaps);
    }

    private void setUISettings(GoogleMap maps) {
        maps.getUiSettings().setZoomControlsEnabled(true);
        maps.getUiSettings().setCompassEnabled(true);
        maps.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        getAlerts();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: called");
    }
}