package com.anandkumar.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private ArrayList<String> fromFriends;
    private ArrayList<SentPicture> incomingPhotos;
    private ArrayAdapter<String> fromFriendsadapter;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inbox, container, false);

        fromFriends=new ArrayList<String>();
        incomingPhotos=new ArrayList<SentPicture>();
        fromFriendsadapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                fromFriends
        );

        ListView friendsList=(ListView)view.findViewById(R.id.incomingPhotos);
        friendsList.setAdapter(fromFriendsadapter);

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayImageFomFriend(position);
            }
        });

        String currentUserId= Backendless.UserService.loggedInUser();

        Backendless.UserService.findById(currentUserId, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                String currentUserName=response.getProperty("name").toString();

                getPhotosSendTo(currentUserName);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        return view;
    }

    private void displayImageFomFriend(int position) {

        String imageLocation=incomingPhotos.get(position).getImageLocation();

        try{
            URL url=new URL("https://api.backendless.com/B95DAA23-ED7C-373A-FF03-DDB3ECB1DA00/v1/files/"+imageLocation);
            DownloadImage downloadImage=new DownloadImage();
            downloadImage.execute(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    private void getPhotosSendTo(String currentUserName) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("toUser= '%s'",currentUserName));

        Backendless.Persistence.of(SentPicture.class).find(query, new AsyncCallback<BackendlessCollection<SentPicture>>() {
            @Override
            public void handleResponse(BackendlessCollection<SentPicture> response) {
                List<SentPicture> incomingRequests=response.getData();
                for(SentPicture photo:incomingRequests ){
                    if(!photo.isViewed()){
                        fromFriends.add(photo.getFromUser());
                        incomingPhotos.add(photo);
                    }
                }
                fromFriendsadapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
    private class DownloadImage extends AsyncTask<URL,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(URL... params) {
            for(URL url: params){
                try {
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    int responseCode=httpConn.getResponseCode();
                    if(responseCode== HttpURLConnection.HTTP_OK){
                        InputStream inputStream=httpConn.getInputStream();
                        Bitmap imageBitmap= BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        return imageBitmap;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

                Toast.makeText(getActivity(),"Bitmap is not empty",Toast.LENGTH_SHORT).show();

            displayPopupImage(bitmap);
        }
    }

    private void displayPopupImage(Bitmap bitmap) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("Incoming Photo");
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ImageView photo=new ImageView(getActivity());


        photo.setLayoutParams(new ViewGroup.LayoutParams(300,500));
        photo.setImageBitmap(bitmap);
        dialog.setView(photo);
        dialog.create();
        dialog.show();
    }

}
