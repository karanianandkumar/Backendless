package com.anandkumar.myapplication;


import android.app.Activity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {


    //public static final int REQUEST_IMAGE_CAPTURE=1;
    public static final int REQUEST_CHOOSE_PHOTO=2;
    private String imageDir;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_menu, container, false);;
        final String menuItems[]={
                "Send Picture"
        };
        ListView listView=(ListView)view.findViewById(R.id.main_menu_listView);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                menuItems
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   if(position==0){
                       Intent intent=new Intent();
                       intent.setType("image/*");
                       intent.setAction(Intent.ACTION_GET_CONTENT);
                       startActivityForResult(Intent.createChooser(intent,"Select Picture"),
                               REQUEST_CHOOSE_PHOTO);
                   }

            }
        });
        /*File imageFile=null;

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

        */
        return view;
    }
   /* private File createImgFile()throws IOException{

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

*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CHOOSE_PHOTO){
            if(resultCode== Activity.RESULT_OK){
              //  addToImageGallary(imageDir);
                Uri uri=data.getData();
                Intent intent=new Intent(getActivity(),FriendsListActivity.class);
                intent.putExtra("imageURI",uri);
                startActivity(intent);
            }

        }
    }
}
