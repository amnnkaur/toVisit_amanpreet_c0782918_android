package com.lambton.tovisit_amanpreet_c0782918_android.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavPlaceDao {

    @Insert
    void insertPlace(FavPlace favPlace);

    @Query("DELETE FROM place")
    void deleteAllPlaces();

    @Query("DELETE FROM place WHERE placeID = :id")
    int deletePlace(int id);

    @Query("UPDATE place SET latitude = :latitude, longitude= :longitude, date = :date, address = :address, status = :status WHERE placeID = :id")
    int updatePlace(int id, Double latitude, Double longitude, String date, String address, boolean status);

    @Query("SELECT * FROM place")
    List<FavPlace> getAllPlaces();

    @Query("SELECT * FROM place WHERE placeID = :id")
    List<FavPlace> getSelectedPlaces(int id);

    @Query("SELECT * FROM place WHERE status = :status ")
    List<FavPlace> getSelectedStatusPlaces(boolean status);
}
