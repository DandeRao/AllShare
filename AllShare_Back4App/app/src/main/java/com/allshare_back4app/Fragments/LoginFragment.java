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

import com.allshare_back4app.MainActivity_1;
import com.allshare_back4app.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginFragment extends Fragment {

    Button login;
    Button register;
    EditText userName;
    EditText password;
    ProgressDialog progressDialog;
    ParseUser currentUser;
    public LoginFragment() {
        // Required empty public constructor
    }


     @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        System.out.println("Login Fragment Called");
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        progressDialog = new ProgressDialog(this.getContext());
        userName = (EditText) view.findViewById(R.id.userName);
        password = (EditText) view.findViewById(R.id.password);
        login = (Button) view.findViewById(R.id.login);

         currentUser = ParseUser.getCurrentUser();
         if (currentUser != null) {
             progressDialog.setMessage("Please Wait");
             progressDialog.setTitle("Logging in");
             progressDialog.show();
             progressDialog.dismiss();
             System.out.println("Current User "+currentUser.getUsername());
             alertDisplayer("Welcome Back", "User:" + currentUser.getUsername() +" Login.Email:"+currentUser.getEmail());
             ((MainActivity_1) getActivity()).replaceFragment(new RequestsList(),true);

         } else {
             // show the signup or login screen
         }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Logging in");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseLogin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        register = (Button) view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_1) getActivity()).replaceFragment(new RegisterFragment(),true);
            }
        });
        return view;
    }

    void parseLogin(){
        ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                // Passed login step, Load the requestsList fragment
                if (parseUser != null) {
                    progressDialog.dismiss();
                    getUserDetailFromParse();
                    ((MainActivity_1) getActivity()).replaceFragment(new RequestsList(),true);

                } else {
                    progressDialog.dismiss();
                    alertDisplayer("Login Fail", e.getMessage()+" Please re-try");
                }
            }
        });
    }



    void getUserDetailFromParse(){
        ParseUser user = ParseUser.getCurrentUser();

     /*   t_username.setText(user.getUsername());
        t_email.setText(user.getEmail());*/
        alertDisplayer("Welcome Back", "User:" + user.getUsername() +" Login.Email:"+user.getEmail());

    }
    void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity_1) getActivity())
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
