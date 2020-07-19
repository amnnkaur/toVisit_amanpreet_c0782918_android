package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lambton.tovisit_amanpreet_c0782918_android.IPassData;
import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlace;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.sax.TemplatesHandler;

import static android.app.Activity.RESULT_OK;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsFragment";

    FusedLocationProviderClient mClient;

    GoogleMap mMap;
    FavPlaceRoomDB favPlaceRoomDB;

    LatLng addedLocation;
    int placeID;
    String placeName;
    List<FavPlace> favPlaceList;

    public GoogleMap getmMap() {
        return mMap;
    }

    public LatLng getDestLocation() {
        return destLocation;
    }

    LatLng destLocation;
    Location destination;
    Double dest_lat, dest_lng;
    boolean onMarkerClick = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(final GoogleMap googleMap) {

            // Room database
            favPlaceRoomDB = FavPlaceRoomDB.getINSTANCE(getActivity());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap = googleMap;
            mMap.setOnMarkerDragListener(MapsFragment.this);
            mMap.setOnMarkerClickListener(MapsFragment.this);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);


            // add long press gesture on map
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    destLocation = latLng;
                    destination = new Location("Your destination");
                    destination.setLatitude(latLng.latitude);
                    destination.setLongitude(latLng.longitude);

                    dest_lat = latLng.latitude;
                    dest_lng = latLng.longitude;

                    addedLocation = latLng;


                    setMarker(latLng);
                    addToFavPlace(addedLocation);
                }
            });

        }
    };

    private void addToFavPlace(LatLng latLng){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        String addDate = simpleDateFormat.format(calendar.getTime());

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if(!addresses.isEmpty()){

                String address = addresses.get(0).getAddressLine(0);

//                Toast.makeText(getActivity(), "Location: " +addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                // insert into room db
//                Toast.makeText(getActivity(), "lat is : " +addedLocation.latitude +","+addedLocation.longitude, Toast.LENGTH_SHORT).show();

//                Toast.makeText(getActivity(), "lat is : " +address, Toast.LENGTH_SHORT).show();

                if (onMarkerClick){

                    onMarkerClick = false;
                }else {

                    FavPlace favPlace = new FavPlace(addedLocation.latitude,addedLocation.longitude,addDate,address);
                    favPlaceRoomDB.favPlaceDao().insertPlace(favPlace);

//                    Toast.makeText(getActivity(), "places NOT FOUND:", Toast.LENGTH_SHORT).show();
                }
            }


        }catch (IOException e){
            e.printStackTrace();

        }

    }

    private void setMarker(Location destination) {
        LatLng destLatLng = new LatLng(destination.getLatitude(), destination.getLongitude());
        setMarker(destLatLng);
    }

    private void setMarker(LatLng location) {

        MarkerOptions options = new MarkerOptions().position(location)
                .title("Your Destination")
                .snippet("You are going there")
                .draggable(true);
        mMap.addMarker(options);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(latLng));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                    ((MainActivity) getActivity()).showLaunchNearbyPlaces(latLng);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setHomeMarker(Location location) {

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        if(mMap!=null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }


    public void getDestination(IPassData callback) {
        callback.destinationSelected(destination, mMap);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        destination.setLatitude(marker.getPosition().latitude);
        destination.setLongitude(marker.getPosition().longitude);

        Log.d(TAG, "onMarkerDragEnd: " + destination.getLatitude());

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(destination.getLatitude(), destination.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                String address = "";
                if (addresses.get(0).getAdminArea() != null)
                    address += addresses.get(0).getAdminArea() + " ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + " ";
                if (addresses.get(0).getPostalCode() != null)
                    address += addresses.get(0).getPostalCode() + " ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare();
                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                favPlaceList = favPlaceRoomDB.favPlaceDao().getAllPlaces();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to add the place in Favourites");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onMarkerClick = true;

                addToFavPlace(addedLocation);

//                            Intent intent =  new Intent(getActivity(),FavouriteListActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();

                startActivityForResult(new Intent(getActivity(),FavouriteListActivity.class),1);

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onMarkerClick = false;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

//                    Toast.makeText(getActivity(), "marker click", Toast.LENGTH_SHORT).show();
        return false;
    }
}
















