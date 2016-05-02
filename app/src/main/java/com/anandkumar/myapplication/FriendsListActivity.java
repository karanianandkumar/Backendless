package com.anandkumar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        FriendsListFragment friendsListFragment=new FriendsListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.friendsListContainer,friendsListFragment).commit();
    }
}
