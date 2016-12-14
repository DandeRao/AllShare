package com.allshare_back4app.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkrao on 12/13/2016.
 */

public class UserProfilePostedAdapter extends ArrayAdapter<Request> {

    private LayoutInflater mInflater;
    ArrayList<Request> requests;
    public UserProfilePostedAdapter(Context context, int resource, int textViewResourceId, ArrayList<Request> objects) {
        super(context, resource, textViewResourceId, objects);
        requests = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(R.id.row_request_item);
        TextView text2 = (TextView) view.findViewById(R.id.row_request_requestor);
        TextView text3 = (TextView) view.findViewById(R.id.row_request_neededBy);
        TextView text4 = (TextView) view.findViewById(R.id.row_request_acceptedOn);

        System.out.println(requests.get(position).getItem());

        text1.setText(requests.get(position).getItem());
        text2.setText("Requested By: "+requests.get(position).getRequestedBy());
        text3.setText("Needed By: "+requests.get(position).getNeededBy());
        text4.setText("Accepted On: "+requests.get(position).getPostedOn());
        return view;
    }

}
