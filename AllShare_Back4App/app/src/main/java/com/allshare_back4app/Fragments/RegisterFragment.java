package com.allshare_back4app.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.allshare_back4app.MainActivity;
import com.allshare_back4app.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * Register Fragment to handle register functionality
 * Extends Fragment
 */
public class RegisterFragment extends Fragment {

Button register;
    ProgressDialog progressDialog;
    EditText userName;
    EditText password;
    EditText email;

    /**
     * No Arg Constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * On Create View used to handle all tasks in this fragment
     * @param inflater inflater to inflate the layout
     * @param container the view group
     * @param savedInstanceState the bundle with previous state
     * @return View created in the createView
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        progressDialog = new ProgressDialog(this.getContext());
        userName = (EditText) view.findViewById(R.id.userName);
        password = (EditText) view.findViewById(R.id.password);
        register = (Button) view.findViewById(R.id.register);
        email = (EditText) view.findViewById(R.id.email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Registering");
                progressDialog.show();
                // Register Step in differetn thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Calling parse Register method
                            parseRegister();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return view;
    }

    /**
     * Method to contact parse servers for signing up
     */
    void parseRegister(){
        ParseUser user = new ParseUser();
        user.setUsername(userName.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    progressDialog.dismiss();
                  /*  t_username.setText(ParseUser.getCurrentUser().getUsername());*/
                   // User signed up by now, now saving the user by linking the device and account
                    saveNewUser();
                } else {
                    progressDialog.dismiss();
                    alertDisplayer("Register Fail", e.getMessage());
                }
            }
        });
    }

    /**
     * Method to save user's session on device
     */
    void saveNewUser(){
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(userName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                alertDisplayer("Registration Successful Welcome", "User:" + userName.getText().toString() + "\nEmail:" + email.getText().toString());
                ((MainActivity) getActivity()).replaceFragment(new RequestsList(),false);
            }
        });

    }

    /**
     * Alert displayer method to reuse code for displaying the alerts
     * @param title title of the alert
     * @param message message to be displayed on the alert
     */
    void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }


}
