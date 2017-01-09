package gllc.tech.dateapp.UpComingDates;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.Messages.MessageFragment;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.User;
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
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private User otherPerson;
    String dateKey;

    //public static User reviewPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_review2, container, false);

        LinearLayout test = (LinearLayout)view.findViewById(R.id.addToThis);
        LinearLayout second = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.profile_review_details, null);
        test.addView(second);

        String cameFrom = getArguments().getString("cameFrom");
        otherPerson = MyApplication.userHashMap.get(getArguments().getString("otherPerson"));
        dateKey = getArguments().getString("dateSelectedKey");

        profilePic = (CircleImageView) view.findViewById(R.id.profileImage);
        reviewPhoto2 = (ImageView) view.findViewById(R.id.reviewPhoto2);
        reviewPhoto3 = (ImageView) view.findViewById(R.id.reviewPhoto3);
        reviewPhoto4 = (ImageView) view.findViewById(R.id.reviewPhoto4);
        name = (TextView)view.findViewById(R.id.nameProfileReview);
        shortBio = (TextView)view.findViewById(R.id.shortBioProfileReview);
        karmaPoints = (TextView)view.findViewById(R.id.karmaPointsReview);

        RelativeLayout relativeLayout1 = new RelativeLayout(getContext());
        relativeLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        chatImage = new ImageView(getContext());
        chatImage.setImageResource(R.drawable.chat);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(150, 150);
        lp.setMargins(100, 0, 0, 0);
        chatImage.setLayoutParams(lp);

        relativeLayout1.addView(chatImage);

        removeImage = new ImageView(getContext());
        removeImage.setImageResource(R.drawable.no);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(150, 150);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
        lp2.setMargins(0,0,100,0);
        removeImage.setLayoutParams(lp2);

        relativeLayout1.addView(removeImage);

        test.addView(relativeLayout1);



        if (cameFrom.equals("Messaging")) {
            chatImage.setVisibility(View.INVISIBLE);
            removeImage.setVisibility(View.INVISIBLE);

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        karmaPoints.setText(otherPerson.getKarmaPoints() + " Karma Points");

        if (otherPerson.getPhoto1().equals("NA")) {
            Picasso.with(getContext()).load(otherPerson.getProfilePic()).into(profilePic);
        } else {
            Picasso.with(getContext()).load(otherPerson.getPhoto1()).into(profilePic);
        }
        if (!otherPerson.getPhoto2().equals("NA")) {
            Picasso.with(getContext()).load(otherPerson.getPhoto2()).into(reviewPhoto2);
        }
        if (!otherPerson.getPhoto3().equals("NA")) {
            Picasso.with(getContext()).load(otherPerson.getPhoto3()).into(reviewPhoto3);
        }
        if (!otherPerson.getPhoto4().equals("NA")) {
            Picasso.with(getContext()).load(otherPerson.getPhoto4()).into(reviewPhoto4);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.full_image);

                ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);
                if (otherPerson.getPhoto1().equals("NA")) {
                    Picasso.with(getContext()).load("https://graph.facebook.com/" + otherPerson.getFid() + "/picture?type=large").into(imageView);
                } else {
                    Picasso.with(getContext()).load(otherPerson.getPhoto1()).into(imageView);
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
                if (!otherPerson.getPhoto2().equals("NA")) {
                    Picasso.with(getContext()).load(otherPerson.getPhoto2()).into(imageView);
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
                if (!otherPerson.getPhoto3().equals("NA")) {
                    Picasso.with(getContext()).load(otherPerson.getPhoto3()).into(imageView);
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
                if (!otherPerson.getPhoto4().equals("NA")) {
                    Picasso.with(getContext()).load(otherPerson.getPhoto4()).into(imageView);
                }
                dialog.show();
            }
        });

        shortBio.setText(otherPerson.getBio());
        name.setText(otherPerson.getName());

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).changeToolbarText("Chat");
                //((MainActivity)getActivity()).saveUser(otherPerson);

                Bundle bundle = new Bundle();
                bundle.putString("cameFrom", "ReviewProfile");
                bundle.putString("otherPerson", otherPerson.getId());

                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.container, "Messaging", bundle);
                AgreedChats agreedChats = new AgreedChats(MyApplication.currentUser.getId(), otherPerson.getId(), dateKey);

                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE"+MyApplication.currentUser.getId());

                databaseReference = database.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + dateKey);
                databaseReference.setValue(agreedChats);

                databaseReference = database.getReference("AgreedChats/" + otherPerson.getId() + "/" + dateKey);
                databaseReference.setValue(agreedChats);

                new SendPush(otherPerson.getName()+" has opened chat with you!", otherPerson.getPushToken(),
                        "Date: " + MyApplication.dateHashMap.get(dateKey).getDateTitle());


            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference = database.getReference("Requests/" + dateKey + "/" + otherPerson.getId());
                databaseReference.setValue("Rejected");

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
