package com.allshare_back4app;

import android.app.Application;
import android.util.Log;

import com.allshare_back4app.Model.Requests;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by mkrao on 11/20/2016.
 */

public class AllShareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         ParseObject.registerSubclass(Requests.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iHC7u9QFrvp51WsWHwOCCNaG8ASnJloDWjKXn20l")
                .clientKey("UPAjRXifzfU96kZPW5bRbtnnxyi7wyq9LgjV6njj")
                .server("https://parseapi.back4app.com/").build()
        );
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "855353218401");
        installation.saveInBackground();
        //ParseInstallation.getCurrentInstallation().saveInBackground();
       // ParsePush.subscribeInBackground("Requests");
        ParsePush.subscribeInBackground("Requests", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                    Log.d("Parse","Success");
                else
                    Log.d("Parse","Failed");
            }
        });
    }
}
