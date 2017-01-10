package gllc.tech.dateapp.Loading;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SimpleCalculations;
import gllc.tech.dateapp.FacebookAlbums.DisplayFacebookAlbums;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/11/2016.
 */

public class Profile extends Fragment {
    CircleImageView profileImage;
    ImageView photo2, photo3, photo4;
    ImageView editPhoto1, editPhoto2, editPhoto3, editPhoto4;
    TextView name, bio, karmaPoints, ageRange, gender, school;
    EditText enterBio;
    RelativeLayout chooseSchool;
    public static ArrayList<String> albumIds = new ArrayList<>();
    public static ArrayList<String> albumNames = new ArrayList<>();
    public static ArrayList<String> coverPhotosArray = new ArrayList<>();
    public static HashMap<String,String> albumIdToCoverPhoto = new HashMap<>();
    public static HashMap<String,String> albumIdToLink = new HashMap<>();
    public static int photoToReplace=0;

    String albumId;
    int a=0,b=0,c=0;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile, container, false);

        LinearLayout test = (LinearLayout)view.findViewById(R.id.mainLinearLayout);
        LinearLayout second = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.about_you, null);
        RelativeLayout third = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.event_adapter3, null);
        test.addView(second);
        test.addView(third);

        gender = (TextView)view.findViewById(R.id.listGender);
        profileImage = (CircleImageView)view.findViewById(R.id.userPicture);
        name = (TextView)view.findViewById(R.id.nameUser);
        //editBio = (ImageView)view.findViewById(R.id.editButton);
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
        ageRange = (TextView)view.findViewById(R.id.ageTangeTextView);
        school = (TextView)view.findViewById(R.id.schoolProfile);
        chooseSchool = (RelativeLayout)view.findViewById(R.id.chooseSchool);

        loadImages();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name.setText(MyApplication.currentUser.getName());

        gender.setText(MyApplication.currentUser.getGender().substring(0,1).toUpperCase() + MyApplication.currentUser.getGender().substring(1));
        karmaPoints.setText(MyApplication.currentUser.getKarmaPoints() + " Karma Points");

        if (MyApplication.currentUser.getSchool().equals("NA")) {
            school.setText("Select One Here");
        } else if (MyApplication.currentUser.getSchool().equals("None")) {
            school.setText("None Chosen");
        }else {
            school.setText(MyApplication.currentUser.getSchool());
        }

        setBirthdate();

        editPhoto1.setImageResource(R.drawable.edit);
        editPhoto2.setImageResource(R.drawable.edit);
        editPhoto3.setImageResource(R.drawable.edit);
        editPhoto4.setImageResource(R.drawable.edit);

        enterBio.setText(MyApplication.currentUser.getBio());

        enterBio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyApplication.currentUser.setBio(enterBio.getText().toString());
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

        getProfilePhoto();

        if (!MyApplication.currentUser.getId().equals("ZyOum0RLpLUqSxQ9if8OTjZVD7y1")) {
            setLatitudeLongitude();
        }

        chooseSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEducation();
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
        parameters.putString("redirect", "false");
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
                            ((MainActivity)getActivity()).addFragments(DisplayFacebookAlbums.class, R.id.container, "DisplayFacebookAlbums", null);
                        }
                    }
                }
        ).executeAsync();
    }


    public void getProfilePhoto() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        parameters.putString("redirect", "redirect=false");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + MyApplication.currentUser.getFid() + "/picture",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Login.dialog.hide();
                        Log.i("--All", "Response: " + response.toString());
                        JSONObject jsonObject = response.getJSONObject();
                        try{
                            //JSONArray imagesArrayJSON = jsonObject.getJSONArray("images");
                            //albumIdToLink.put(albumIdToUse, imagesArrayJSON.getJSONObject(0).getString("source"));
                        }catch (Exception e){
                            Log.i("--All", "Error: " + e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }

    public void setLatitudeLongitude() {
        databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/latitude");
        databaseReference.setValue(MyApplication.latitude);

        databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/longitude");
        databaseReference.setValue(MyApplication.longitude);
    }




    public void getEducation() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "education");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + MyApplication.currentUser.getFid(),
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try{
                            ArrayList<String> educationList = new ArrayList<String>();
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);

                            JSONObject education = response.getJSONObject();
                            JSONArray educationArray = education.getJSONArray("education");

                            for (int i=0; i<educationArray.length(); i++) {
                                JSONObject schoolData = educationArray.getJSONObject(i);
                                JSONObject school = schoolData.getJSONObject("school");

                                arrayAdapter.add(school.getString("name"));
                            }

                            arrayAdapter.add("None");

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                            builderSingle.setTitle("Which School to Display?");

                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    school.setText(arrayAdapter.getItem(which));

                                    databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId() +"/school");
                                    databaseReference.setValue(arrayAdapter.getItem(which));
                                }
                            });

                            builderSingle.show();

                        }catch (Exception e){
                            Log.i("--All", "Error: " + e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }

    public void setBirthdate() {

        if (MyApplication.currentUser.isGaveFullBirthday()) {
            name.append(", "  + SimpleCalculations.getAge(MyApplication.currentUser));
        }

    }

    public static Profile newInstance() {

        Profile f = new Profile();
        return f;
    }

}
