package gllc.tech.dateapp.UpComingDates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Messages.MessageFragment;
import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class ReviewProfileFragment extends Fragment {

    CircleImageView profilePic;
    TextView name;
    TextView shortBio;
    Button chat, remove;

    //public static User reviewPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.justPosted = false;
        //MyApplication.otherPersonHolder = MyApplication.otherPerson;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_review, container, false);

        profilePic = (CircleImageView) view.findViewById(R.id.profileImage);
        name = (TextView)view.findViewById(R.id.nameProfileReview);
        shortBio = (TextView)view.findViewById(R.id.shortBioProfileReview);
        chat = (Button)view.findViewById(R.id.chatButton);
        remove = (Button)view.findViewById(R.id.removeButton);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(profilePic);
        shortBio.setText(MyApplication.otherPerson.getBio());
        name.setText(MyApplication.otherPerson.getName());

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).changeToolbarText("Chat");
                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.profileReview, "ReviewProfile");
                AgreedChats agreedChats = new AgreedChats(MyApplication.currentUser.getId(), MyApplication.otherPerson.getId(), MyApplication.dateSelectedKey);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + MyApplication.dateSelectedKey);
                databaseReference.setValue(agreedChats);

                databaseReference = database.getReference("AgreedChats/" + MyApplication.otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                databaseReference.setValue(agreedChats);

                MyApplication.justPosted = true;
                MyApplication.agreedChats.add(agreedChats);


            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Requests/" + MyApplication.dateSelectedKey + "/" + MyApplication.otherPerson.getId());
                databaseReference.removeValue();

                ((MainActivity)getActivity()).popBackStack();

            }
        });
    }
}
