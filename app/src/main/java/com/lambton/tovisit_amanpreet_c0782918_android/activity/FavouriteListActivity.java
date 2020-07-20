package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.adapter.FavListAdapter;
import com.lambton.tovisit_amanpreet_c0782918_android.adapter.MovedPlaceAdapter;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.util.ArrayList;
import java.util.List;

public class FavouriteListActivity extends AppCompatActivity {

    RecyclerView rvFavList;

    private FavPlaceRoomDB favPlaceRoomDB;
    FavListAdapter favListAdapter;
    FavPlace favPlace;

    List<FavPlace> favPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        getSupportActionBar().setTitle("Favourite Places");

        rvFavList = findViewById(R.id.rvFavList);
        rvFavList.setLayoutManager(new LinearLayoutManager(this));
        rvFavList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
//        rvFavList.setAdapter(favListAdapter);

        favPlaceList = new ArrayList<>();

        favPlaceRoomDB = FavPlaceRoomDB.getINSTANCE(this);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {

                    String address = favPlaceList.get(position).getAddress();
                    int id = favPlaceList.get(position).getPlaceID();
                    double lat = favPlaceList.get(position).getLatitude();
                    double longitude = favPlaceList.get(position).getLongitude();
                    String date = favPlaceList.get(position).getDate();

//                    favPlaceRoomDB.favPlaceDao().updatePlace(favPlaceList.get(position).getPlaceID(),favPlaceList.get(position).getLatitude(),favPlaceList.get(position).getLongitude(),favPlaceList.get(position).getDate(),favPlaceList.get(position).getAddress(),true);

//                  favPlaceList.clear();
//                  favListAdapter.getItemCount();

                    favPlaceRoomDB.favPlaceDao().deletePlace(favPlaceList.get(position).getPlaceID());
//                    favPlaceList.remove(position);

                    favListAdapter.notifyItemRemoved(position);

                    favListAdapter.notifyDataSetChanged();

                    favPlaceRoomDB.favPlaceDao().insertPlace(new FavPlace(lat,longitude,date,address,true));
                    startActivity(new Intent(FavouriteListActivity.this,MoveToActivity.class));
                    finish();
//                    Intent intent = new Intent(FavouriteListActivity.this, MoveToActivity.class);
//                    intent.putExtra("placeID", favPlaceList.get(position).getPlaceID());
//                    startActivity(intent);
//                    Toast.makeText(FavouriteListActivity.this, favPlaceList.get(position).getAddress(), Toast.LENGTH_SHORT).show();


//                    favPlaceList.remove(position);
//                    loadPlaces();

                } else if (direction == ItemTouchHelper.LEFT) {

                    deletePlace(position);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvFavList);

        loadPlaces();
    }

    private void deletePlace(final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete this?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                favPlaceRoomDB.favPlaceDao().deletePlace(favPlaceList.get(position).getPlaceID());
                favListAdapter.loadPlaces();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);

        MenuItem visitedItem = menu.findItem(R.id.btnVisited);
//
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                favListAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.btnAdd){

            MapsFragment.flagEdit = false;
            MainActivity.EDIT_CALL = false;

            Intent intent = new Intent(FavouriteListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        if (item.getItemId() == R.id.btnVisited){

            Intent intent = new Intent(FavouriteListActivity.this, MoveToActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {

        favPlaceList = favPlaceRoomDB.favPlaceDao().getSelectedStatusPlaces(false);

        favListAdapter = new FavListAdapter(this,R.layout.item_place, favPlaceList);
        rvFavList.setAdapter(favListAdapter);

    }

}