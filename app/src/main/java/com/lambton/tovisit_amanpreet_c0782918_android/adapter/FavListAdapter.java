package com.lambton.tovisit_amanpreet_c0782918_android.adapter;

import android.content.Context;
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

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ListViewHolder> {

    private static final String TAG = "PlaceAdapter";
    Context context;
    int layoutRes;
    List<FavPlace> favPlaceList;

    FavPlaceRoomDB favPlaceRoomDB;
    MainActivity mainActivity;

    public FavListAdapter(Context context, int resource, List<FavPlace> favPlaces) {

        this.favPlaceList = favPlaces;
        this.layoutRes = resource;
        this.context = context;

        favPlaceRoomDB = favPlaceRoomDB.getINSTANCE(context);

    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, null);

        ListViewHolder listViewHolder = new FavListAdapter.ListViewHolder(view);

        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        final FavPlace favPlace = favPlaceList.get(position);
        holder.address.setText(favPlace.getAddress());
        holder.latitude.setText(favPlace.getLatitude());
        holder.longitude.setText(favPlace.getLongitude());
        holder.date.setText(favPlace.getDate());

    }

    @Override
    public int getItemCount() {
        return favPlaceList.size();
    }

    private void loadPlaces() {
        favPlaceList= favPlaceRoomDB.favPlaceDao().getAllPlaces();
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
