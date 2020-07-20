package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.adapter.FavListAdapter;
import com.lambton.tovisit_amanpreet_c0782918_android.adapter.MovedPlaceAdapter;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.util.ArrayList;
import java.util.List;

public class MoveToActivity extends AppCompatActivity {

    MovedPlaceAdapter movedPlaceAdapter;
    RecyclerView rvMovedList;

    int placeID;

    List<FavPlace> favPlaceList;

    FavPlaceRoomDB favPlaceRoomDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to);

        getSupportActionBar().setTitle("Visited Places");

        favPlaceRoomDB = FavPlaceRoomDB.getINSTANCE(this);
        favPlaceList = new ArrayList<>();

        rvMovedList = findViewById(R.id.rvMovedLocation);

        rvMovedList.setLayoutManager(new LinearLayoutManager(this));
        rvMovedList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        rvMovedList.setAdapter(movedPlaceAdapter);

        placeID = getIntent().getIntExtra("placeID", 0);

        loadPlaces();
    }

    private void loadPlaces() {

        favPlaceList = favPlaceRoomDB.favPlaceDao().getAllPlaces();

        movedPlaceAdapter = new MovedPlaceAdapter(this,R.layout.item_place, favPlaceList);

        rvMovedList.setAdapter(movedPlaceAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MoveToActivity.this,FavouriteListActivity.class);
        startActivity(intent);
        finish();

    }
}