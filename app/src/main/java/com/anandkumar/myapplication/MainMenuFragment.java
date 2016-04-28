package com.anandkumar.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {


    public static final int REQUEST_IMAGE_CAPTURE=1;
    private String imageDir;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_menu, container, false);;
       /* final String menuItems[]={
                "one",
                "two",
                "three",
                "four"
        };
        ListView listView=(ListView)view.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                menuItems
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(getActivity(),menuItems[position],Toast.LENGTH_SHORT).show();

            }
        }); */
        File imageFile=null;

        try {
                imageFile=createImgFile();
                imageDir=imageFile.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(imageFile!=null) {


            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            if (takePhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePhoto,REQUEST_IMAGE_CAPTURE);
            }
        }
        return view;
    }
    private File createImgFile()throws IOException{

        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName="JPEG_"+timeStamp+"_";

        File fileDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File img=File.createTempFile(fileName,".jpg",fileDir);

        return img;
    }
    private void addToImageGallary(String imageDirectory){

        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f=new File(imageDirectory);
        Uri uri=Uri.fromFile(f);
        mediaScanIntent.setData(uri);


        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            if(resultCode== Activity.RESULT_OK){
                addToImageGallary(imageDir);
            }

        }
    }
}
