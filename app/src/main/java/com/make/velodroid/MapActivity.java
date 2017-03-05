package com.make.velodroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends GoogleApiActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    public static final String RIDE_EXTRA = "ride";

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 1000;

    private ArrayList<Step> steps;

    private boolean firstLocationChange;

    private GoogleMap mMap;
    private Ride r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        firstLocationChange = true;

        // Get the parameterized ride.
        Bundle b = getIntent().getExtras();
        r = b.getParcelable(RIDE_EXTRA);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, NavigationActivity.class);
                intent.putParcelableArrayListExtra(NavigationActivity.STEPS_EXTRA, steps);
                startActivity(intent);
            }
        });
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);

            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(r.getFrom())
                    .to(r.getTo())
                    .transportMode(TransportMode.BICYCLING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if (direction.getStatus().equals(RequestResult.OK)) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                steps = new ArrayList<Step>(leg.getStepList());
                                ArrayList<LatLng> points = leg.getDirectionPoint();


                                PolylineOptions options =
                                        DirectionConverter.createPolyline(MapActivity.this, points, 5, Color.RED);
                                googleMap.addPolyline(options);
                            } else {
                                // TODO: Display good error message.
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // TODO: Display good error message.
                        }
                    });
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
        if (firstLocationChange) {
            LatLng mapsLoc = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate mapUpdate = CameraUpdateFactory.newLatLng(mapsLoc);
            mMap.animateCamera(mapUpdate);
        }

        firstLocationChange = false;
    }
}
