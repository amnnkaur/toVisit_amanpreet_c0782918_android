package com.lambton.tovisit_amanpreet_c0782918_android.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "place")
public class FavPlace {

    @PrimaryKey(autoGenerate = true)
    private int placeID;

    @NonNull
    @ColumnInfo(name = "latitude")
    private String latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private String longitude;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "address")
    private String address;

    public FavPlace(int placeID, @NonNull String latitude, @NonNull String longitude, @NonNull String date, @NonNull String address) {
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.address = address;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    @NonNull
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull String latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull String longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }
}
