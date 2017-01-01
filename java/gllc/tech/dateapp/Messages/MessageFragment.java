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
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.Objects.Message;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.UpComingDates.DateReviewFragment;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class MessageFragment extends Fragment {

    Button sendButton;
    EditText messageToSend;
    CircleImageView youImage, otherImage;
    public static MessageAdapter adapter;
    public static ListView messageListview;
    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    public static String messageKey = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.visitedMessages=true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_screen, container, false);

        sendButton = (Button)view.findViewById(R.id.sendButton);
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
                //DatabaseReference myRef = database.getReference("Messages/" + MyApplication.otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                DatabaseReference myRef = database.getReference("Messages/" + MyApplication.dateSelectedKey + "/" + messageKey);

                Message sendMessage = new Message(messageToSend.getText().toString(), MyApplication.currentUser.getId(), MyApplication.otherPerson.getId());

                myRef.push().setValue(sendMessage);

                new SendPush(sendMessage.getMessage(), MyApplication.otherPerson.getPushToken(), "Message From " + MyApplication.otherPerson.getName());

                messageToSend.setText("");
            }
        });

        Picasso.with(getContext()).load(MyApplication.currentUser.getProfilePic()).into(youImage);
        Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(otherImage);

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

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (MyApplication.dateSelected.getPoster().equals(MyApplication.currentUser.getId())) {
            inflater.inflate(R.menu.select_date, menu);
        } else {
            inflater.inflate(R.menu.empty_menu, menu);
        }
        //Log.i("--All", "Create Menu in Messaging");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("--All", "Selected Options Messaging");

        switch (item.getItemId()) {
            case R.id.selectDate:

                MyApplication.dateSelected.setTheDate(MyApplication.otherPerson.getId());

                databaseReference = database.getReference("Dates/" +MyApplication.dateSelectedKey);
                databaseReference.setValue(MyApplication.dateSelected);

                //databaseReference = database.getReference("FullMatches/" + MyApplication.otherPerson.getId() + "/" + MyApplication.dateSelectedKey);
                //databaseReference.setValue(MyApplication.dateSelected);

                //databaseReference = database.getReference("FullMatches/" + MyApplication.currentUser.getId() + "/" + MyApplication.dateSelectedKey);
                //databaseReference.setValue(MyApplication.dateSelected);


                new SendPush(MyApplication.currentUser.getName() + " has selected you at the date for " + MyApplication.dateSelected.getDateTitle() + "!",
                        MyApplication.otherPerson.getPushToken(), "You got a date!");

                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag("DatesReview");
                ((DateReviewFragment) fragment).setupDate();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        MessageAdapter.myRef.removeEventListener(MessageAdapter.childEventListener);
        MessageAdapter.messageArrayList.clear();

        if (!MyApplication.cameFromDateReview){
            MyApplication.otherPerson=null;
        }

        MyApplication.cameFromDateReview=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
