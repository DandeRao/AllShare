package com.allshare_back4app.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allshare_back4app.Model.Request;
import com.allshare_back4app.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mkrao on 12/13/2016.
 */

public class RequestAdapter extends ArrayAdapter<Request> {

    private LayoutInflater mInflater;
    List<Request> requests;
    public RequestAdapter(Context context, int resource, int textViewResourceId, List<Request> objects) {
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

        text1.setText(requests.get(position).getItem());
        text2.setText(requests.get(position).getRequestedBy());
        text3.setText(requests.get(position).getNeededBy());
        return view;
    }


}
