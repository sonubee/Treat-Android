package gllc.tech.dateapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bhangoo on 12/11/2016.
 */

public class Profile extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CircleImageView profileImage;
    ImageView editBio;
    TextView name, bio;
    EditText enterBio;
    boolean editingBio = false;
    public static ArrayList<String> albumIds = new ArrayList<>();
    public static ArrayList<String> albumNames = new ArrayList<>();
    public static ArrayList<String> imageURLs = new ArrayList<>();
    public static ArrayList<String> coverPhotosArray = new ArrayList<>();
    public static ArrayList<Boolean> hasHoverPhotos = new ArrayList<>();
    public static HashMap<String,String> albumIdToCoverPhoto = new HashMap<>();
    public static HashMap<String,String> albumIdToLink = new HashMap<>();

    String albumId;
    int a=0,b=0,c=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        profileImage = (CircleImageView)view.findViewById(R.id.userPicture);
        name = (TextView)view.findViewById(R.id.nameUser);
        editBio = (ImageView)view.findViewById(R.id.editButton);
        bio = (TextView)view.findViewById(R.id.bioTextView);
        enterBio = (EditText)view.findViewById(R.id.bioEditText);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getContext()).load("https://graph.facebook.com/" + preferences.getString("fid", "NA") + "/picture?type=large").into(profileImage);
        name.setText(preferences.getString("name", "NA"));

        editBio.setImageResource(R.drawable.edit);
        bio.setText(preferences.getString("bio", "Enter Short Bio Here!"));

        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editingBio) {
                    enterBio.setText(preferences.getString("bio",""));
                    bio.setVisibility(View.INVISIBLE);
                    enterBio.setVisibility(View.VISIBLE);
                    editBio.setImageResource(R.drawable.save);
                    editingBio=true;
                } else {
                    editor = preferences.edit();
                    editor.putString("bio", enterBio.getText().toString());
                    editor.apply();
                    bio.setText(enterBio.getText().toString());
                    bio.setVisibility(View.VISIBLE);
                    enterBio.setVisibility(View.INVISIBLE);
                    editBio.setImageResource(R.drawable.edit);
                    editingBio=false;

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/bio");
                    databaseReference.setValue(enterBio.getText().toString());
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject();
                                    if (jsonObject.has("data")) {
                                        JSONArray jaData = jsonObject.optJSONArray("data");
                                        for (int i = 0; i < jaData.length(); i++) {
                                            try {
                                                JSONObject albumObject = jaData.getJSONObject(i);

                                                albumId = albumObject.getString("id");
                                                albumIds.add(albumId);
                                                String albumName = albumObject.getString("name");
                                                albumNames.add(albumName);

                                                Log.i("--All", "Album Size Early: " + albumIds.size());
                                                getCoverPhotoID(albumId);

                                                //GetFacebookImages(albumId); //find Album ID and get All Images from album

                                                //Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE" + albumId);


                                            } catch (Exception e) {
                                                Log.i("--All", "Error: " + e.toString());

                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                ).executeAsync();

                //MyApplication.selectedImageUrl = MyApplication.currentUser.getProfilePic();
                //((MainActivity)getActivity()).addFragments(FullImageFragment.class, R.id.profileLayout, "ProfileScreen");
            }
        });



    }

    public void getCoverPhotoID(final String albumIdToUse) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "cover_photo");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumIdToUse,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObject = response.getJSONObject();

                                Log.i("--All", "JSON: " + jsonObject.toString());

                                if (jsonObject.has("cover_photo")) {

                                    JSONObject coverPhotoJSON = jsonObject.getJSONObject("cover_photo");
                                    String coverPhotoId = coverPhotoJSON.get("id").toString();
                                    coverPhotosArray.add(coverPhotoId);

                                    albumIdToCoverPhoto.put(albumIdToUse, coverPhotoId);


                                    //Log.i("--All", "AlbumID: " + albumIdToUse);
                                    //Log.i("--All", "Cover Photo ID: " + coverPhotoId);
                                    Log.i("--All", "Cover Photo Size: " + coverPhotosArray.size());

                                    //getCoverPhotoPicture(coverPhotoId, albumIdToUse);
                                    getCoverPhotoPicture2(coverPhotoId, albumIdToUse);


                                } else{
                                    coverPhotosArray.add("NA");
                                    albumIdToCoverPhoto.put(albumIdToUse,"NA");
                                    b++;
                                }

                            } else {
                                Log.v("--All", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

    public void getCoverPhotoPicture2(String coverPhotoId, final String albumIdToUse) {
        a++;
        Log.i("--All", "Called: " + a);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + coverPhotoId,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //Log.i("--All", "Response: " + response.toString());
                        JSONObject jsonObject = response.getJSONObject();
                        Log.i("--All", "JSON: " + jsonObject.toString());

                        try{
                            JSONArray imagesArrayJSON = jsonObject.getJSONArray("images");
                            Log.i("--All", "Images: " +imagesArrayJSON.getJSONObject(0).getString("source"));
                            if (!albumIdToCoverPhoto.get(albumIdToUse).equals("NA")) {
                                albumIdToLink.put(albumIdToUse, imagesArrayJSON.getJSONObject(0).getString("source"));
                                //imageURLs.add(imagesArrayJSON.getJSONObject(0).getString("source"));
                            }else{
                                //imageURLs.add("NA");
                                albumIdToLink.put(albumIdToUse, "NA");
                            }

                        }catch (Exception e){
                            Log.i("--All", "Error: " + e.getMessage());
                        }

                        b++;

                        if (b == albumIds.size()) {
                            ((MainActivity)getActivity()).addFragments(DisplayFacebookAlbums.class, R.id.profileLayout, "Profile");
                        }

                        Log.i("--All", "B: " + b);
                        Log.i("--All", "Album: " + albumIds.size());

                    }
                }
        ).executeAsync();
    }

    public void getCoverPhotoPicture(final String coverPhotoId, String albumIdToUse) {
        //        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumIdToUse + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //Log.v("--All", "Facebook Photos response: " + response);
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();

                                Log.i("--All", "JSON from Getting Links: "+ joMain.toString());
                                Log.i("--All", "Cover Photo ID to Match: " + coverPhotoId);

                                boolean found = false;

                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");// get images Array in JSONArray format
                                        if (jaImages.length() > 0) {
                                            //imageURLs.add(jaImages.getJSONObject(0).getString("source"));
                                            Log.i("--All", "Picture Link: "+jaImages.getJSONObject(0).getString("source"));
                                            if (jaImages.getJSONObject(0).getString("source").contains(coverPhotoId)) {
                                                found = true;
                                                b++;
                                                imageURLs.add(jaImages.getJSONObject(0).getString("source"));
                                            }
                                        }
                                    }

                                    if (b > (albumIds.size()-10)) {
                                        ((MainActivity)getActivity()).addFragments(DisplayFacebookAlbums.class, R.id.profileLayout, "Profile");
                                    }



                                    //set your adapter here
                                }

                            } else {
                                Log.v("--All", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();

    }

    public void GetFacebookImages(final String albumId) {
//        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        //Log.v("--All", "Facebook Photos response: " + response);
                        try {
                            if (response.getError() == null) {


                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");// get images Array in JSONArray format
                                        if (jaImages.length() > 0) {
                                            imageURLs.add(jaImages.getJSONObject(0).getString("source"));
                                            //Log.i("--All", "Picture Link: "+jaImages.getJSONObject(0).getString("source"));
                                        }
                                    }




                                    //set your adapter here
                                }

                            } else {
                                Log.v("--All", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }




}
