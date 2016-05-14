package com.anandkumar.myapplication;

/**
 * Created by Anand on 5/14/2016.
 */
public class SentPicture {

    private boolean viewed;
    private String fromUser;
    private String toUser;
    private String imageLocation;

    public SentPicture(){
        this.viewed=false;
        this.fromUser="";
        this.toUser="";
        this.imageLocation="";
    }
    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
