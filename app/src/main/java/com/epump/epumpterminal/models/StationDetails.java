package com.epump.epumpterminal.models;

import android.os.Parcel;
import android.os.Parcelable;

import kotlinx.android.parcel.Parcelize;


public class StationDetails implements Parcelable {
    private String id;
    private String name;
    private String city;
    private Double latitude;
    private Double longitude;
    private String street;
    private String state;

    public StationDetails(String id, String name, String city, Double latitude, Double longitude, String street, String state) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.state = state;
    }


    protected StationDetails(Parcel in) {
        id = in.readString();
        name = in.readString();
        city = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        street = in.readString();
        state = in.readString();
    }

    public static final Creator<StationDetails> CREATOR = new Creator<StationDetails>() {
        @Override
        public StationDetails createFromParcel(Parcel in) {
            return new StationDetails(in);
        }

        @Override
        public StationDetails[] newArray(int size) {
            return new StationDetails[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(city);
        if (latitude != null) {
            parcel.writeDouble(latitude);
        }

        if (longitude != null) {
            parcel.writeDouble(longitude);
        }
        parcel.writeString(street);
        parcel.writeString(state);
    }
}
