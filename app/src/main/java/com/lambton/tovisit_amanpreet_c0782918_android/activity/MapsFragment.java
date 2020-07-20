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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.android.gms.maps.model.CameraPosition;
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
    FavPlace favPlace;

    LatLng addedLocation;
    int placeID;
    String placeName;
    List<FavPlace> favPlaceList;
    GestureDetector gestureDetector;

    public static boolean flagEdit = false;

    public GoogleMap getmMap() {
        return mMap;
    }

    public LatLng getDestLocation() {
        return destLocation;
    }

    LatLng destLocation;
    Location destination;
    static double dest_lat, dest_lng;
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

//            placeID = getActivity().getIntent().getIntExtra("placeID", 0);
            favPlaceList = new ArrayList<>();

            favPlace = (FavPlace) getActivity().getIntent().getSerializableExtra("favPlace");

            // Room database
            favPlaceRoomDB = FavPlaceRoomDB.getINSTANCE(getActivity());
            favPlaceList = favPlaceRoomDB.favPlaceDao().getAllPlaces();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap = googleMap;
            mMap.setOnMarkerDragListener(MapsFragment.this);
            mMap.setOnMarkerClickListener(MapsFragment.this);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            if (flagEdit){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(dest_lat,dest_lng))
                        .title("favPlaceCall.get(0).getAddress()")
                        .snippet("favPlaceCall.get(0).getDate()")
                        .draggable(true);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dest_lat,dest_lng),15));
            }

                for (FavPlace places : favPlaceList) {
                    if (places.getPlaceID() == placeID) {

                        LatLng latLng = new LatLng(places.getLatitude(), places.getLongitude());
                        addedLocation = latLng;
                        setMarker(addedLocation);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                }

            // add long press gesture on map
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    destLocation = latLng;
                    destination = new Location("Your destination");
                    destination.setLatitude(latLng.latitude);
                    destination.setLongitude(latLng.longitude);
                    setMarker(destLocation);
                }
            });

        }
    };

    private void addToFavPlace(Marker marker){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        String addDate = simpleDateFormat.format(calendar.getTime());

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);

            if (!addresses.isEmpty()) {

                String address = addresses.get(0).getAddressLine(0);

                FavPlace favPlace = new FavPlace(marker.getPosition().latitude, marker.getPosition().longitude, addDate, address,false);
                favPlaceRoomDB.favPlaceDao().insertPlace(favPlace);

            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void setMarker(Location destination) {
        LatLng destLatLng = new LatLng(destination.getLatitude(), destination.getLongitude());
        setMarker(destLatLng);
    }

    public void setMarker(LatLng location) {

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
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target( latLng )
                    .zoom( 13 )
                    .bearing( 0 ).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void setEditMarker(LatLng editlatlng){
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

            if (mMap != null) {
                LatLng latLng = new LatLng(editlatlng.latitude, editlatlng.longitude);
//                CameraPosition cameraPosition = CameraPosition.builder()
//                        .target(latLng)
//                        .zoom(13)
//                        .bearing(0).build();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("favPlaceCall.get(0).getAddress()")
                        .snippet("favPlaceCall.get(0).getDate()")
                        .draggable(true);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
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

    private String locationName(Marker marker) {

        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        String address = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                if (addresses.get(0).getSubThoroughfare() != null)
                    address += addresses.get(0).getSubThoroughfare() + ", ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare() + ", ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + ", ";
                if (addresses.get(0).getAdminArea() != null)
                    address += addresses.get(0).getAdminArea();
                if (addresses.get(0).getPostalCode() != null)
                    address += "\n" + addresses.get(0).getPostalCode();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        mMap.clear();

        MarkerOptions markerOptions = new MarkerOptions().position(marker.getPosition())
                .title("Your dragged location").draggable(true);
        mMap.addMarker(markerOptions);

        addedLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        placeName = locationName(marker);

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
    public boolean onMarkerClick(final Marker marker) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to add the place in Favourites");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onMarkerClick = true;
                addToFavPlace(marker);
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

        return false;
    }

}
















