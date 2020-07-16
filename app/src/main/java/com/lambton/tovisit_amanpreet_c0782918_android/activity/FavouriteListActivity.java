package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lambton.tovisit_amanpreet_c0782918_android.R;

public class FavouriteListActivity extends AppCompatActivity {

    RecyclerView rvFavList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFavList = findViewById(R.id.rvFavList);
        rvFavList.setLayoutManager(new LinearLayoutManager(this));


    }
}