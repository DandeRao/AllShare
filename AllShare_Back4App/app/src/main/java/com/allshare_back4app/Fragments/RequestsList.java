package com.allshare_back4app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.allshare_back4app.MainActivity_1;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class RequestsList extends ActionBarItemsHandler {
    ListView requestsList;
    public RequestsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     //   System.out.println("called 1");
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view =inflater.inflate(R.layout.fragment_requests_list, container, false);
        requestsList = (ListView) view.findViewById(R.id.requestsList);
        fetchRequests();
       //List<Requests> list = fetchRequests();
       // showRequests((ArrayList<Requests>) list);
        requestsList.setClickable(true);
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity_1) getActivity()).setPosition(position);
                ((MainActivity_1) getActivity()).replaceFragment(new RequestDetails(),true);
            }
        });
        return view;
    }

    public void refreshRequests(){

        fetchRequests();
    }

public void fetchRequests(){
    //System.out.println("Called 2");
    ParseQuery<Requests> query = new ParseQuery<Requests>("Requests");
    query.whereEqualTo("isAccepted", false);
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
                    System.out.println("Getting objectIds "+ r.getObjectId());
                    nr.setObjectId(r.getObjectId());
                 //   System.out.println(nr.toString());
                    listOfRequests.add(nr);
                }
                ((MainActivity_1) getActivity()).setRequests(listOfRequests);
                ArrayAdapter adapter = new ArrayAdapter<Request>( getContext(),android.R.layout.simple_list_item_1,listOfRequests);
                requestsList.setAdapter(adapter);

            } else {
                // handle Parse Exception here
            }
        }
    });

    /*ParseQuery<Requests> query = new ParseQuery("Requests");
    query.setLimit(10);
    try {

        ArrayList<Requests> listOfRequests =  (ArrayList) query.find();
        ArrayList<Request> lr = new ArrayList<>();
        for(Requests r:listOfRequests){
            System.out.println(r.getString("item"));
            Request nr = new Request();
            nr.setItem(r.getString("item"));
            nr.setNeededBy(r.getString("neededBy"));
            nr.setRequestedBy(r.getString("requestedBy"));
            nr.setRequestedOn(r.getString("requestedOn"));
            System.out.println(nr.toString());
            lr.add(nr);
        }
        //ArrayList<Requests> listOfRequests =
        ArrayAdapter adapter = new ArrayAdapter<Requests>( getContext(),android.R.layout.simple_list_item_1,listOfRequests);
        requestsList.setAdapter(adapter);
    }catch (Exception e){
        e.printStackTrace();
    }*/
   /* query.find(new FindCallback<Requests>() {
        @Override
        public void done(List<Requests> list, ParseException e) {
            System.out.println("got from server"+list.size());
            System.out.println(Arrays.toString(list.toArray()));
        //    ((MainActivity) getActivity()).setRequests(list);
            ArrayList<Requests> listOfRequests = new ArrayList<Request>();
            for(ParseObject r:list){
                System.out.println(r.getString("item"));
                Request nr = new Request();
                nr.setItem(r.getString("item"));
                nr.setNeededBy(r.getString("neededBy"));
                nr.setRequestedBy(r.getString("requestedBy"));
                nr.setRequestedOn(r.getString("requestedOn"));
                System.out.println(nr.toString());
                listOfRequests.add(nr);
            }

            ArrayAdapter adapter = new ArrayAdapter<Request>( getContext(),android.R.layout.simple_list_item_1,listOfRequests);
            requestsList.setAdapter(adapter);

        }

        })*/;

   // return ((MainActivity) getActivity()).getRequests();
}

    public void showRequests(ArrayList<Requests> req){
      //  System.out.println("Called 3");
    //    System.out.println("got Requests"+req.size());
        ArrayAdapter adapter = new ArrayAdapter<Requests>( getContext(),android.R.layout.simple_list_item_1,req);
        requestsList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sign_out:
                // Logout Line
                ParseUser.logOut();
                ((MainActivity_1) getActivity()).replaceFragment(new LoginFragment(),true);

                return true;

            case R.id.add_request:
                ((MainActivity_1) getActivity()).replaceFragment(new AddRequest(), true);
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
