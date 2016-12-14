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


public class RequestsList extends ActionBarItemsHandler {
    ListView requestsList;
    public double lattitide;
    public double longitude;
    double maxDistance;
    Spinner maxDistanceSpinner;
    SeekBar maxDistanceSeekBar;
    EditText maxDistanceET;
    int TAG_CODE_PERMISSION_LOCATION;

    public RequestsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //   System.out.println("called 1");
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        maxDistance = ((MainActivity) getActivity()).getMaxDistanceForSeeker();

        View view = inflater.inflate(R.layout.fragment_requests_list, container, false);
        requestsList = (ListView) view.findViewById(R.id.requestsList);
        //maxDistanceSpinner = (Spinner) view.findViewById(R.id.maxDistance);
        maxDistanceSeekBar = (SeekBar) view.findViewById(R.id.maxDistance);
        maxDistanceSeekBar.setProgress((int) maxDistance);
        maxDistanceSeekBar.setMax(75);
        System.out.println("Max distance is :"+maxDistance);

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
     /*  *//* ArrayList<String> list = new ArrayList<String>();
        list.add(10+"");
        list.add(25+"");
        list.add(50+"");
        list.add(75+"");*//*
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       *//* maxDistanceSpinner.setAdapter(dataAdapter);
        maxDistanceSpinner.setOnItemSelectedListener(this);*/

                fetchRequests();
        //List<Requests> list = fetchRequests();
        // showRequests((ArrayList<Requests>) list);
        requestsList.setClickable(true);
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).setPosition(position);
                ((MainActivity) getActivity()).setCurrentPosition(position);
                ((MainActivity) getActivity()).replaceFragment(new RequestDetails(), true);
            }
        });




        return view;
    }

    public void refreshRequests(){

        fetchRequests();
    }

public void fetchRequests(){
    //System.out.println("Called 2");

    ParseGeoPoint parsePoint = new ParseGeoPoint(((MainActivity) getActivity()).getCurrentLocation().getLatitude(),((MainActivity) getActivity()).getCurrentLocation().getLongitude());
    ParseQuery<Requests> query = new ParseQuery<Requests>("Requests");
    query.whereEqualTo("isAccepted", false);
    query.whereWithinMiles("requestedLocation",parsePoint,maxDistance);
    query.findInBackground(new FindCallback<Requests>() {
        public void done(List<Requests> list, ParseException e) {
            if (e == null) {

             //   System.out.println("got from server"+list.size());
             //   System.out.println(Arrays.toString(list.toArray()));
                //    ((MainActivity) getActivity()).setRequests(list);
                ArrayList<Request> listOfRequests = new ArrayList<Request>();
                for(Requests r:list){

                //    System.out.println(r.getString("item"));
                    Request nr = new Request();

                    nr.setItem(r.getString("item"));
                    nr.setNeededBy(r.getString("neededBy"));
                    nr.setRequestedBy(r.getString("RequestedBy"));
                    nr.setRequestedOn(r.getString("requestedOn"));
                    nr.setPostedOn(r.getString("acceptedOn"));
                    System.out.println("Getting objectIds "+ r.getObjectId());
                    nr.setObjectId(r.getObjectId());

                 //   System.out.println(nr.toString());
                    listOfRequests.add(nr);
                }
                ((MainActivity) getActivity()).setRequests(listOfRequests);
              //  ArrayAdapter adapter = new ArrayAdapter<Request>( getContext(),android.R.layout.simple_list_item_1,listOfRequests);
               ArrayAdapter adapter = new RequestAdapter(getContext(),R.layout.request_adapter,R.id.row_request_item,listOfRequests);
                requestsList.setAdapter(adapter);

            } else {
                // handle Parse Exception here
            }
        }
    });

}


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
