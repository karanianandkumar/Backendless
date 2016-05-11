package com.anandkumar.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DemoReceiver extends BroadcastReceiver {
    public DemoReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       String action=intent.getAction();
        if(action.equals(Constants.BROADCAST_ADD_FRIEND_SUCCESS)){
            Toast.makeText(context,"Added friend Succesfully",Toast.LENGTH_SHORT).show();
        }else if(action.equals(Constants.BROADCAST_ADD_FRIEND_FAILURE)){
            Toast.makeText(context,"Failed to Add friend",Toast.LENGTH_SHORT).show();
        }
    }
}
