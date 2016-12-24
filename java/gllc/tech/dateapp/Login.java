package gllc.tech.dateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.UpComingDates.YourDatesFragment;

/**
 * Created by bhangoo on 12/3/2016.
 */
public class Login extends Fragment {

    public static CallbackManager callbackManager;
    private LoginButton loginButton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    boolean doneDownloading=false;
    JSONObject facebookLoginResponseJSONObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MyApplication.agreedChats.clear();
        MyApplication.allDates.clear();
        MyApplication.combinedDates.clear();
        MyApplication.fullMatchesAsCreator.clear();
        MyApplication.fullMatchesAsDate.clear();
        MyApplication.pendingDates.clear();

        callbackManager = CallbackManager.Factory.create();

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        setupFacebookLogin();

        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                try{
                    currentAccessToken.getToken();
                } catch (Exception e){
                    Log.i("--All", "Logged Out from AccessTokenTracker");
                    FirebaseAuth.getInstance().signOut();

                }

                if (currentAccessToken != null){
                    Log.i("--All", "Logged in from AccessTokenTracker");
                    Log.i("--All", "Permissions: " + currentAccessToken.getPermissions().toString());

                }
            }
        };



        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        try {
            if (accessToken.getToken() != null){
                Log.i("--All", "Logged In Already");
                //upload/ProfilePhoto();
                loginButton.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){
            Log.i("--All", "Logged Out Already");
        }




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.i("--All", "Logged in through Firebase");
                    // User is signed in
/*
                    if (preferences.getString("id", "NA").equals("NA")) {
                        Log.i("--All", "Inside Not Found");
                        MyApplication.currentUser = new User(preferences.getString("name", "NA"), preferences.getString("email", "NA"), user.getUid(),
                                preferences.getString("gender", "NA"), preferences.getString("profilePic", "NA"), preferences.getString("fid", "NA"),
                                preferences.getString("bio", "NA"), preferences.getString("photo1", "NA"), preferences.getString("photo2", "NA"),
                                preferences.getString("photo3", "NA"), preferences.getString("photo4", "NA"), preferences.getInt("karmaPoints", 0));

                        editor = preferences.edit();
                        editor.putString("id", user.getUid());
                        editor.apply();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" +MyApplication.currentUser.getId());
                        myRef.setValue(MyApplication.currentUser);
                    }
*/

                    downloadUsers(user);

                    //if (loggedInFacebook && doneDownloading) {
                    //    ((MainActivity)getActivity()).replaceFragments(gllc.tech.dateapp.Profile.class, R.id.container, "Profile");
                    //}

                } else {
                    // User is signed out
                    Log.i("--All", "Logged out through Firebase");
                }
                // ...
            }
        };


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);




    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("--All", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("--All", "signInWithCredential", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void setupFacebookLogin(){

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends", "user_photos"));
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                editor = preferences.edit();
                Toast.makeText(getContext(), "Successful Login", Toast.LENGTH_LONG).show();
                final AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                Log.i("--All", "Logged In through FaceBook");
                handleFacebookAccessToken(loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                facebookLoginResponseJSONObject = object;
                                loginButton.setVisibility(View.INVISIBLE);
                                /*
                                try {
                                    editor.putString("accessToken", accessToken.getToken());
                                    editor.putString("email",object.getString("email"));
                                    editor.putString("fid", object.getString("id"));
                                    editor.putString("name", object.getString("name"));
                                    editor.putString("gender", object.getString("gender"));
                                    editor.putString("profilePic", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                                    editor.apply();

                                    if (preferences.getString("id", "NA").equals("NA")) {
                                        MyApplication.currentUser = new User(object.getString("name"), object.getString("email"), "NA", preferences.getString("gender", ""),
                                                "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large", object.getString("id"),
                                                preferences.getString("bio", ""), preferences.getString("photo1", "NA"), preferences.getString("photo2", "NA"),
                                                preferences.getString("photo3", "NA"), preferences.getString("photo4", "NA"), preferences.getInt("karmaPoints", 0));
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                */
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }

        });
    }

/*
    public void downloadAgreedChats(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("AgreedChats/" + MyApplication.currentUser.getId());

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                AgreedChats agreedChats = dataSnapshot.getValue(AgreedChats.class);

                if (!agreedChats.getPoster().equals(MyApplication.currentUser.getId())){
                    MyApplication.agreedChats.add(agreedChats);
                }

                if (agreedChats.getPoster().equals(MyApplication.currentUser.getId()) && !MyApplication.justPosted){
                    MyApplication.agreedChats.add(agreedChats);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    */
    public void downloadDates() {

        MyApplication.allDates.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Dates");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TheDate value = dataSnapshot.getValue(TheDate.class);
                value.setKey(dataSnapshot.getKey());

                boolean completed = false;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date strDate = sdf.parse(value.getDateOfDate());
                    if (System.currentTimeMillis() > strDate.getTime()) {

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Dates/"+value.getKey());
                        databaseReference.removeValue();

                        //DatabaseReference databaseReference1 = firebaseDatabase.getReference("CompletedDates/"+value.getKey());
                        //databaseReference1.setValue(value);

                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("CompletedDates/"+ value.getPoster() + "/" + value.getKey());
                        databaseReference1.setValue(value);

                        databaseReference1 = firebaseDatabase.getReference("CompletedDates/"+ value.getTheDate() + "/" + value.getKey());
                        databaseReference1.setValue(value);

                        completed = true;
                    }
                } catch (Exception e) {
                    Log.i("--All", "Error from Adding Dates: " + e.getMessage());
                }

                if (!completed) {
                    MyApplication.allDates.add(value);
                    MyApplication.dateHashMap.put(dataSnapshot.getKey(), value);

                    if (value.getTheDate().equals(MyApplication.currentUser.getId())){
                        MyApplication.fullMatchesAsDate.add(value);
                        MyApplication.fullMatchesAsDateHashMap.put(dataSnapshot.getKey(), value);
                    }

                    if (value.getPoster().equals(MyApplication.currentUser.getId()) && !value.getTheDate().equals("NA")){
                        MyApplication.fullMatchesAsCreator.add(value);
                        MyApplication.fullMatchesAsCreatorHashMap.put(dataSnapshot.getKey(), value);
                        //MyApplication.myDate = value;
                        //MyApplication.myDateKey = dataSnapshot.getKey();
                    }

                    if (value.getPoster().equals(MyApplication.currentUser.getId()) && value.getTheDate().equals("NA")){
                        MyApplication.pendingDates.add(value);
                        MyApplication.pendingDatesHashMap.put(value.getKey(), value);
                    }
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TheDate theDate = dataSnapshot.getValue(TheDate.class);
                theDate.setKey(dataSnapshot.getKey());

                for (int i=0; i<MyApplication.allDates.size(); i++){
                    if (MyApplication.allDates.get(i).getKey().equals(dataSnapshot.getKey())){
                        MyApplication.allDates.set(i,theDate);

                    }
                }

                if (theDate.getTheDate().equals(MyApplication.currentUser.getId()) && MyApplication.dateHashMap.get(dataSnapshot.getKey()).getTheDate().equals("NA")){

                }

                boolean found = false;
                for (int i=0; i<MyApplication.fullMatchesAsDate.size(); i++){
                    if (MyApplication.fullMatchesAsDate.get(i).getKey().equals(dataSnapshot.getKey()) && theDate.getTheDate().equals("NA")){
                        MyApplication.fullMatchesAsDate.remove(i);
                        MyApplication.fullMatchesAsDateHashMap.remove(dataSnapshot.getKey());

                        for (int j=0; j<MyApplication.combinedDates.size(); j++) {
                            if (MyApplication.combinedDates.get(j).getKey().equals(dataSnapshot.getKey())) {
                                MyApplication.combinedDates.remove(j);
                                MyApplication.combinesDatesHashMap.remove(dataSnapshot.getKey());
/*
                                for (int k=0; k<MyApplication.agreedChats.size(); k++){
                                    if (MyApplication.agreedChats.get(k).getDateKey().equals(dataSnapshot.getKey())){
                                        MyApplication.agreedChats.remove(k);
                                    }
                                }*/
                                YourDatesFragment.adapter.notifyDataSetChanged();
                                Log.i("--All", "Removing Match as Date");
                            }
                        }

                        found=true;
                    }
                }

                if (!found && theDate.getTheDate().equals(MyApplication.currentUser.getId())){
                    if (MyApplication.dateHashMap.get(dataSnapshot.getKey()).getTheDate().equals("NA")){
                        MyApplication.fullMatchesAsDate.add(theDate);
                        MyApplication.fullMatchesAsDateHashMap.put(dataSnapshot.getKey(), theDate);
                        MyApplication.combinedDates.add(theDate);
                        MyApplication.combinesDatesHashMap.put(dataSnapshot.getKey(), theDate);
                        YourDatesFragment.adapter.notifyDataSetChanged();
                        Log.i("--All", "Adding Match as Date");
                    }
                }

                for (int i=0; i<MyApplication.fullMatchesAsCreator.size();i++){
                    if (MyApplication.fullMatchesAsCreator.get(i).getKey().equals(dataSnapshot.getKey()) && theDate.getTheDate().equals("NA")){
                        MyApplication.fullMatchesAsCreator.remove(i);
                        MyApplication.fullMatchesAsCreatorHashMap.remove(dataSnapshot.getKey());
                        MyApplication.pendingDates.add(theDate);
                        MyApplication.pendingDatesHashMap.put(dataSnapshot.getKey(), theDate);
                        Log.i("--All", "Full Match to Pending: " + theDate.getDateTitle());
                    }
                }

                for (int i=0; i<MyApplication.pendingDates.size(); i++){
                    if (MyApplication.pendingDates.get(i).getKey().equals(dataSnapshot.getKey()) && !theDate.getTheDate().equals("NA")){
                        MyApplication.pendingDates.remove(i);
                        MyApplication.pendingDatesHashMap.remove(dataSnapshot.getKey());
                        MyApplication.fullMatchesAsCreator.add(theDate);
                        MyApplication.fullMatchesAsCreatorHashMap.put(dataSnapshot.getKey(), theDate);
                        Log.i("--All", "Removing from Pending adding to Full Match Creator");
                    }
                }

                MyApplication.dateHashMap.put(dataSnapshot.getKey(), theDate);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TheDate theDate = dataSnapshot.getValue(TheDate.class);



                theDate.setKey(dataSnapshot.getKey());
                for (int i=0;i<MyApplication.allDates.size();i++) {
                    if (dataSnapshot.getKey().equals(MyApplication.allDates.get(i).getKey())) {
                        MyApplication.allDates.remove(i);

                    }
                }

                for (int i = 0; i < MyApplication.combinedDates.size(); i++) {
                    if (MyApplication.combinedDates.get(i).getKey().equals(dataSnapshot.getKey())) {
                        MyApplication.combinedDates.remove(i);
                        YourDatesFragment.adapter.notifyDataSetChanged();
                    }
                }

                for (int i = 0; i < MyApplication.fullMatchesAsCreator.size(); i++) {
                    if (MyApplication.fullMatchesAsCreator.get(i).getKey().equals(dataSnapshot.getKey())) {
                        MyApplication.fullMatchesAsCreator.remove(i);
                    }
                }

                for (int i = 0; i < MyApplication.fullMatchesAsDate.size(); i++) {
                    if (MyApplication.fullMatchesAsDate.get(i).getKey().equals(dataSnapshot.getKey())) {
                        MyApplication.fullMatchesAsDate.remove(i);
                    }
                }

                MyApplication.fullMatchesAsDateHashMap.remove(dataSnapshot.getKey());
                MyApplication.pendingDatesHashMap.remove(dataSnapshot.getKey());
                MyApplication.fullMatchesAsCreatorHashMap.remove(dataSnapshot.getKey());
                MyApplication.combinesDatesHashMap.remove(dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void downloadCompletedDates(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("CompletedDates/"+MyApplication.currentUser.getId());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TheDate completedDate = dataSnapshot.getValue(TheDate.class);

                //MyApplication.allDates.add(completedDate);
                if (completedDate.getPoster().equals(MyApplication.currentUser.getId()) || completedDate.getTheDate().equals(MyApplication.currentUser.getId())) {
                    MyApplication.completedDates.add(completedDate);
                    MyApplication.completedDatesHashMap.put(completedDate.getKey(), completedDate);

                    if (completedDate.isPosterKarma() && completedDate.isTheDateKarma()) {
                        int karma = MyApplication.currentUser.getKarmaPoints();
                        MyApplication.currentUser.setKarmaPoints(karma++);
                        MyApplication.karmaAccounted.put(completedDate.getKey(), true);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TheDate completedDate = dataSnapshot.getValue(TheDate.class);

                if (completedDate.getPoster().equals(MyApplication.currentUser.getId()) || completedDate.getTheDate().equals(MyApplication.currentUser.getId())) {
                    if (completedDate.isPosterKarma() && completedDate.isTheDateKarma()) {
                        int karma = MyApplication.currentUser.getKarmaPoints();
                        MyApplication.currentUser.setKarmaPoints(karma++);
                        MyApplication.karmaAccounted.put(completedDate.getKey(), true);

                        for (int i=0; i<MyApplication.completedDates.size(); i++) {
                            if (MyApplication.completedDates.get(i).getPoster().equals(MyApplication.currentUser.getId()) ||
                                    MyApplication.completedDates.get(i).getTheDate().equals(MyApplication.currentUser.getId())) {
                                MyApplication.completedDates.set(i, completedDate);
                            }
                        }

                        MyApplication.completedDatesHashMap.put(completedDate.getKey(), completedDate);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void downloadUsers(final FirebaseUser firebaseUser){

        Log.i("--All", "Downloading Users");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("--All", "Done Downloading");
                doneDownloading=true;

                if (MyApplication.foundUser) {
                    ((MainActivity) getActivity()).replaceFragments(gllc.tech.dateapp.Profile.class, R.id.container, "Profile");
                } else {
                    try {
                        MyApplication.currentUser = new User(facebookLoginResponseJSONObject.getString("name"), facebookLoginResponseJSONObject.getString("email"),
                                firebaseUser.getUid(), facebookLoginResponseJSONObject.getString("gender"), "https://graph.facebook.com/" +
                                facebookLoginResponseJSONObject.getString("id") + "/picture?type=large", facebookLoginResponseJSONObject.getString("id"),
                                "Enter Bio Here", "NA", "NA", "NA", "NA", 0);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" +MyApplication.currentUser.getId());
                        myRef.setValue(MyApplication.currentUser);

                    } catch (JSONException e) {
                        Log.i("--All", "Unable to Parse New Facebook User");
                    }

                    ((MainActivity) getActivity()).replaceFragments(gllc.tech.dateapp.Profile.class, R.id.container, "Profile");
                }

                //downloadAgreedChats();
                downloadDates();
                downloadCompletedDates();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User downloadUser = dataSnapshot.getValue(User.class);
                MyApplication.allUsers.add(downloadUser);
                MyApplication.userHashMap.put(downloadUser.getId(), downloadUser);
                if (downloadUser.getId().equals(firebaseUser.getUid())) {
                    MyApplication.currentUser = downloadUser;
                    MyApplication.foundUser=true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                for (int i=0;i<MyApplication.allUsers.size();i++) {
                    if (MyApplication.allUsers.get(i).getId().equals(user.getId())) {
                        MyApplication.allUsers.set(i, user);
                        MyApplication.userHashMap.put(user.getId(), user);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
