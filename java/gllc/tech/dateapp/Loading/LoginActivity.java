package gllc.tech.dateapp.Loading;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

/**
 * Created by bhangoo on 1/29/2017.
 */

public class LoginActivity extends Activity {

    public static CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    JSONObject facebookLoginResponseJSONObject;
    String refreshedToken;
    public static ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        MyApplication.allDates.clear();
        MyApplication.combinedDates.clear();
        MyApplication.completedDates.clear();
        MyApplication.fullMatchesAsCreator.clear();
        MyApplication.fullMatchesAsDate.clear();
        MyApplication.pendingDates.clear();
        MyApplication.categories.clear();
    }
}
