package com.allshare_back4app.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.allshare_back4app.MainActivity_1;
import com.allshare_back4app.Model.Request;
import com.allshare_back4app.Model.Requests;
import com.allshare_back4app.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetails extends ActionBarItemsHandler {
    EditText item;
    EditText neededBy;
    EditText requestedBy;
    EditText requestedOn;
    Button goBack;
    Button acceptRequest;


    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;


    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    int currentPosition;
    int listEnd ;
       final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    Log.i("On Fling", "onFling has been called!");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i("On Right to Left", "Right to Left");
                            onSwipeLeft();

                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i("On Left to Right", "Left to Right");
                            onSwipeRight();
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });

    public RequestDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_details, container, false);
        setHasOptionsMenu(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        currentPosition  = ((MainActivity_1) getActivity()).getPosition();
        listEnd = ((MainActivity_1) getActivity()).getRequests().size();
        item = (EditText) view.findViewById(R.id.item);
        neededBy = (EditText) view.findViewById(R.id.neededBy);
        requestedBy = (EditText) view.findViewById(R.id.requestedBy);
        requestedOn = (EditText) view.findViewById(R.id.requestedOn);



        goBack = (Button) view.findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_1) getActivity()).replaceFragment(new RequestsList(),true);
            }
        });

        acceptRequest = (Button) view.findViewById(R.id.accept);

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Position is "+ currentPosition);
                System.out.println("Object Id is: "+((MainActivity_1)getActivity()).getRequests().get(currentPosition).getObjectId());
                ParseQuery<Requests> query = ParseQuery.getQuery("Requests");


// Retrieve the object by id
                query.getInBackground(((MainActivity_1)getActivity()).getRequests().get(currentPosition).getObjectId(), new GetCallback<Requests>() {
                    public void done(final Requests currentRequest, ParseException e) {
                        if (e == null) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                            Date dateobj = new Date();
                            currentRequest.put("isAccepted", true);
                            currentRequest.put("acceptedBy", ParseUser.getCurrentUser().getUsername());
                            currentRequest.put("acceptedOn",df.format(dateobj));
                            currentRequest.saveInBackground();
                             new AsyncTask<Void, Integer,Integer>() {
                                @Override
                                protected Integer doInBackground(Void... params) {
                                    try {
                                        generateAndSendEmailToAcceptor(currentRequest);
                                        generateAndSendEmailToRequestor(currentRequest);
                                    } catch (MessagingException e1) {
                                        e1.printStackTrace();
                                    }
                                    return 0;
                                }


                            }.execute();

                        }
                        else{e.printStackTrace();}
                    }
                });
                ((MainActivity_1) getActivity()).replaceFragment(new RequestsList(),true);
            }
        });
        loadRequestDetails(((MainActivity_1)getActivity()).getPosition());

        return view;
    }

    public void loadRequestDetails(int position){
        System.out.println("Position is "+position);
        Request req = ((MainActivity_1) getActivity()).getRequests().get(position);
     //   System.out.println(req.toString());
        item.setText(req.getItem());
        neededBy.setText(req.getNeededBy());
        requestedBy.setText(req.getRequestedBy());
        requestedOn.setText(req.getRequestedOn());
    /*    System.out.println("requested BY = "+ req.getRequestedBy());
        System.out.println("current user ");*/
        if(req.getRequestedBy().equals(ParseUser.getCurrentUser().getUsername())){
            acceptRequest.setVisibility(View.GONE);
        }


    }

    public boolean onSwipeRight() {
        if(currentPosition > 0){
            currentPosition = currentPosition -1;
            loadRequestDetails(currentPosition);

        }
        if(currentPosition ==0)
            loadRequestDetails(currentPosition);
        return false;
    }

    public boolean onSwipeLeft() {

        if( currentPosition < listEnd){
            currentPosition = currentPosition + 1;
            loadRequestDetails(currentPosition);

        }
        if(currentPosition == listEnd)
            loadRequestDetails(currentPosition);

        return false;
    }


    public void generateAndSendEmailToAcceptor(Requests req) throws  MessagingException {


        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(ParseUser.getCurrentUser().getEmail()));
        //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@gmail.com"));
        generateMailMessage.setSubject("Details of Accepted Request");
        String emailBody = "Here are the Details of the request you have accepted <br>" +
                "Item: "+ req.getString("item")+"<br>"+
                "Requested By: "+req.getString("RequestedBy")+"<br>"+
        "Requested On: "+ req.getString("requestedOn")+"<br>"+
                "Accepted On: "+req.getString("acceptedOn")+"<br>"+
                "You can Contact the requester at "+ req.getString("requestorEmail")
                +". <br> Thankyou, <br> All Share team";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "dandegus16105@gmail.com", "wwecmallikharjun");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    public void generateAndSendEmailToRequestor(Requests req) throws  MessagingException {

// Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getString("requestorEmail")));
        //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@gmail.com"));
        generateMailMessage.setSubject("An user has accepted to fulfil your request");
        String emailBody = "You have requested for <br> " +
                "Item: "+ req.getString("item")+"<br>"+
                "Requested By: "+req.getString("RequestedBy")+"<br>"+
                "Requested On:"+ req.getString("requestedOn")+"<br>"+
                "Accepted On: "+req.getString("acceptedOn")+"<br>"+
                "User "+ParseUser.getCurrentUser().getUsername() + " Has accepted your request."+"<br>"+
                "You can contact "+ParseUser.getCurrentUser().getUsername()+" at "+ ParseUser.getCurrentUser().getEmail()
                +". <br> Thankyou, <br> All Share team";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "dandegus16105@gmail.com", "wwecmallikharjun");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
