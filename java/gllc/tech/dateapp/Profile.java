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
    public static ArrayList<String> coverPhotosArray = new ArrayList<>();
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

                                                getCoverPhotoID(albumId);

                                            } catch (Exception e) {Log.i("--All", "Error: " + e.toString());}
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                ).executeAsync();
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

                                if (jsonObject.has("cover_photo")) {

                                    JSONObject coverPhotoJSON = jsonObject.getJSONObject("cover_photo");
                                    String coverPhotoId = coverPhotoJSON.get("id").toString();
                                    coverPhotosArray.add(coverPhotoId);

                                    albumIdToCoverPhoto.put(albumIdToUse, coverPhotoId);

                                    getCoverPhotoPicture2(coverPhotoId, albumIdToUse);


                                } else{
                                    coverPhotosArray.add("NA");
                                    albumIdToCoverPhoto.put(albumIdToUse,"NA");
                                    albumIdToLink.put(albumIdToUse, "NA");
                                    b++;
                                }
                            } else {
                                Log.v("--All", "Error: " + response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void getCoverPhotoPicture2(String coverPhotoId, final String albumIdToUse) {
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
                        try{
                            JSONArray imagesArrayJSON = jsonObject.getJSONArray("images");
                            albumIdToLink.put(albumIdToUse, imagesArrayJSON.getJSONObject(0).getString("source"));
                        }catch (Exception e){
                            Log.i("--All", "Error: " + e.getMessage());
                        }

                        b++;
                        if (b == albumIds.size()) {
                            ((MainActivity)getActivity()).addFragments(DisplayFacebookAlbums.class, R.id.container, "Profile");
                        }
                    }
                }
        ).executeAsync();
    }
}
