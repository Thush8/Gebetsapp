package com.example.gebetsapp.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gebetsapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static java.lang.Thread.sleep;

public class HomeFragment extends Fragment {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private TextView user_location, datetime;
    private TextView[] time;
    private double latitude = 0, longitude = 0;

    //Googles API for location services
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(getContext(), "This app requires the permission to be granted to work properly",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        user_location = root.findViewById(R.id.user_location);
        datetime = root.findViewById(R.id.date);
        time = new TextView[6];
        time[0] = root.findViewById(R.id.TextViewTime);
        time[1] = root.findViewById(R.id.TextViewTime2);
        time[2] = root.findViewById(R.id.TextViewTime3);
        time[3] = root.findViewById(R.id.TextViewTime4);
        time[4] = root.findViewById(R.id.TextViewTime5);
        time[5] = root.findViewById(R.id.TextViewTime6);

        mySwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateGPS();
                    }
                }
        );

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*300);
        locationRequest.setFastestInterval(1000*5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        updateGPS();

        return root;
    }

    private void updateGPS() {
        mySwipeRefreshLayout.setRefreshing(true);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            // user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions. Put the values of location. XXX into the UI components.
                    System.out.println("Got Location");
                    updateUIValues(location);
                    if (fetchLocation()) {
                        fetchTimes();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            // permissions not granted
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(@NotNull Location location) {
        try {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
    }


    public boolean fetchLocation() {

            Geocoder coder = new Geocoder(getContext());
            List<Address> address;
            try {
                address = coder.getFromLocation(latitude, longitude, 1);
                if (address == null) {
                    return false;
                }
                Address street = address.get(0);
                user_location.setText(street.getLocality());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return latitude + longitude != 0;
    }


    private void fetchTimes(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String month = currentDate.substring(3,5);
        String year = currentDate.substring(6);
        datetime.setText(currentDate + " " + currentTime);
        //Koordinaten Essen Altendorf
        //latitude = 51.458184;
        //longitude = 6.998448;
        //month = 4, year = 2017;
        String url = "https://api.aladhan.com/v1/calendar?latitude="+ latitude +"&longitude="+ longitude +
                "&method=2&month="+ month +"&year="+ year +""+"/";
        System.out.println(url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int day = response.indexOf(currentDate);
                        int time0 = response.lastIndexOf("Fajr",day) + 7;
                        int time1 = response.lastIndexOf("Sunrise",day) + 10;
                        int time2 = response.lastIndexOf("Dhuhr",day) + 8;
                        int time3 = response.lastIndexOf("Asr",day) + 6;
                        int time4 = response.lastIndexOf("Maghrib",day) + 10;
                        int time5 = response.lastIndexOf("Isha",day) + 7;

                        time[0].setText(response.substring(time0,time0 + 5));

                        time[1].setText(response.substring(time1,time1 + 5));

                        time[2].setText(response.substring(time2,time2 + 5));

                        time[3].setText(response.substring(time3,time3 + 5));

                        time[4].setText(response.substring(time4,time4 + 5));

                        time[5].setText(response.substring(time5,time5 + 5));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                time[0].setText("That didn't work!");
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



}