package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bhangoo on 12/31/2016.
 */

public class TestProfile extends Fragment{

    CircleImageView profileImage;
    ImageView photo2, photo3, photo4;
    ImageView editBio, editPhoto1, editPhoto2, editPhoto3, editPhoto4;
    TextView name, bio, karmaPoints, ageRange;
    EditText enterBio;
    boolean editingBio = false;
    public static ArrayList<String> albumIds = new ArrayList<>();
    public static ArrayList<String> albumNames = new ArrayList<>();
    public static ArrayList<String> coverPhotosArray = new ArrayList<>();
    public static HashMap<String,String> albumIdToCoverPhoto = new HashMap<>();
    public static HashMap<String,String> albumIdToLink = new HashMap<>();
    public static int photoToReplace=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile, container, false);

        LinearLayout test = (LinearLayout)view.findViewById(R.id.mainLinearLayout);
        LinearLayout second = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.about_you, null);
        test.addView(second);

        return view;
    }
}
