package com.example.disastermanagentsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable {

    private String tag;
    private double lat;
    private double lang;
    private String address;

    public Alert() {
    }

    public Alert(String tag, double lat, double lang, String address) {
        this.tag = tag;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
    }

    protected Alert(Parcel in){
        tag = in.readString();
        lat = in.readDouble();
        lang = in.readDouble();
        address = in.readString();
    }

    public static final Creator<Alert> CREATOR = new Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeDouble(lat);
        parcel.writeDouble(lang);
        parcel.writeString(address);
    }
}
