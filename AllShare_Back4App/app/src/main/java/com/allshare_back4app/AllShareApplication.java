package com.allshare_back4app;

import android.*;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.allshare_back4app.Model.Requests;
import com.onesignal.OneSignal;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONObject;

import tgio.parselivequery.BaseQuery;
import tgio.parselivequery.LiveQueryClient;
import tgio.parselivequery.LiveQueryEvent;
import tgio.parselivequery.Subscription;
import tgio.parselivequery.interfaces.OnListener;

/**
 * Created by mkrao on 11/20/2016.
 */

public class AllShareApplication extends Application {

    private static AllShareApplication instance = new AllShareApplication();
    public static Context getContext(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

         ParseObject.registerSubclass(Requests.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iHC7u9QFrvp51WsWHwOCCNaG8ASnJloDWjKXn20l")
                .clientKey("UPAjRXifzfU96kZPW5bRbtnnxyi7wyq9LgjV6njj")
                .server("https://parseapi.back4app.com/").build()
        );
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Log.d("Parse","Initialized");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "855353218401");
        installation.saveInBackground();
        OneSignal.startInit(this).init();

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
