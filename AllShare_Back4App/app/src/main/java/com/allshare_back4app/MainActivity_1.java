package com.allshare_back4app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.allshare_back4app.Fragments.LoginFragment;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity_1 extends AppCompatActivity {
    List<Request> requests;
    int position;
    int showing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requests = new ArrayList<Request>();
       /* ParseObject.registerSubclass(Requests.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iHC7u9QFrvp51WsWHwOCCNaG8ASnJloDWjKXn20l")
                .clientKey("UPAjRXifzfU96kZPW5bRbtnnxyi7wyq9LgjV6njj")
                .server("https://parseapi.back4app.com/").build()
        );
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "855353218401");
        installation.saveInBackground();
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("Requests");*/
        setContentView(R.layout.activity_main);
        System.out.println("Calling Login Fragment");
        replaceFragment(new LoginFragment(),false);
    }


    public void replaceFragment(Fragment frag, boolean addToBackStack){
        System.out.println("Recahed Replacing function");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentBox, frag);
        if (addToBackStack) {
            ft.addToBackStack(frag.toString());
        }
        ft.commit();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getShowing() {
        return showing;
    }

    public void setShowing(int showing) {
        this.showing = showing;
    }

}
