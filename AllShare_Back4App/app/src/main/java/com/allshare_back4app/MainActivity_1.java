package com.allshare_back4app;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.allshare_back4app.Fragments.LoginFragment;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity_1 extends AppCompatActivity implements LocationListener{
    List<Request> requests;
    int position;
    int showing;
    int TAG_CODE_PERMISSION_LOCATION;
    Location currentLocation;

    boolean hasLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requests = new ArrayList<Request>();


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
            System.out.println("Permission Not Granted for Location services");
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
if(currentLocation!=null){
    locationManager.removeUpdates(this);
}
        setContentView(R.layout.activity_main);
        System.out.println("Calling Login Fragment");
        replaceFragment(new LoginFragment(), false);


       // new FetchLocation().execute();
       // locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);

       /* new AsyncTask<>() {
            private final ProgressDialog dialog = new ProgressDialog(MainActivity_1.this);


            @Override
            protected Object doInBackground(Object[] params) {
                //Wait 10 seconds to see if we can get a location from either network or GPS, otherwise stop
                Long t = Calendar.getInstance().getTimeInMillis();
                while (!hasLocation && Calendar.getInstance().getTimeInMillis() - t < 15000) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                return null;
            }

            protected void onPreExecute()
            {
                this.dialog.setMessage("Searching");
                this.dialog.show();
            }


            protected void onPostExecute(final Void unused)
            {
                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }

                if (currentLocation != null)
                {
                    //useLocation();

                }
                else
                {
                    //Couldn't find location, do something like show an alert dialog
                }
            }
        }.execute();*/

       /* new Runnable() {
            @Override
            public void run() {
                if (hasLocation){

                else{
                    try {
                          Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };*/




    }



    public void replaceFragment(Fragment frag, boolean addToBackStack){
        System.out.println("Recahed Replacing function");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentBox, frag);
        if (addToBackStack) {
            ft.addToBackStack(frag.toString());
        }
        ft.commit();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getShowing() {
        return showing;
    }

    public void setShowing(int showing) {
        this.showing = showing;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        hasLocation = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

   private class FetchLocation extends AsyncTask<Void,Void,Void>{

        private ProgressDialog dialog = new ProgressDialog(MainActivity_1.this);



        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(hasLocation){
                setContentView(R.layout.activity_main);
                System.out.println("Calling Login Fragment");
                replaceFragment(new LoginFragment(), false);
            }
            else{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Inserting data...");
            this.dialog.show();
        }
    }
}

