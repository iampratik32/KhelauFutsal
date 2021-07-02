package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.example.khelaufutsal.RegisterDetailHolder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Futsal_MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView previousLocationTextView;
    boolean scrolled = false;
    private static final String TAG = "MapsActivity";
    ListView lstPlaces;
    LatLng latLngUser;
    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location userCurrentLocation;
    private static final int DEFAULT_ZOOM = 15;
    ImageView userLocation;
    TextView manageLocation;
    TextView locationTextView;
    TextView newLocationTextView;
    Toolbar toolbar;
    AppBarLayout mAppBarLayout;
    String address;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;


    @Override
    public void onBackPressed() {
        if(scrolled==false){
            super.onBackPressed();
        }
        else {
            mAppBarLayout.setExpanded(true);
            scrolled=false;
            toolbar.setVisibility(View.GONE);
            manageLocation.setText("Update Location ↡");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__maps);

        mAppBarLayout = findViewById(R.id.app_bar);

        toolbar = findViewById(R.id.toolbar);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });



        params.setBehavior(behavior);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocation = findViewById(R.id.myLocation);
        userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCurrentPlace();
            }
        });
        locationTextView = findViewById(R.id.textViwe);

        newLocationTextView = findViewById(R.id.newLocation_futsalMaps);
        newLocationTextView.setText(locationTextView.getText().toString());

        Button updateLocationButton = findViewById(R.id.updateLocationButton_futsalMaps);

        previousLocationTextView = findViewById(R.id.previousLocation_futsalMaps);

        if(FutsalHolder.getFutsal()!=null){
            String[] latlong =  FutsalHolder.getFutsal().getFutsalLocation().split(",");
            String location = ChangeCoordinates.changeToText(latlong,getApplicationContext());
            previousLocationTextView.setText(location);
        }
        else {
            if(RegisterDetailHolder.getAllDetails()!=null && RegisterDetailHolder.getAllDetails().size()==2){
                String[] latlong =  RegisterDetailHolder.getAllDetails().get(1).toString().split(",");
                String location = ChangeCoordinates.changeToText(latlong,getApplicationContext());
                previousLocationTextView.setText(location);
            }
            else {
                previousLocationTextView.setText("N/A");
            }
        }

        manageLocation = findViewById(R.id.manageLocation_futsalMaps);
        manageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrolled==false){
                    mAppBarLayout.setExpanded(false);
                    scrolled=true;
                    toolbar.setVisibility(View.VISIBLE);
                    manageLocation.setText("Change Location ↟");
                }
                else {
                    mAppBarLayout.setExpanded(true);
                    scrolled=false;
                    toolbar.setVisibility(View.GONE);
                    manageLocation.setText("Update Location ↡");
                }
            }
        });

        ImageView closeButton = findViewById(R.id.closeButton_futsalMaps);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newLocationTextView.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Choose Location To Continue",Toast.LENGTH_LONG).show();
                    return;
                }
                if(FutsalHolder.getFutsal()!=null){
                    DatabaseReference futsalDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("FutsalLocation");
                    futsalDb.setValue(address);
                    FutsalHolder.getFutsal().setFutsalLocation(address);
                    finish();
                }
                else {
                    Intent closeIntent = new Intent();
                    closeIntent.putExtra("5575",address);
                    setResult(Activity.RESULT_OK, closeIntent);
                    finish();
                }
            }
        });

        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getUserLocation();
        configureCameraIdle();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        // Add a marker in Sydney and move the camera
        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(27.712935,85.345142));
        if(latLngUser!=null){
            point = CameraUpdateFactory.newLatLng(latLngUser);
        }
        mMap.moveCamera(point);
        mMap.animateCamera(point);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        getLocationPermission();
    }

    private void getLocationPermission() {

        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getUserLocation(){
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            userCurrentLocation = task.getResult();
                            if(userCurrentLocation!=null){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(userCurrentLocation.getLatitude(),
                                                userCurrentLocation.getLongitude()), DEFAULT_ZOOM));
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            if(latLngUser!=null){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, DEFAULT_ZOOM));
                            }
                            else {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.712935,85.345142), DEFAULT_ZOOM));

                            }
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getUserLocation();
        } else {
            getLocationPermission();
        }
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(Futsal_MapsActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        if (!locality.isEmpty() && !country.isEmpty()){
                            locationTextView.setText(locality + "  " + country);
                            newLocationTextView.setText(locality+"  "+country);
                            address = addressList.get(0).getLatitude()+","+addressList.get(0).getLongitude();

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }


}
