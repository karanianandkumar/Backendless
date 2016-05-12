package com.anandkumar.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequestsFragment extends Fragment {

    private ArrayList<String> fromUsers;
    private ArrayAdapter<String> adapter;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_requests, container, false);

        fromUsers=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                fromUsers
        );

        ListView friendRequests=(ListView)view.findViewById(R.id.friendRequests);
        friendRequests.setAdapter(adapter);

        //Get name of the current loggedin user

        String currentUserId= Backendless.UserService.loggedInUser();

        Backendless.UserService.findById(currentUserId, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                String currentUserName=response.getProperty("name").toString();

                getIncomingFriendRequests(currentUserName);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        return view;
    }

    private void getIncomingFriendRequests(String currentUserName) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("toUser= '%s'",currentUserName));

        Backendless.Persistence.of(FriendRequest.class).find(query, new AsyncCallback<BackendlessCollection<FriendRequest>>() {
            @Override
            public void handleResponse(BackendlessCollection<FriendRequest> response) {
                List<FriendRequest> incomingRequests=response.getData();
                for(FriendRequest request:incomingRequests ){
                    if(!request.isAccepted()){
                        fromUsers.add(request.getFromUser());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

}
