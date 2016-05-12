package com.anandkumar.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;

public class DemoService extends IntentService {



    public DemoService() {
        super("DemoService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String action=intent.getAction();
            if(action==Constants.ACTION_ADD_FRIEND){
                String firstUserName=intent.getStringExtra("firstUserName");
                String secondUserName=intent.getStringExtra("secondUserName");
                Log.i("DemoService","Demo Added Friend"+ firstUserName +" ::: "+secondUserName);
                addFriends(firstUserName,secondUserName);
            }else if(action==Constants.ACTION_SEND_FRIEND_REQUEST){
                String toUser=intent.getStringExtra("toUser");
                String fromUser=intent.getStringExtra("fromUser");
                Log.i("Demo Service","Send Friend request to "+toUser+"  from  "+fromUser);
            }
        }

    }

    private void addFriends(String firstName,String secondName){

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("name= '%s' or name= '%s'",firstName,secondName));
        Backendless.Persistence.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                List<BackendlessUser> users=response.getData();

                if(users.size()!=2){
                    //TODO: ERROR
                    broadcastAddFriendFailure();
                }else{
                    final BackendlessUser user1=users.get(0);
                    final BackendlessUser user2=users.get(1);

                    updateFriendsList(user1,user2);
                    Backendless.UserService.update(user1, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            updateFriendsList(user2,user1);
                            Backendless.UserService.update(user2, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {
                                    broadcastAddFriendSuccess();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    broadcastAddFriendFailure();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            broadcastAddFriendFailure();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                broadcastAddFriendFailure();
            }
        });
    }

    private void broadcastAddFriendSuccess(){

        Intent intent=new Intent(Constants.BROADCAST_ADD_FRIEND_SUCCESS);
        sendBroadcast(intent);
    }
    private void broadcastAddFriendFailure(){

        Intent intent=new Intent(Constants.BROADCAST_ADD_FRIEND_FAILURE);
        sendBroadcast(intent);
    }

   private void updateFriendsList(BackendlessUser user,BackendlessUser friend){
       BackendlessUser[] newFriends;

       Object[] currentFriendsObject=(Object[]) user.getProperty("friends");

       if(currentFriendsObject.length>0){
            BackendlessUser[] currentFriends=(BackendlessUser[])currentFriendsObject;
           newFriends=new BackendlessUser[currentFriends.length+1];
           for(int i=0;i<currentFriends.length;i++){
               newFriends[i]=currentFriends[i];
           }
           newFriends[newFriends.length-1]=friend;

       }else{
           newFriends=new BackendlessUser[]{friend};
       }

       user.setProperty("friends",newFriends);
    }
}
