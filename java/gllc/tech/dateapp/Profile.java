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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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



    }


}
