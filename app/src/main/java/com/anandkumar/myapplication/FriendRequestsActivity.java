package com.anandkumar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        FriendRequestsFragment addFriendFragment=new FriendRequestsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.friendRequestContainer,addFriendFragment).commit();
    }
}
