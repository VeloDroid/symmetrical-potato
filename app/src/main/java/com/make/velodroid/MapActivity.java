package com.make.velodroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends GoogleApiActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    public static final String RIDE_EXTRA = "ride";

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 1000;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the parameterized ride.
        Bundle b = getIntent().getExtras();
        Ride r = b.getParcelable(RIDE_EXTRA);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Is this a problem or is there a way to remove the permission error.
                    mMap.setMyLocationEnabled(true);
                }

                break;
        }
    }

    /**
     * A utility method for checking if permissions have been given at runtime. If a given
     * permission has not been granted, then it is requested, with the id
     * REQUEST_CODE_ASK_PERMISSIONS.
     *
     * @param per The given permission to check.
     * @return True if the permission has already been granted. False otherwise.
     */
    private boolean checkAndRequestPermission(String per) {
        if (ContextCompat.checkSelfPermission(this, per) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { per }, REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(LOCATION_UPDATE_INTERVAL);
        // TODO: Check permission.
        LocationServices.FusedLocationApi.requestLocationUpdates(getClient(), request, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng mapsLoc = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate mapUpdate = CameraUpdateFactory.newLatLng(mapsLoc);
        mMap.animateCamera(mapUpdate);
    }
}
