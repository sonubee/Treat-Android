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
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by bhangoo on 12/14/2016.
 */

public class ChooseFBPhoto extends Fragment {

    ImageView imageView;
    Button selectButton;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_screen_image, container, false);

        imageView = (ImageView)view.findViewById(R.id.fullScreenImage);
        selectButton = (Button)view.findViewById(R.id.selectPhotoButton);

        Picasso.with(getContext()).load(MyApplication.selectedImageUrl).into(imageView);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


                if (Profile.photoToReplace == 1) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + MyApplication.currentUser.getId() + "/photo1");
                    databaseReference.setValue(MyApplication.selectedImageUrl);
                    editor.putString("photo1", MyApplication.selectedImageUrl);
                    editor.apply();
                }
                if (Profile.photoToReplace == 2) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + MyApplication.currentUser.getId() + "/photo2");
                    databaseReference.setValue(MyApplication.selectedImageUrl);
                    editor.putString("photo2", MyApplication.selectedImageUrl);
                    editor.apply();
                }
                if (Profile.photoToReplace == 3) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + MyApplication.currentUser.getId() + "/photo3");
                    databaseReference.setValue(MyApplication.selectedImageUrl);
                    editor.putString("photo3", MyApplication.selectedImageUrl);
                    editor.apply();
                }
                if (Profile.photoToReplace == 4) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + MyApplication.currentUser.getId() + "/photo4");
                    databaseReference.setValue(MyApplication.selectedImageUrl);
                    editor.putString("photo4", MyApplication.selectedImageUrl);
                    editor.apply();
                }

                ((MainActivity)getActivity()).popAllFragments();
                ((MainActivity)getActivity()).changePhotos();
            }
        });
    }
}
