package com.anandkumar.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;


public class FriendsListFragment extends Fragment {


    private ArrayList<String> friends;
    private ArrayAdapter<String> friendsList;
    public FriendsListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends_list, container, false);

        friends=new ArrayList<String>();
        friendsList=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                friends
        );
        ListView friendsLV=(ListView)view.findViewById(R.id.friendsList);
        friendsLV.setAdapter(friendsList);

        String currentUser= Backendless.UserService.loggedInUser();
        Backendless.Persistence.of(BackendlessUser.class).findById(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                Object[] friendsObject=(Object[]) user.getProperty("friends");
                BackendlessUser[] backendlessFriends=(BackendlessUser[])friendsObject;

                if(backendlessFriends.length>0){
                    for(BackendlessUser friend:backendlessFriends){

                        String name=friend.getProperty("name").toString();
                        friends.add(name);
                        friendsList.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        return view;
    }


}
