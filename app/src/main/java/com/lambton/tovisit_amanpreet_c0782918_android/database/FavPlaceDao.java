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
    int updatePlace(int id, String latitude, String longitude, String address);

    @Query("SELECT * FROM place ORDER BY placeID")
    List<FavPlace> getAllPlaces();
}
