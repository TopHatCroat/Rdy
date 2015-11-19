package com.tophatcroat.rdy;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAdressTask extends AsyncTask<Location, Void, String> {
    Context context;

    public GetAdressTask(Context context){
        super();
        this.context = context;
    }

    protected void onPostExecute(String adress){
        //TODO: call method on mainActivity
    }

    @Override
    protected String doInBackground(Location... params) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        Location loc = params[0];
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e1){
            Log.e("Location", "getFromLocation");
            return("IO ERROR: getFromLocation");
        }

        catch (IllegalArgumentException e2){
            Log.e("Location", "IllegalArguments");
            return("IO ERROR: IllegalArguments");
        }

        if(addresses != null && addresses.size() > 0){
            Address address = addresses.get(0);
            String addressText = "";
            if (address.getMaxAddressLineIndex() > 0) addressText = address.getAddressLine(0);
            addressText += ", " + address.getLocality() + ", " + address.getCountryName();
            return addressText;
        } else {
            return "Not Found";
        }
    }

}
