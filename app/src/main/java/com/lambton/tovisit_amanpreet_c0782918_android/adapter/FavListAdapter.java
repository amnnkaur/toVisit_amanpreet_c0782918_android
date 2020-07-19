package com.lambton.tovisit_amanpreet_c0782918_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.activity.FavouriteListActivity;
import com.lambton.tovisit_amanpreet_c0782918_android.activity.MainActivity;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ListViewHolder> {

    private static final String TAG = "PlaceAdapter";
    Context context;
    int layoutRes;
    List<FavPlace> favPlaceList;

    FavPlaceRoomDB favPlaceRoomDB;
    MainActivity mainActivity;

    ListViewHolder listViewHolder;

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

        listViewHolder = new FavListAdapter.ListViewHolder(view);

        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {

        final FavPlace favPlace = favPlaceList.get(position);
        holder.address.setText(favPlace.getAddress());
        holder.latitude.setText(String.valueOf(favPlace.getLatitude()));
        holder.longitude.setText(String.valueOf(favPlace.getLongitude()));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        String addDate = simpleDateFormat.format(calendar.getTime());

        holder.date.setText(addDate);


    }

    @Override
    public int getItemCount() {
        return favPlaceList.size();
    }

    public void loadPlaces() {

        favPlaceList= favPlaceRoomDB.favPlaceDao().getAllPlaces();
        notifyDataSetChanged();

    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(final ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            final int position = listViewHolder.getAdapterPosition();

            contextMenu.add("Edit Location").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {


                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("placeID", favPlaceList.get(position).getPlaceID());
                    context.startActivity(intent);

//                    Toast.makeText(context, "On click", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            contextMenu.add("Mark as Visited").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {


                    return true;
                }
            });

        }
    }

}
