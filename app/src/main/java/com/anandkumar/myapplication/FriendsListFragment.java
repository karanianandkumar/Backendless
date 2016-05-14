package com.anandkumar.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;


public class FriendsListFragment extends Fragment {


    private ArrayList<String> friends;
    private ArrayAdapter<String> friendsListAdapter;
    public FriendsListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends_list, container, false);

        final Uri imageToSend=getActivity().getIntent().getParcelableExtra("imageURI");
        friends=new ArrayList<String>();
        friendsListAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                friends
        );
        final ListView friendsList=(ListView)view.findViewById(R.id.friendsList);
        friendsList.setAdapter(friendsListAdapter);

        final String currentUser= Backendless.UserService.loggedInUser();
        Backendless.Persistence.of(BackendlessUser.class).findById(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                Object[] friendsObject=(Object[]) user.getProperty("friends");
                BackendlessUser[] backendlessFriends=(BackendlessUser[])friendsObject;

                if(backendlessFriends.length>0){
                    for(BackendlessUser friend:backendlessFriends){

                        String name=friend.getProperty("name").toString();
                        friends.add(name);
                        friendsListAdapter.notifyDataSetChanged();
                    }
                }
                String currentUserName=(String)user.getProperty("name");
                friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String friendName=(String)parent.getItemAtPosition(position);
                        sendImageToFriend(currentUser,friendName,imageToSend);
                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        return view;
    }


    private void sendImageToFriend(String currentUser,String toUser,Uri sentImage){

        //Start service to send image to friend

        Intent intent=new Intent(getActivity(),DemoService.class);
        intent.setAction(Constants.ACTION_SEND_PHOTO);
        intent.putExtra("fromUser",currentUser);
        intent.putExtra("toUser",toUser);
        intent.putExtra("imageURI",sentImage);
        getActivity().startService(intent);
    }
}
