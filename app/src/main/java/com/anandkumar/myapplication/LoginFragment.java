package com.anandkumar.myapplication;


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
public class LoginFragment extends Fragment {

    private Button loginButton;
    private EditText userNameField;
    private EditText passwordField;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        userNameField=(EditText)view.findViewById(R.id.loginUserNameField);
        passwordField=(EditText)view.findViewById(R.id.loginPasswordField);
        loginButton=(Button)view.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=userNameField.getText().toString();
                String password=passwordField.getText().toString();

                Backendless.UserService.login(userName, password, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Toast.makeText(getActivity(),
                                "Succefully Loged in",
                                Toast.LENGTH_SHORT
                        ).show();
                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(getActivity(),
                                "Failed to Loged in",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },true);
            }
        });


        return view;
    }

}
