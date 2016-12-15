package com.allshare_back4app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.allshare_back4app.MainActivity;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Requests List Class to show the requests once user logs into the application
 * Extends ActionBArHandler to handle actionBar
 */
public class RequestsList extends ActionBarItemsHandler {
    ListView requestsList;
    double maxDistance;
    SeekBar maxDistanceSeekBar;

    /**
     * No Arg Constructor
     */
    public RequestsList() {
        // Required empty public constructor
    }

    /**
     * onCreateView method, Handles all the element hooks and their definitions
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        maxDistance = ((MainActivity) getActivity()).getMaxDistanceForSeeker();

        View view = inflater.inflate(R.layout.fragment_requests_list, container, false);
        requestsList = (ListView) view.findViewById(R.id.requestsList);
//Using Seekbar to set miles radius
        maxDistanceSeekBar = (SeekBar) view.findViewById(R.id.maxDistance);
        maxDistanceSeekBar.setProgress((int) maxDistance);
        maxDistanceSeekBar.setMax(75);
        System.out.println("Max distance is :"+maxDistance);
//On Seekbar Change listener
    maxDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        maxDistance = (double) progress;
        System.out.println("Max Distance set from slider progress to (Progress):"+progress);
        maxDistance = (maxDistance<=10.0?10.0:maxDistance);
        ((MainActivity) getActivity()).setMaxDistanceForSeeker(maxDistance);
        System.out.println("Max Distance set from slider progress to :"+((MainActivity) getActivity()).getMaxDistanceForSeeker());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getContext(),"Showing requests within "+ (int) maxDistance + " miles.", Toast.LENGTH_SHORT).show();
        refreshRequests();
    }
});
  //FetchRequests from back4app servers
                fetchRequests();
       // Setting clickable listView
        requestsList.setClickable(true);
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // On click get the position, save in mainactiviy to access it in later times.
                ((MainActivity) getActivity()).setPosition(position);
                ((MainActivity) getActivity()).setCurrentPosition(position);

                // Redirect to requestdetails fragment
                ((MainActivity) getActivity()).replaceFragment(new RequestDetails(), true);
            }
        });

        return view;
    }
//Request Refresher (called when refresher is clicked on)
    public void refreshRequests(){

        fetchRequests();
    }

    /**
     * Method to fetch requests from back4app servers and add it to list in manactivity to access in other fragments
     */
    public void fetchRequests(){

    ParseGeoPoint parsePoint = new ParseGeoPoint(((MainActivity) getActivity()).getCurrentLocation().getLatitude(),((MainActivity) getActivity()).getCurrentLocation().getLongitude());
   // Query for fetching the requests
    ParseQuery<Requests> query = new ParseQuery<Requests>("Requests");
    query.whereEqualTo("isAccepted", false);
    query.whereWithinMiles("requestedLocation",parsePoint,maxDistance);

        // Making query to fetch in background
        query.findInBackground(new FindCallback<Requests>() {
        public void done(List<Requests> list, ParseException e) {
            if (e == null) {
                ArrayList<Request> listOfRequests = new ArrayList<Request>();
                for(Requests r:list){

                    Request nr = new Request();
                    nr.setItem(r.getString("item"));
                    nr.setNeededBy(r.getString("neededBy"));
                    nr.setRequestedBy(r.getString("RequestedBy"));
                    nr.setRequestedOn(r.getString("requestedOn"));
                    nr.setPostedOn(r.getString("acceptedOn"));
                    System.out.println("Getting objectIds "+ r.getObjectId());
                    nr.setObjectId(r.getObjectId());
                    listOfRequests.add(nr);
                }
                // Save fetched list to list in Mainactivity so that other fragments can access it
                ((MainActivity) getActivity()).setRequests(listOfRequests);
              // Create adapter to display in custom listView
               ArrayAdapter adapter = new RequestAdapter(getContext(),R.layout.request_adapter,R.id.row_request_item,listOfRequests);
                requestsList.setAdapter(adapter);

            } else {
                // handle Parse Exception here
            }
        }
    });

}

    /**
     * Method to handle the Actionbar items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sign_out:
                // Logout Line
                ParseUser.logOut();
                ((MainActivity) getActivity()).replaceFragment(new LoginFragment(),true);

                return true;

            case R.id.add_request:
                ((MainActivity) getActivity()).replaceFragment(new AddRequest(), true);
                return (true);

            case R.id.refresh_request:
                //    Call Load Request Method
                refreshRequests();
                Toast toast = Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_LONG);
                toast.show();
                return (true);
        }

        return super.onOptionsItemSelected(item);
    }


}
