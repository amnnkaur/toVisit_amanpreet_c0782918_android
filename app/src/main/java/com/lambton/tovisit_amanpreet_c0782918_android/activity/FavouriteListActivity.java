package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.adapter.FavListAdapter;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.util.ArrayList;
import java.util.List;

public class FavouriteListActivity extends AppCompatActivity {

    RecyclerView rvFavList;

    private FavPlaceRoomDB favPlaceRoomDB;
    FavListAdapter favListAdapter;

    List<FavPlace> favPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        rvFavList = findViewById(R.id.rvFavList);
        rvFavList.setLayoutManager(new LinearLayoutManager(this));
        rvFavList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvFavList.setAdapter(favListAdapter);

        favPlaceList = new ArrayList<>();
        favPlaceRoomDB = favPlaceRoomDB.getINSTANCE(this);

        loadPlaces();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);

        MenuItem searchItem = menu.findItem(R.id.btnSearch);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                favListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.btnAdd){

            Intent intent = new Intent(FavouriteListActivity.this, MainActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {
        favPlaceList = favPlaceRoomDB.favPlaceDao().getAllPlaces();

        favListAdapter = new FavListAdapter(this,R.layout.item_place, favPlaceList);
        rvFavList.setAdapter(favListAdapter);

    }
}