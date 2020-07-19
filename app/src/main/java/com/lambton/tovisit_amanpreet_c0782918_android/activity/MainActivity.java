package com.lambton.tovisit_amanpreet_c0782918_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lambton.tovisit_amanpreet_c0782918_android.IPassData;
import com.lambton.tovisit_amanpreet_c0782918_android.R;
import com.lambton.tovisit_amanpreet_c0782918_android.database.FavPlaceRoomDB;
import com.lambton.tovisit_amanpreet_c0782918_android.volley.GetByVolley;
import com.lambton.tovisit_amanpreet_c0782918_android.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static final int RADIUS = 1500;

    // use the fused location provider client
    private FusedLocationProviderClient mClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private LatLng userLocation;
    FavPlaceRoomDB favPlaceRoomDB;

    FragmentManager fragmentManager;
    MapsFragment fragment;

    FloatingActionButton fab;
    ChipGroup chipGroupType;
    Chip chipCafe, chipMuseums, chipRestaurants, chipHospital, chipSchool;
    Spinner mapType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();
        fragment = new MapsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.myContainer, fragment)
                .commit();

        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();

        optionsMapType();
        typesOfPlaces();
        goToDirection();

    }


    private void goToDirection() {

        fab = findViewById(R.id.fabDirections);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getDestination(new IPassData() {
                    @Override
                    public void destinationSelected(final Location location, final GoogleMap map) {

                        if (location == null) {

                        } else {

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                    getDirectionUrl(latLng), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    GetByVolley.getDirection(response, map, location);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                        }
                    }
                });
            }
        });
    }

    private void typesOfPlaces() {

        chipGroupType = findViewById(R.id.chipGroupType);
        chipCafe = findViewById(R.id.chipCafes);
        chipMuseums = findViewById(R.id.chipMuseums);
        chipRestaurants = findViewById(R.id.chipRestaurants);
        chipSchool = findViewById(R.id.chipSchool);
        chipHospital = findViewById(R.id.chipHospital);

        chipRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String restaurantUrl = getPlaceUrl(userLocation.latitude, userLocation.longitude, "restaurant");
                showNearbyPlaces(restaurantUrl);
//                Toast.makeText(MainActivity.this, "restaurants", Toast.LENGTH_SHORT).show();
            }
        });

        chipCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cafeUrl = getPlaceUrl(userLocation.latitude, userLocation.longitude, "cafe");
                showNearbyPlaces(cafeUrl);

//                Toast.makeText(MainActivity.this, "cafe", Toast.LENGTH_SHORT).show();

            }
        });

        chipMuseums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = getPlaceUrl(userLocation.latitude, userLocation.longitude, "museum");
                showNearbyPlaces(url);

//                Toast.makeText(MainActivity.this, "museums", Toast.LENGTH_SHORT).show();
            }
        });

        chipHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = getPlaceUrl(userLocation.latitude, userLocation.longitude, "hospital");
                showNearbyPlaces(url);

//                Toast.makeText(MainActivity.this, "hospital", Toast.LENGTH_SHORT).show();
            }
        });

        chipSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = getPlaceUrl(userLocation.latitude, userLocation.longitude, "school");
                showNearbyPlaces(url);

                Toast.makeText(MainActivity.this, "school", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void optionsMapType() {

        mapType = findViewById(R.id.spinnerMapType);

        mapType.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        fragment.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        fragment.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 2:
                        fragment.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 3:
                        fragment.mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));
    }

    private void showNearbyPlaces(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GetByVolley.getNearbyPlaces(response, fragment.getmMap());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private String getPlaceUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append(("&radius="+RADIUS));
        googlePlaceUrl.append("&type="+placeType);
        googlePlaceUrl.append("&key="+getString(R.string.google_maps_key));
        Log.d(TAG, "getDirectionUrl: " + googlePlaceUrl);
        return googlePlaceUrl.toString();
    }

    private String getDirectionUrl(LatLng location) {
        StringBuilder googleDirectionUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionUrl.append("origin="+userLocation.latitude+","+userLocation.longitude);
        googleDirectionUrl.append(("&destination="+location.latitude+","+location.longitude));
        googleDirectionUrl.append("&key="+getString(R.string.google_maps_key));
        Log.d(TAG, "getDirectionUrl: " + googleDirectionUrl);
        return googleDirectionUrl.toString();
    }


    private void startUpdateLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                Log.d(TAG, "onLocationResult: " + location);
                    fragment.setHomeMarker(location);
                }
            }
        };
        mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (fragment.mMap != null)
                {
                    startUpdateLocation();
                }
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//
//
//        startActivity(new Intent(MainActivity.this, FavouriteListActivity.class));
//
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
//        String addedDate = simpleDateFormat.format(cal.getTime());
//
//        favPlaceRoomDB.favPlaceDao().updatePlace(fragment.placeID, fragment.destLocation.latitude, fragment.destLocation.longitude, fragment.placeName);
//        finish();
////                Toast.makeText(MainActivity.this, "info:" +fragment.placeID +fragment.placeName +fragment.destLocation.latitude, Toast.LENGTH_SHORT).show();
//
//
//
//    }

    public void showLaunchNearbyPlaces(LatLng location) {
        String url = getPlaceUrl(location.latitude, location.longitude, "restaurant");
        showNearbyPlaces(url);
    }
}