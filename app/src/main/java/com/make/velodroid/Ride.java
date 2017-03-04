package com.make.velodroid;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by matgrioni on 3/4/17.
 *
 * A model to represent the bike rides of the user from point A to point B.
 */

public class Ride implements Parcelable {
    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel source) {
            return new Ride(source);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };

    private Location from;
    private Location to;

    public Ride(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    private Ride(Parcel source) {
        this.from = source.readParcelable(Location.class.getClassLoader());
        this.to = source.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(from, 0);
        dest.writeParcelable(to, 0);
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
