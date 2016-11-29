package com.allshare_back4app.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Requests")
public class Requests extends ParseObject{

    String requestedBy;
    String neededBy;
    String requestedOn;
    String item;
    boolean isAccepted;


    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Requests() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getNeededBy() {
        return neededBy;
    }

    public void setNeededBy(String neededBy) {
        this.neededBy = neededBy;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Override
    public String toString() {
        return  "Requested item: " + getString("item")+ "\n"+
                "Requested By(User): " + getString("RequestedBy") + '\n' +
                "Accepted On: "+getString("acceptedOn")+ '\n' +
                "Needed by: "+getString("neededBy");
    }
}
