package com.example.disastermanagentsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Alert implements Parcelable {

    private String tag;
    private double lat;
    private double lang;
    private String address;
    private String alertPushId;

    public Alert() {
    }

    public Alert(String tag, double lat, double lang, String address, String alertPushId) {
        this.tag = tag;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.alertPushId = alertPushId;
    }

    protected Alert(Parcel in) {
        tag = in.readString();
        lat = in.readDouble();
        lang = in.readDouble();
        address = in.readString();
        alertPushId = in.readString();
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

    public String getAlertPushId() {
        return alertPushId;
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
        parcel.writeString(alertPushId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Double.compare(alert.lat, lat) == 0 && Double.compare(alert.lang, lang) == 0 && tag.equals(alert.tag) && address.equals(alert.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, lat, lang, address);
    }

    public static DiffUtil.ItemCallback<Alert> itemCallback = new DiffUtil.ItemCallback<Alert>() {
        @Override
        public boolean areItemsTheSame(@NonNull Alert oldItem, @NonNull Alert newItem) {
            return oldItem.getAddress().equals(newItem.getAddress());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Alert oldItem, @NonNull Alert newItem) {
            return oldItem.equals(newItem);
        }
    };
}
