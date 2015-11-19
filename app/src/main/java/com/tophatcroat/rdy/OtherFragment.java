package com.tophatcroat.rdy;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class OtherFragment extends Fragment implements LocationListener{
    Button button;
    TextView textView;
    LocationManager locationManager;
    String provider;
    Location location;
    List<Address> address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //some fucking hack
        LayoutInflater lf = getActivity().getLayoutInflater();
        ViewGroup viewGroup = (ViewGroup) lf.inflate(R.layout.other_fragment, container, false);

        button = (Button) viewGroup.findViewById(R.id.button_other_fragment);
        textView = (TextView) viewGroup.findViewById(R.id.text_view_other_fragment);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        try {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                onLocationChanged(location);
            } else {
                textView.setText("fuck you, no location");
            }
        } catch (SecurityException e){
            Log.i("Security Permission", "not granted");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        } catch (SecurityException e){
            Log.i("Security Permission", "not granted");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        } catch (SecurityException e){
            Log.i("Security Permission", "not granted");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e1){
            Log.e("Location", "getFromLocation");
        }
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        textView.setText(String.valueOf(lat) + ", " + String.valueOf(lng) + " - " + address.get(0).getLocality());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

}


