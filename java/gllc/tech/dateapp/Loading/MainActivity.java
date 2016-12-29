package gllc.tech.dateapp.Loading;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.CompletedDates.CompletedDates;
import gllc.tech.dateapp.Messages.MessageAdapter;
import gllc.tech.dateapp.Messages.ShowAllMessages;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.PostDate.CreateEvent;
import gllc.tech.dateapp.PostDate.PostDateFragment;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.SearchDate.SearchDatesFragment;
import gllc.tech.dateapp.UpComingDates.DateReviewFragment;
import gllc.tech.dateapp.UpComingDates.YourDatesFragment;

public class MainActivity extends NavigationLiveo implements OnItemClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private HelpLiveo mHelpLiveo;
    User saveUser;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    private GoogleApiClient mGoogleApiClient;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    public void onInt(Bundle savedInstanceState) {

        materialDesignSetup();

        FacebookSdk.sdkInitialize(getApplicationContext());

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .build();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        MyApplication.screenHeight = metrics.heightPixels;
        MyApplication.screenWidth = metrics.widthPixels;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MyApplication.ACCESS_FINE_LOCATION_VALUE);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MyApplication.ACCESS_FINE_LOCATION_VALUE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        MyApplication.hasDate = false;
        MyApplication.otherPerson = null;
        MyApplication.dateSelected = null;
        MyApplication.dateSelectedKey = "";

        if(MyApplication.visitedMessages){
            MessageAdapter.myRef.removeEventListener(MessageAdapter.childEventListener);
        }

        switch (position) {

            case 0:
                getSupportActionBar().setTitle("Login");
                mFragment = new Login();
                break;

            case 1:
                getSupportActionBar().setTitle("Create The Date");
                mFragment = new PostDateFragment();
                break;

            case 2:
                getSupportActionBar().setTitle("Search Dates");
                mFragment = new SearchDatesFragment();
                break;

            case 3:
                getSupportActionBar().setTitle("Your Dates");
                mFragment = new YourDatesFragment();
                break;

            case 4:
                getSupportActionBar().setTitle("Completed Dates");
                mFragment = new CompletedDates();
                break;

            case 5:
                getSupportActionBar().setTitle("All Messages");
                mFragment = new ShowAllMessages();
                break;

            case 6:
                LoginManager.getInstance().logOut();
                getSupportActionBar().setTitle("Login");
                mFragment = new Login();
                break;

            default:
                getSupportActionBar().setTitle("Login");
                mFragment = new Login();
                break;
        }

        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.container, mFragment).commit();

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    public void materialDesignSetup() {
        //this.userName.setText("Check Out Other Features Below!");
        //this.userEmail.setText("Or Keep Chatting!");

        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add("Profile", R.drawable.profile);
        mHelpLiveo.add("Post Date", R.drawable.event);
        mHelpLiveo.add("Search Dates", R.drawable.search);
        mHelpLiveo.add("Upcoming Dates", R.drawable.upcoming);
        mHelpLiveo.add("Completed Dates", R.drawable.upcoming);
        mHelpLiveo.add("Messages", R.drawable.message);
        mHelpLiveo.add("Logout", R.drawable.logout);

//        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        //mHelpLiveo.add(getString(R.string.starred), R.mipmap.ic_star_black_24dp);
        //mHelpLiveo.add(getString(R.string.sent_mail), R.drawable.bracelet2);
        //mHelpLiveo.add(getString(R.string.trash), R.mipmap.ic_add_white_24dp);
        //mHelpLiveo.add(getString(R.string.drafts), R.drawable.feedback);
        //mHelpLiveo.add("About", R.drawable.about);
//        mHelpLiveo.addSeparator(); // Item separator

        with(this, Navigation.THEME_DARK).
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                        //.footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    public void addFragments(Class fragmentClass, int id, String tag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //fragment = getSupportFragmentManager().findFragmentById(R.id.fullScreenImageLayout);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(id, fragment, tag).addToBackStack(null)
                .commit();
        //getSupportFragmentManager().executePendingTransactions();
    }

    public void popAllFragments(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void replaceFragments(Class fragmentClass, int id, String tag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(id, fragment, tag)
                .commit();
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
        Log.i("--All", "Popping Backstack");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.i("--All", "Options in activity called");
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.i("--All", "OptionsSelected in activity called");
        switch (item.getItemId()) {
            case R.id.selectDate:
                MyApplication.dateSelected.setTheDate(MyApplication.otherPerson.getId());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Dates/" +MyApplication.dateSelectedKey);
                myRef.setValue(MyApplication.dateSelected);
                Log.i("--All", "Selected Date");
                myRef = database.getReference("FullMatches/" + MyApplication.otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                myRef.setValue(MyApplication.dateSelected);

                myRef = database.getReference("FullMatches/" + MyApplication.currentUser.getId() + "/" + MyApplication.dateSelectedKey);
                myRef.setValue(MyApplication.dateSelected);

                myRef = database.getReference("Requests/" + MyApplication.dateSelectedKey + "/" + MyApplication.otherPerson.getId());
                myRef.removeValue();

                new SendPush(MyApplication.currentUser.getName() + " has selected you at the date for " + MyApplication.dateSelected.getDateTitle() + "!",
                        MyApplication.otherPerson.getPushToken(), "You got a date!");

                //Fragment dateReview = getSupportFragmentManager().findFragmentById(R.id.dateReview);
                //Fragment reviewProfile = getSupportFragmentManager().findFragmentById(R.id.profileReview);
                //Fragment messaging = getSupportFragmentManager().findFragmentById(R.id.messaging);
                //Fragment dateReview = getSupportFragmentManager().findFragmentByTag("YourDates");
                //Fragment reviewProfile = getSupportFragmentManager().findFragmentByTag("DateReview");
                //Fragment messaging = getSupportFragmentManager().findFragmentByTag("ReviewProfile");
                //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                FragmentManager manager = getSupportFragmentManager();
                //Fragment fragment = manager.findFragmentById(R.id.yourDates);
                Fragment fragment = manager.findFragmentByTag("DatesReview");
                ((DateReviewFragment) fragment).setupDate();
/*
                MyApplication.otherPersonHolder = MyApplication.otherPerson;
                MyApplication.dateSelectedHolder = MyApplication.dateSelected;

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                MyApplication.dateSelected = MyApplication.dateSelectedHolder;
                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE4444"+MyApplication.dateSelected.getDateOfDate());
                addFragments(DateReviewFragment.class, R.id.yourDates, "YourDates");
                MyApplication.otherPerson = MyApplication.otherPersonHolder;
                addFragments(ReviewProfileFragment.class, R.id.dateReview, "DateReview");

                addFragments(MessageFragment.class, R.id.profileReview, "ReviewProfile");



                //fragmentTransaction.remove(dateReview);
                //fragmentTransaction.remove(reviewProfile);
                //fragmentTransaction.detach(messaging);



                //fragmentTransaction.add(dateReview, "DateReview");
                //fragmentTransaction.add(reviewProfile, "ReviewProfile");
                //fragmentTransaction.add(messaging, "Messaging");

                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE111"+MyApplication.otherPerson.getProfilePic());
                //addFragments(MessageFragment.class, R.id.profileReview, "ReviewProfile");
                //fragmentTransaction.commit();*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveUser(User user){
        saveUser = user;
    }

    public User geteUser(){
        return saveUser;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }

                    // Right to left swipe action
                    else
                    {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Login.callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("--All", "Place: " + place.getName());
                MyApplication.placeChosen = place;
                if (place.getAddress().toString().contains(place.getName())) {
                    CreateEvent.addressTextView.setText(place.getAddress().toString().replaceFirst(",","\n"));
                } else {
                    CreateEvent.addressTextView.setText(place.getName() + System.getProperty("line.separator") + place.getAddress().toString().replaceFirst(",",".\n"));
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("--All", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void refreshDateReview() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("DatesReview");
        ((DateReviewFragment) fragment).decideDate();
    }

    public void changePhotos() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("Profile");
        ((Profile) fragment).reloadProfileFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MyApplication.ACCESS_FINE_LOCATION_VALUE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location mLastLocation;

        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE22");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE3333");
            if (mLastLocation != null) {
                String latitude = String.valueOf(mLastLocation.getLatitude());
                String longitude = String.valueOf(mLastLocation.getLongitude());

                Toast.makeText(this, "Latitude " + latitude + " - Longitude: " + longitude, Toast.LENGTH_LONG).show();
                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE"+longitude);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

