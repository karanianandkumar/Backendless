package com.anandkumar.myapplication;

/**
 * Created by Anand on 5/12/2016.
 */
public class FriendRequest {

    private String toUser;
    private String fromUser;
    private boolean isAccepted;

    public FriendRequest(){
        this.toUser="";
        this.fromUser="";
        this.isAccepted=false;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public boolean isAccepted(){
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted){
        this.isAccepted=isAccepted;
    }
}
