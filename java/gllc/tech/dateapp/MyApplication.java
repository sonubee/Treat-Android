package gllc.tech.dateapp;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.location.places.Place;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.Objects.User;

/**
 * Created by bhangoo on 12/2/2016.
 */
public class MyApplication extends Application {

    public static User currentUser;
    public static ArrayList<TheDate> allDates = new ArrayList<>();
    //public static ArrayList<String> allDatesKeys = new ArrayList<>();
    //public static ArrayList<String> fullMatchesAsDateKeys = new ArrayList<>();
    public static ArrayList<TheDate> fullMatchesAsDate = new ArrayList<>();
    public static ArrayList<TheDate> fullMatchesAsCreator = new ArrayList<>();
    //public static ArrayList<String> fullMatchesKeyAsCreator = new ArrayList<>();
    public static ArrayList<TheDate> pendingDates = new ArrayList<>();
    //public static ArrayList<String> pendingDatesKeys = new ArrayList<>();
    public static HashMap<String, Boolean> matchMap = new HashMap<>();
    public static ArrayList<User> allUsers = new ArrayList<>();
    //public static ArrayList<AgreedChats> agreedChats = new ArrayList<>();
    public static TheDate dateSelected = new TheDate();
    public static User otherPerson = new User();
    public static String dateSelectedKey = "";
    public static TheDate myDate = new TheDate();
    public static String myDateKey = "";
    public static ArrayList<TheDate> combinedDates = new ArrayList<>();
    //public static ArrayList<String> combinedDatesKeys = new ArrayList<>();
    public static boolean justPosted = false;
    public static boolean visitedMessages=false;
    public static HashMap<String, TheDate> dateHashMap = new HashMap<>();
    public static HashMap<String, TheDate> pendingDatesHashMap = new HashMap<>();
    public static HashMap<String, TheDate> fullMatchesAsCreatorHashMap = new HashMap<>();
    public static HashMap<String, TheDate> fullMatchesAsDateHashMap = new HashMap<>();
    public static HashMap<String, TheDate> combinesDatesHashMap = new HashMap<>();
    public static boolean cameFromDateReview = false;
    public static boolean refreshingFragments = false;
    //public static User otherPersonHolder = null;
    public static TheDate dateSelectedHolder = null;
    public static HashMap<String, User> userHashMap = new HashMap<>();
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static Place placeChosen;
    public static boolean cameFromPost = false;
    public static boolean cameFromYourDates = false;
    public static String selectedImageUrl = "";
    public static boolean hasDate = false;
    public static ArrayList<TheDate> completedDates = new ArrayList<>();
    public static HashMap<String, Boolean> karmaAccounted = new HashMap<>();
    public static boolean foundUser = false;

    @Override
    public void onCreate() {
        super.onCreate();

        currentUser = new User("Name", "Email", "FID", "Gender", "Profile Pic", "ID", "Bio", "NA", "NA", "NA", "NA", 0);

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
