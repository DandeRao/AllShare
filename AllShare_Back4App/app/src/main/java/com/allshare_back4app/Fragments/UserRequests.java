package com.allshare_back4app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.allshare_back4app.MainActivity;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRequests extends Fragment {

ListView userRequests;
    TextView requestsTypeLabel;
    String fetching;
    int showing ;
    public UserRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_requests, container, false);
showing = ((MainActivity) getActivity()).getShowing();
        requestsTypeLabel = (TextView) view.findViewById(R.id.request_type_label);
        userRequests = (ListView) view.findViewById(R.id.userRequests);
        if(showing==1){
            requestsTypeLabel.setText("Requests Accepted");
            fetching = "acceptedBy";
        }else{
            requestsTypeLabel.setText("Requests Posted");
            fetching = "RequestedBy";
        }

        ParseQuery<Requests> query = new ParseQuery<Requests>("Requests");
        query.whereEqualTo(fetching, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Requests>(){
            @Override
            public void done(List<Requests> list, ParseException e) {
                ArrayList<Request> lor = new ArrayList<Request>();
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
                    lor.add(nr);
                }
                ArrayAdapter adapter = new UserProfilePostedAdapter(getContext(),R.layout.user_request_adapter,R.id.row_request_item,lor);
                userRequests.setAdapter(adapter);
                /*ArrayAdapter adapter = new ArrayAdapter<Requests>( getContext(),android.R.layout.simple_list_item_1,list);
                userRequests.setAdapter(adapter);*/
            }
        });


        return view;
    }

}
