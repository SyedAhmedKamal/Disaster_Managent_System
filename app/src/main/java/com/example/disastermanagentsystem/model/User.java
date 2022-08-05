package com.example.disastermanagentsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String email;
    private String username;
    private String password;
    private String userPhone;
    private String emNum1;
    private String emNum2;

    public User() {
    }

    public User(String email, String username, String password, String userPhone, String emNum1, String emNum2) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userPhone = userPhone;
        this.emNum1 = emNum1;
        this.emNum2 = emNum2;
    }

    protected User(Parcel in){
        email = in.readString();
        username = in.readString();
        password = in.readString();
        userPhone = in.readString();
        emNum1 = in.readString();
        emNum2 = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getEmNum1() {
        return emNum1;
    }

    public String getEmNum2() {
        return emNum2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(userPhone);
        parcel.writeString(emNum1);
        parcel.writeString(emNum2);
    }
}
