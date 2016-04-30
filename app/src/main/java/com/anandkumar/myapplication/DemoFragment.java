package com.anandkumar.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment {

    public static final int CHOOSE_PHOTO=2;

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
            startActivityForResult(choosePhotoIntent,CHOOSE_PHOTO);
        }
        return view;
    }

    private void displayPopupImage(Bitmap bitmapImage){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("You selected this Image!!");

        ImageView imageView=new ImageView(getActivity());
        imageView.setImageBitmap(bitmapImage);

        alertDialog.setView(imageView);
        alertDialog.create();
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_PHOTO){
            if (resultCode== Activity.RESULT_OK){
                Uri uri=data.getData();

                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    displayPopupImage(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
