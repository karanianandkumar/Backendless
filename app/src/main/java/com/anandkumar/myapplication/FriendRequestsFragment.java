package com.anandkumar.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private ArrayList<FriendRequest> friendRequests;
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
        friendRequests=new ArrayList<FriendRequest>();
        adapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                fromUsers
        );

        ListView friendRequests=(ListView)view.findViewById(R.id.friendRequests);
        friendRequests.setAdapter(adapter);

        friendRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);
            }
        });

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

    private void showAlertDialog(final int position) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("Accept Friend Request from "+ fromUsers.get(position)+ " ?");

        dialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    acceptRequest(friendRequests.get(position));
            }
        });

        dialog.create();
        dialog.show();
    }

    private void acceptRequest(FriendRequest friendRequest) {

        Toast.makeText(getActivity(),"Accepted Request!",Toast.LENGTH_SHORT).show();
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
                        friendRequests.add(request);
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
