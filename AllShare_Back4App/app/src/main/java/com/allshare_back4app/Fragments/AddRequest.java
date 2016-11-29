package com.allshare_back4app.Fragments;



import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.allshare_back4app.MainActivity_1;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRequest extends ActionBarItemsHandler {

    TextView neededBy;
    TextView item;
    Button addRequest;

    Date value =new Date();
    Calendar cal = Calendar.getInstance();
    ProgressDialog progressDialog;
    String neededByString;
    public AddRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_add_request, container, false);
        item = (TextView) view.findViewById(R.id.item);
        neededBy = (TextView) view.findViewById(R.id.needed_by);
        addRequest = (Button) view.findViewById(R.id.add_request);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cal.setTime(value);
        progressDialog = new ProgressDialog(getActivity());
        neededBy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("Called on FocusChange");
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        neededByString = month+"/"+dayOfMonth+"/"+year;
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                neededByString = neededByString + " "+ String.format("%02d",hourOfDay)+":"+String.format("%02d",minute);
                                neededBy.setText(neededByString);
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE), true).show();
                    }


                },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }


        });

        addRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Adding the request to pool...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addRequest();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                progressDialog.dismiss();
                ((MainActivity_1) getActivity()).replaceFragment(new RequestsList(),false);
            }
        });
    }

public void addRequest(){

new AsyncTask <String, Integer, String>() {
    @Override
    protected String doInBackground(String... params) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date dateobj = new Date();
        Requests request = new Requests();
        request.put("item",params[1]);
        request.put("RequestedBy", ParseUser.getCurrentUser().getUsername());
        request.put("neededBy",params[0]);
        request.put("requestedOn",df.format(dateobj));
        request.put("isAccepted",false);
        request.put("requestorEmail",ParseUser.getCurrentUser().getEmail());
        request.saveInBackground();
               return null;
    }
}.execute(neededBy.getText().toString(),item.getText().toString());

}


    public void sendPushNotifications(){

        ParsePush push = new ParsePush();
        push.setChannel("Requests");
        push.setMessage("User "+ ParseUser.getCurrentUser().getUsername()+" needs "+ item.getText().toString());
        push.sendInBackground();


    }
}
//First DatePicker Code
/*
        final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
                        String date = datePicker.getMonth()+"/"+datePicker.getDayOfMonth()+datePicker.getYear()+" "+timePicker.getHour()+":"+timePicker.getMinute();
                        neededBy.setText(date);
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();

*/

//Second DatePicker Code

/*
cal.setTime(value);
        new DatePickerDialog(getActivity(),
        new DatePickerDialog.OnDateSetListener() {
        boolean fired = false;
@Override public void onDateSet(DatePicker view,
        int y, int m, int d) {
        if (fired) {}else{
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m);
        cal.set(Calendar.DAY_OF_MONTH, d);

        // now show the time picker
        new TimePickerDialog(getActivity(),
        new TimePickerDialog.OnTimeSetListener() {
@Override
public void onTimeSet(TimePicker view,
        int h, int min) {
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, min);
        value = cal.getTime();
        }
        }, cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE), true).show();
        neededByString = cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        System.out.println("Setting Text");
        neededBy.setText(neededByString);
        fired=true;
        }
        }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)).show();


        }
*/