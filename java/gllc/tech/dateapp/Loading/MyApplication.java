package gllc.tech.dateapp.Loading;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.location.places.Place;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.Categories;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.Objects.User;

/**
 * Created by bhangoo on 12/2/2016.
 */
public class MyApplication extends Application {

    public static User currentUser;
    public static ArrayList<TheDate> allDates = new ArrayList<>();
    public static ArrayList<TheDate> fullMatchesAsDate = new ArrayList<>();
    public static ArrayList<TheDate> fullMatchesAsCreator = new ArrayList<>();
    public static ArrayList<TheDate> pendingDates = new ArrayList<>();
    public static ArrayList<TheDate> combinedDates = new ArrayList<>();
    public static ArrayList<Categories> categories = new ArrayList<>();
    public static HashMap<String, TheDate> dateHashMap = new HashMap<>();
    public static HashMap<String, TheDate> pendingDatesHashMap = new HashMap<>();
    public static HashMap<String, TheDate> fullMatchesAsCreatorHashMap = new HashMap<>();
    public static HashMap<String, TheDate> fullMatchesAsDateHashMap = new HashMap<>();
    public static HashMap<String, TheDate> combinesDatesHashMap = new HashMap<>();
    public static HashMap<String, Boolean> matchMap = new HashMap<>();
    public static HashMap<String, Categories> categoriesMap = new HashMap<>();
    public static TheDate dateSelected = new TheDate();
    public static User otherPerson = new User();
    public static String dateSelectedKey = "";
    public static boolean visitedMessages=false;
    public static HashMap<String, User> userHashMap = new HashMap<>();
    public static Place placeChosen;
    public static String selectedImageUrl = "";
    public static ArrayList<TheDate> completedDates = new ArrayList<>();
    public static HashMap<String, Boolean> karmaAccounted = new HashMap<>();
    public static HashMap<String, TheDate> completedDatesHashMap = new HashMap<>();
    public static boolean foundUser = false;
    public static String pushToken;
    public static int screenHeight;
    public static int screenWidth;
    public static double latitude = 0;
    public static double longitude = 0;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int ACCESS_FINE_LOCATION_VALUE = 9999;
    public static String myId = "ZyOum0RLpLUqSxQ9if8OTjZVD7y1";
    //public static String[] activitiesString = new String[] {"Bowling", "Coffee", "Concert", "Dinner", "Event", "Lunch", "Go-Kart", "MiniGolf", "MusicFestival",
    //        "Hike", "Movie", "Walk", "Other", "FastFood"};
    public static String yelpToken = "";

    @Override
    public void onCreate() {
        super.onCreate();

        pushToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("--All", "Token: " + pushToken);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "gllc.tech.dateapp",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.i("--All", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
