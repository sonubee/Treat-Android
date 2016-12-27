package gllc.tech.dateapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.FacebookAlbums.DisplayFacebookAlbums;
import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/11/2016.
 */

public class Profile extends Fragment {

    //SharedPreferences preferences;
    //SharedPreferences.Editor editor;
    CircleImageView profileImage;
    ImageView photo2, photo3, photo4;
    ImageView editBio, editPhoto1, editPhoto2, editPhoto3, editPhoto4;
    CrystalRangeSeekbar ageSeekBar;
    TextView name, bio, karmaPoints, ageRange;
    EditText enterBio;
    boolean editingBio = false;
    public static ArrayList<String> albumIds = new ArrayList<>();
    public static ArrayList<String> albumNames = new ArrayList<>();
    public static ArrayList<String> coverPhotosArray = new ArrayList<>();
    public static HashMap<String,String> albumIdToCoverPhoto = new HashMap<>();
    public static HashMap<String,String> albumIdToLink = new HashMap<>();
    public static int photoToReplace=0;

    String albumId;
    int a=0,b=0,c=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_profile, container, false);

        //preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        profileImage = (CircleImageView)view.findViewById(R.id.userPicture);
        name = (TextView)view.findViewById(R.id.nameUser);
        editBio = (ImageView)view.findViewById(R.id.editButton);
        editPhoto1 = (ImageView)view.findViewById(R.id.editButtonPhoto1);
        editPhoto2 = (ImageView)view.findViewById(R.id.editButtonPhoto2);
        editPhoto3 = (ImageView)view.findViewById(R.id.editButtonPhoto3);
        editPhoto4 = (ImageView)view.findViewById(R.id.editButtonPhoto4);
        bio = (TextView)view.findViewById(R.id.bioTextView);
        enterBio = (EditText)view.findViewById(R.id.bioEditText);
        photo2 = (ImageView)view.findViewById(R.id.supportImage1);
        photo3 = (ImageView)view.findViewById(R.id.supportImage2);
        photo4 = (ImageView)view.findViewById(R.id.supportImage3);
        karmaPoints = (TextView)view.findViewById(R.id.profileKarmaPoints);
        ageSeekBar = (CrystalRangeSeekbar)view.findViewById(R.id.ageSeekBar);
        ageRange = (TextView)view.findViewById(R.id.ageTangeTextView);

        //Picasso.with(getContext()).load("https://scontent.xx.fbcdn.net/v/t31.0-8/616355_10101220844195301_933715506_o.jpg?oh=d044b451beac88a1b86effb64c37dd45&oe=58E57F97").into(photo2);

        loadImages();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //name.setText(preferences.getString("name", "NA"));
        name.setText(MyApplication.currentUser.getName());

        karmaPoints.setText(MyApplication.currentUser.getKarmaPoints() + " Karma Points");

        editBio.setImageResource(R.drawable.edit);
        editPhoto1.setImageResource(R.drawable.edit);
        editPhoto2.setImageResource(R.drawable.edit);
        editPhoto3.setImageResource(R.drawable.edit);
        editPhoto4.setImageResource(R.drawable.edit);

//        bio.setText(preferences.getString("bio", "Enter Short Bio Here!"));
        bio.setText(MyApplication.currentUser.getBio());

        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editingBio) {
                    //enterBio.setText(preferences.getString("bio",""));
                    enterBio.setText(MyApplication.currentUser.getBio());
                    bio.setVisibility(View.INVISIBLE);
                    enterBio.setVisibility(View.VISIBLE);
                    editBio.setImageResource(R.drawable.save);
                    editingBio=true;
                } else {
                    //editor = preferences.edit();
                    //editor.putString("bio", enterBio.getText().toString());
                    //editor.apply();
                    MyApplication.currentUser.setBio(enterBio.getText().toString());
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

        editPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbums();
                photoToReplace=1;
            }
        });

        editPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbums();
                photoToReplace=2;
            }
        });

        editPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbums();
                photoToReplace=3;
            }
        });

        editPhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbums();
                photoToReplace=4;
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (MyApplication.currentUser.getPhoto1().equals("NA")) {
                    Picasso.with(getContext()).load("https://graph.facebook.com/" + MyApplication.currentUser.getFid() + "/picture?type=large").into(imageView);
                } else {
                    Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto1()).into(imageView);
                }
                dialog.show();
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (MyApplication.currentUser.getPhoto2().equals("NA")) {
                    getAlbums();
                    photoToReplace=2;
                } else {
                    Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto2()).into(imageView);
                }
                dialog.show();
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (MyApplication.currentUser.getPhoto3().equals("NA")) {
                    getAlbums();
                    photoToReplace=3;
                } else {
                    Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto3()).into(imageView);
                }
                dialog.show();
            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (MyApplication.currentUser.getPhoto4().equals("NA")) {
                    getAlbums();
                    photoToReplace=4;
                } else {
                    Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto4()).into(imageView);
                }
                dialog.show();
            }
        });

        ageSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                ageRange.setText(minValue + " - " + maxValue);
            }
        });
    }

    public void loadImages() {
        if (MyApplication.currentUser.getPhoto1().equals("NA")) {
            Picasso.with(getContext()).load("https://graph.facebook.com/" + MyApplication.currentUser.getFid() + "/picture?type=large").into(profileImage);
        } else {
            Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto1()).into(profileImage);
        }

        if (MyApplication.currentUser.getPhoto2().equals("NA")) {
            photo2.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto2()).into(photo2);
        }

        if (MyApplication.currentUser.getPhoto3().equals("NA")) {
            photo3.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto3()).into(photo3);
        }

        if (MyApplication.currentUser.getPhoto4().equals("NA")) {
            photo4.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(getContext()).load(MyApplication.currentUser.getPhoto4()).into(photo4);
        }

    }

    public void getAlbums(){
        albumIds.clear();
        b=0;
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
                            ((MainActivity)getActivity()).addFragments(DisplayFacebookAlbums.class, R.id.container, "DisplayFacebookAlbums");
                        }
                    }
                }
        ).executeAsync();
    }

    public void reloadProfileFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }
}
