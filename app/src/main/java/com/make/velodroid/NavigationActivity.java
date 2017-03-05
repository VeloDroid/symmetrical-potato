package com.make.velodroid;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.akexorcist.googledirection.constant.Maneuver;
import com.akexorcist.googledirection.model.Step;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.make.velodroid.comm.VeloBitMasksFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matgrioni on 3/4/17.
 */

public class NavigationActivity extends GoogleApiActivity implements LocationListener {
    public static final String STEPS_EXTRA = "steps";

    private TextView info;

    private VeloBluetoothService vbs;
    private VeloBitMasksFactory fac;

    private List<Step> steps;
    private int curStepIndex;
    private float prevBearing;

    private boolean firstTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        info = (TextView) findViewById(R.id.info);

        vbs = VeloBluetoothService.getInstance();
        fac = new VeloBitMasksFactory();

        Bundle b = getIntent().getExtras();
        steps = b.getParcelableArrayList(STEPS_EXTRA);

        curStepIndex = 1;

        firstTime = false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);
        // TODO: Check permission.
        LocationServices.FusedLocationApi.requestLocationUpdates(getClient(), request, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Step curStep = steps.get(curStepIndex);
        String curManeuver = curStep.getManeuver();

        LatLng stepLatLng = new LatLng(curStep.getStartLocation().getLatitude(), curStep.getStartLocation().getLongitude());
        LatLng curLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        float meters = dist(stepLatLng, curLatLng);

        if (!firstTime) {
            if (meters < 5 && Math.abs(prevBearing - location.getBearing()) > 60) {
                curStepIndex++;
            }
        } else {
            firstTime = false;
        }

        prevBearing = location.getBearing();

        info.setText(Float.toString(meters));

        byte msg = fac.createBitMask(curManeuver, meters);
        vbs.write(msg);
    }

    private float dist(LatLng from, LatLng to) {
        float[] results = new float[3];
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results);
        return results[0];
    }
}
