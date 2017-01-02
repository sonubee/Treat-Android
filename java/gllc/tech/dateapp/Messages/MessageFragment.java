package gllc.tech.dateapp.Messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.Message;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.UpComingDates.DateReviewFragment;
import gllc.tech.dateapp.UpComingDates.ReviewProfileFragment;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class MessageFragment extends Fragment {

    ImageView sendButton;
    EditText messageToSend;
    CircleImageView youImage, otherImage;
    public static MessageAdapter adapter;
    public static ListView messageListview;
    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    public static String messageKey = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    MenuInflater menuInflater;
    private User otherPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.visitedMessages=true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_screen, container, false);

        otherPerson = MyApplication.userHashMap.get(getArguments().getString("otherPerson"));

        sendButton = (ImageView) view.findViewById(R.id.sendButton);
        messageToSend = (EditText)view.findViewById(R.id.messageToSend);
        youImage = (CircleImageView) view.findViewById(R.id.youImage);
        otherImage = (CircleImageView) view.findViewById(R.id.otherImage);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("Messages/" + otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                DatabaseReference myRef = database.getReference("Messages/" + MyApplication.dateSelectedKey + "/" + messageKey);

                Message sendMessage = new Message(messageToSend.getText().toString(), MyApplication.currentUser.getId(), otherPerson.getId());

                myRef.push().setValue(sendMessage);

                new SendPush(sendMessage.getMessage(), otherPerson.getPushToken(), "Message From " + otherPerson.getName());

                messageToSend.setText("");
            }
        });

        Picasso.with(getContext()).load(MyApplication.currentUser.getProfilePic()).into(youImage);
        Picasso.with(getContext()).load(otherPerson.getProfilePic()).into(otherImage);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("AgreedChats/"+MyApplication.currentUser.getId()+"/"+MyApplication.dateSelectedKey);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AgreedChats agreedChats = dataSnapshot.getValue(AgreedChats.class);
                messageKey = agreedChats.getPoster()+agreedChats.getRequester();

                adapter = new MessageAdapter(getContext(), R.id.listviewMessaging, messageArrayList);

                messageListview = (ListView) getActivity().findViewById(R.id.listviewMessaging);
                messageListview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        otherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cameFrom", "Messaging");
                bundle.putString("otherPerson", otherPerson.getId());

                ((MainActivity)getActivity()).addFragments(ReviewProfileFragment.class, R.id.container, "ReviewProfile", bundle);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menuInflater = inflater;

        menu.clear();
        if (MyApplication.dateSelected.getPoster().equals(MyApplication.currentUser.getId()) && MyApplication.dateSelected.getTheDate().equals("NA")) {
            inflater.inflate(R.menu.select_date, menu);
        } else {
            inflater.inflate(R.menu.empty_menu, menu);
        }
        //Log.i("--All", "Create Menu in Messaging");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.clear();

        if (MyApplication.dateSelected.getPoster().equals(MyApplication.currentUser.getId()) && MyApplication.dateSelected.getTheDate().equals("NA")) {
            menuInflater.inflate(R.menu.select_date, menu);
        } else {
            menuInflater.inflate(R.menu.empty_menu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("--All", "Selected Options Messaging");

        switch (item.getItemId()) {
            case R.id.selectDate:

                MyApplication.dateSelected.setTheDate(otherPerson.getId());

                databaseReference = database.getReference("Dates/" +MyApplication.dateSelectedKey);
                databaseReference.setValue(MyApplication.dateSelected);

                new SendPush(MyApplication.currentUser.getName() + " has selected you at the date for " + MyApplication.dateSelected.getDateTitle() + "!",
                        otherPerson.getPushToken(), "You got a date!");

                if (getArguments().get("cameFrom").equals("ReviewProfile")) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    Fragment fragment = manager.findFragmentByTag("DatesReview");
                    ((DateReviewFragment) fragment).setupDate();
                }

                getActivity().invalidateOptionsMenu();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        MessageAdapter.myRef.removeEventListener(MessageAdapter.childEventListener);
        MessageAdapter.messageArrayList.clear();

        if (!getArguments().getString("cameFrom").equals("DateReview")) {
            otherPerson=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
