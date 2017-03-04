package com.make.velodroid;

import android.location.Location;

/**
 * Created by matgrioni on 3/4/17.
 *
 * A model to represent the bike rides of the user from point A to point B.
 */

public class Ride {
    private Location from;
    private Location to;

    public Ride(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
