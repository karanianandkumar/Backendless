package com.anandkumar.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

                sendFriendRequest(fromUser,toUser);
            }else if(action.equals(Constants.ACTION_SEND_PHOTO)){
                String fromUser=intent.getStringExtra("fromUser");
                String toUser=intent.getStringExtra("toUser");
                Uri imageUri=intent.getParcelableExtra("imageURI");
                sendPhoto(fromUser,toUser,imageUri);
            }
        }

    }

    private void sendPhoto(String fromUser, String toUser, Uri imageUri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            String timeFormat=new SimpleDateFormat("yyyyMMdd:HHmmss").format(new Date());
            String fileName="JPEG_"+timeFormat+"_.jpg";


            Backendless.Files.Android.upload(
                    bitmap,
                    Bitmap.CompressFormat.JPEG,
                    100,
                    fileName,
                    "sendPics",
                    new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            Toast.makeText(getApplicationContext(),"Successfully sent to backendless",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    }
            );

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendFriendRequest(final String fromUser, final String toUser){
        //Make sure user exist.
        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("name= '%s'",toUser));

        Backendless.Persistence.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                if(response.getData().size()==0){
                    //TODO: Broadcast Failuere
                    broadcastFriendRequestFailure();
                }else{
                    //Create a friend request.

                    FriendRequest friendRequest=new FriendRequest();
                    friendRequest.setFromUser(fromUser);
                    friendRequest.setToUser(toUser);
                    friendRequest.setAccepted(false);

                    //Save in backendless

                    Backendless.Persistence.save(friendRequest, new AsyncCallback<FriendRequest>() {
                        @Override
                        public void handleResponse(FriendRequest response) {
                            broadcastFriendRequestSuccess();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            //TODO: Broadcast Failuere

                            broadcastFriendRequestFailure();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                //TODO: Broadcast Failuere
                broadcastFriendRequestFailure();
            }
        });

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

    private void broadcastFriendRequestSuccess(){
        Intent intent=new Intent(Constants.BROADCAST_FRIEND_REQUEST_SUCCESS);
        sendBroadcast(intent);
    }

    private void broadcastFriendRequestFailure(){
        Intent intent=new Intent(Constants.BROADCAST_FRIEND_REQUEST_FAILURE);
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
