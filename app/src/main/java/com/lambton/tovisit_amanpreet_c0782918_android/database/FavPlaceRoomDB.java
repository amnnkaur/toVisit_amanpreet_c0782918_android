package com.lambton.tovisit_amanpreet_c0782918_android.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavPlace.class}, version = 1, exportSchema = false)
public abstract class FavPlaceRoomDB extends RoomDatabase {

    private static final String DB_NAME = "place_database";

    public abstract FavPlaceDao favPlaceDao();

    private static volatile FavPlaceRoomDB INSTANCE;

    public static FavPlaceRoomDB getINSTANCE(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavPlaceRoomDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }

}
