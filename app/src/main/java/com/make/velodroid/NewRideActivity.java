package com.make.velodroid;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

public class NewRideActivity extends GoogleApiActivity {

    private Place mCurPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ride);


        SupportPlaceAutocompleteFragment placeFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.places_fragment);
        placeFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mCurPlace = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Anything important to do here.
            }
        });

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewRideActivity.this, MapActivity.class);
                intent.putExtra(MapActivity.RIDE_EXTRA, createRide());
                startActivity(intent);
            }
        });
    }

    /**
     * Creates a new Ride from your current location the place last typed in.
     *
     * @return A new Ride object from the values in the current activity and the user's current
     * position.
     */
    private Ride createRide() {
        // TODO: Permission check, again!
        Location curLoc = LocationServices.FusedLocationApi.getLastLocation(getClient());

        LatLng latLng = mCurPlace.getLatLng();
        Location to = new Location("");
        to.setLatitude(latLng.latitude);
        to.setLongitude(latLng.longitude);

        return new Ride(curLoc, to);
    }
}
