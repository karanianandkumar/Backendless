package com.anandkumar.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment {


    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_demo, container, false);

        Intent choosePhotoIntent=new Intent(Intent.ACTION_GET_CONTENT);
        choosePhotoIntent.setType("image/*");

        if(choosePhotoIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(choosePhotoIntent);
        }
        return view;
    }

}
