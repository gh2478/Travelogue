package com.example.android.travelogue;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jordanhaynes on 7/10/18.
 */

public class Place implements Parcelable {
    public static final String PLACE_NAME = "PlaceName";

    public String placeKey;
    public String placeName;
    public String placeLocation;
    public String placeNotes;

    public Place() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Place(String name, String location, String notes) {
        this.placeName = name;
        this.placeLocation = location;
        this.placeNotes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(placeLocation);
        parcel.writeString(placeNotes);
    }

    // All Parcelables must have a CREATOR that implements these two methods. This is
    // used to regenerate the object
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    // Constructor that takes a Parcel and gives you an object populated with it's values
    private Place(Parcel in) {
        placeName = in.readString();
        placeLocation = in.readString();
        placeNotes = in.readString();
    }
}
