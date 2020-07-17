package com.lambton.tovisit_amanpreet_c0782918_android.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavPlaceDao {

    @Insert
    void insertContact(FavPlace favPlace);

    @Query("DELETE FROM place")
    void deleteAllContacts();

    @Query("DELETE FROM place WHERE placeID = :id")
    int deleteContact(int id);

    @Query("UPDATE place SET latitude = :latitude, longitude= :longitude, address = :address WHERE placeID = :id")
    int updateContact(int id, String latitude, String longitude, String address);

    @Query("SELECT * FROM place ORDER BY placeID")
    List<FavPlace> getAllPlaces();
}
