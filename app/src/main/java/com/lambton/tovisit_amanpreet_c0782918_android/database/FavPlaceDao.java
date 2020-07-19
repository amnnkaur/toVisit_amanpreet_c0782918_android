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

    @Query("UPDATE place SET latitude = :latitude, longitude= :longitude, address = :address WHERE placeID = :id")
    int updatePlace(int id, Double latitude, Double longitude, String address);

    @Query("SELECT * FROM place")
    List<FavPlace> getAllPlaces();

    @Query("SELECT * FROM place WHERE placeID = :id")
    List<FavPlace> getSelectedPlaces(int id);
}
