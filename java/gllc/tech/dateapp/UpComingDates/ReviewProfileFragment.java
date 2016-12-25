package gllc.tech.dateapp.UpComingDates;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SendPush;
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
    TextView shortBio, karmaPoints;
    ImageView chatImage, removeImage;
    ImageView reviewPhoto2,reviewPhoto3,reviewPhoto4;

    //public static User reviewPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MyApplication.justPosted = false;
        //MyApplication.otherPersonHolder = MyApplication.otherPerson;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_review, container, false);

        profilePic = (CircleImageView) view.findViewById(R.id.profileImage);
        reviewPhoto2 = (ImageView) view.findViewById(R.id.reviewPhoto2);
        reviewPhoto3 = (ImageView) view.findViewById(R.id.reviewPhoto3);
        reviewPhoto4 = (ImageView) view.findViewById(R.id.reviewPhoto4);
        name = (TextView)view.findViewById(R.id.nameProfileReview);
        shortBio = (TextView)view.findViewById(R.id.shortBioProfileReview);
        chatImage = (ImageView)view.findViewById(R.id.chatImage);
        removeImage = (ImageView)view.findViewById(R.id.removeImage);
        karmaPoints = (TextView)view.findViewById(R.id.karmaPointsReview);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        karmaPoints.setText(MyApplication.otherPerson.getKarmaPoints() + " Karma Points");

        if (MyApplication.otherPerson.getPhoto1().equals("NA")) {
            Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(profilePic);
        } else {
            Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto1()).into(profilePic);
        }
        if (!MyApplication.otherPerson.getPhoto2().equals("NA")) {
            Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto2()).into(reviewPhoto2);
        }
        if (!MyApplication.otherPerson.getPhoto3().equals("NA")) {
            Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto3()).into(reviewPhoto3);
        }
        if (!MyApplication.otherPerson.getPhoto4().equals("NA")) {
            Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto4()).into(reviewPhoto4);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (MyApplication.otherPerson.getPhoto1().equals("NA")) {
                    Picasso.with(getContext()).load("https://graph.facebook.com/" + MyApplication.otherPerson.getFid() + "/picture?type=large").into(imageView);
                } else {
                    Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto1()).into(imageView);
                }
                dialog.show();
            }
        });

        reviewPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (!MyApplication.otherPerson.getPhoto2().equals("NA")) {
                    Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto2()).into(imageView);
                }
                dialog.show();
            }
        });

        reviewPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (!MyApplication.otherPerson.getPhoto3().equals("NA")) {
                    Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto3()).into(imageView);
                }
                dialog.show();
            }
        });

        reviewPhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (!MyApplication.otherPerson.getPhoto4().equals("NA")) {
                    Picasso.with(getContext()).load(MyApplication.otherPerson.getPhoto4()).into(imageView);
                }
                dialog.show();
            }
        });

        shortBio.setText(MyApplication.otherPerson.getBio());
        name.setText(MyApplication.otherPerson.getName());

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).changeToolbarText("Chat");
                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.container, "ReviewProfile");
                AgreedChats agreedChats = new AgreedChats(MyApplication.currentUser.getId(), MyApplication.otherPerson.getId(), MyApplication.dateSelectedKey);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + MyApplication.dateSelectedKey);
                databaseReference.setValue(agreedChats);

                databaseReference = database.getReference("AgreedChats/" + MyApplication.otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                databaseReference.setValue(agreedChats);

                //MyApplication.justPosted = true;
                new SendPush(MyApplication.otherPerson.getName()+" has opened chat with you!", MyApplication.otherPerson.getPushToken(),
                        "Date: " + MyApplication.dateSelected.getDateTitle());

                //MyApplication.agreedChats.add(agreedChats);


            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Requests/" + MyApplication.dateSelectedKey + "/" + MyApplication.otherPerson.getId());
                databaseReference.removeValue();

                DateReviewFragment.layout.removeAllViews();
                ((MainActivity)getActivity()).refreshDateReview();


                ((MainActivity)getActivity()).popBackStack();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
