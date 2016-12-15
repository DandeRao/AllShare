package com.allshare_back4app;

/**
 * Main Activity class
 * Whole application runs on this single activity
 *
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.allshare_back4app.Fragments.LoginFragment;
import com.allshare_back4app.Model.Request;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener{
    List<Request> requests;
    int position;
    int showing;
    int TAG_CODE_PERMISSION_LOCATION;
    Location currentLocation;
    int currentPosition;
    boolean hasLocation = false;
    double maxDistanceForSeeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPosition =1;
        requests = new ArrayList<Request>();
        maxDistanceForSeeker = 10.0;

        // Permission check for accessing location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
            System.out.println("Permission Not Granted for Location services");
        }
// Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
// Getting current Location
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(currentLocation!=null){
            locationManager.removeUpdates(this);
        }

// Inflating MainActivity Layout
        setContentView(R.layout.activity_main);
// Replacing the Fragment holder in MainActivity with Login Fragment
        replaceFragment(new LoginFragment(), false);

    }

    /**
     * Replace Fragment method takes android.support.app.v4.Fragment type to place in the fragment
     * holder, Boolean is used to allow the fragment to access when pressed back button
     * @param frag
     * @param addToBackStack
     */

    public void replaceFragment(Fragment frag, boolean addToBackStack){
        System.out.println("Recahed Replacing function");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentBox, frag);
        if (addToBackStack) {
            ft.addToBackStack(frag.toString());
        }
        ft.commit();
    }

    /**
     * Getter for MaxDistanceSeeker, Used to set default seeker progress in RequestsFragment
     * @return maxDistanceForSeeker
     */
    public Double getMaxDistanceForSeeker() {
        return maxDistanceForSeeker;
    }

    /**
     * maxDistance Setter, called when the seekbar is altered
     * @param maxDistance
     */
    public void setMaxDistanceForSeeker(Double maxDistance) {
        this.maxDistanceForSeeker = maxDistance;

    }

    /**
     * Getter method to access current element in the request list's ListView
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Setter method for current element in the request list's ListView
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Getter Method to access requests as List
     * @return
     */
    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Setter Method to access requests, set when ever FetchRequests is Called in RequestsList Fragment
     * @param requests
     */
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    /**
     * Setter method for position
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * showing is used to determine what option user selected i.e
     * 0 - requestes posted by user
     * 1- requests accepted by user
     * @return
     */
    public int getShowing() {
        return showing;
    }

    /**
     * setShowing will be called gets the value in showing variable.
     * @param showing
     */
    public void setShowing(int showing) {
        this.showing = showing;
    }

    /**
     * Getter method for accessing the current location of user,
     * set once when the app is first opened
     * @return Location of the user (or mobile apparently)
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Getter method for mutating the current location of user,
     * called once when the app is first opened
     * @return Location of the user (or mobile apparently)
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Inherited from Location listner interface
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        hasLocation = true;
    }

    /**
     * Inherited from Location Listener
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Inherited from Location Listener interface
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Inherited from LocationListener
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {

    }


}

