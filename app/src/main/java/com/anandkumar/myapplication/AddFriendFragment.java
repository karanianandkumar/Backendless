package com.anandkumar.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendFragment extends Fragment {

    private Button addFriend;

    public AddFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_friend, container, false);

        addFriend=(Button)view.findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());

                alertDialog.setMessage("Add a Friend");

                final EditText friendName=new EditText(getActivity());
                alertDialog.setView(friendName);

                alertDialog.setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendFriendRequest(friendName.getText().toString());
                        Toast.makeText(getActivity(),"Add a friend",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });

                alertDialog.create();
                alertDialog.show();
            }
        });

                return view;
    }

    private void sendFriendRequest(final String friendName) {

        String currentUserID= Backendless.UserService.loggedInUser();
        Backendless.Persistence.of(BackendlessUser.class).findById(currentUserID, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser curUser) {

                Intent intent=new Intent(getActivity(),DemoService.class);
                intent.setAction(Constants.ACTION_SEND_FRIEND_REQUEST);

                intent.putExtra("fromUser",curUser.getProperty("name").toString());
                intent.putExtra("toUser",friendName);
                getActivity().startService(intent);

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });


    }

}
