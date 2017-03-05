package com.make.velodroid;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

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

    private LatLng from;
    private LatLng to;

    public Ride(LatLng from, LatLng to) {
        this.from = from;
        this.to = to;
    }

    private Ride(Parcel source) {
        this.from = source.readParcelable(LatLng.class.getClassLoader());
        this.to = source.readParcelable(LatLng.class.getClassLoader());
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

    public LatLng getFrom() {
        return from;
    }

    public LatLng getTo() {
        return to;
    }
}
