package com.lambton.tovisit_amanpreet_c0782918_android.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.activity.MainActivity;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MovedPlaceAdapter extends RecyclerView.Adapter<MovedPlaceAdapter.ListViewHolder> {

    private static final String TAG = "MovedPlaceAdapter";
    Context context;
    int layoutRes;
    List<FavPlace> movedPlaces;

    FavPlaceRoomDB favPlaceRoomDB;
    MainActivity mainActivity;

    ListViewHolder listViewHolder;

    public MovedPlaceAdapter(Context context, int resource, List<FavPlace> favPlaces) {

        this.movedPlaces = favPlaces;
        this.layoutRes = resource;
        this.context = context;

        favPlaceRoomDB = favPlaceRoomDB.getINSTANCE(context);

    }




    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, null);

        listViewHolder = new MovedPlaceAdapter.ListViewHolder(view);

        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {

        final FavPlace favPlace = movedPlaces.get(position);
        holder.address.setText(favPlace.getAddress());
        holder.latitude.setText(String.valueOf(favPlace.getLatitude()));
        holder.longitude.setText(String.valueOf(favPlace.getLongitude()));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        String addDate = simpleDateFormat.format(calendar.getTime());

        holder.date.setText(addDate);
        holder.itemView.setBackgroundColor(Color.GREEN);


    }

    @Override
    public int getItemCount() {
        return movedPlaces.size();
    }

    public void loadPlaces() {
        movedPlaces = favPlaceRoomDB.favPlaceDao().getAllPlaces();
        notifyDataSetChanged();

    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView address;
        TextView latitude;
        TextView longitude;
        TextView date;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.tv_address);
            latitude = itemView.findViewById(R.id.tv_latitude);
            longitude = itemView.findViewById(R.id.tv_longitude);
            date = itemView.findViewById(R.id.tv_date);

        }

    }

}

