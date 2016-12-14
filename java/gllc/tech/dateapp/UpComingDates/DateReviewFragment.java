package gllc.tech.dateapp.UpComingDates;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MapsActivity;
import gllc.tech.dateapp.Messages.MessageFragment;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/5/2016.
 */

public class DateReviewFragment extends Fragment{

    ListView listView;
    DateReviewAdapter adapter;
    TextView textView, requestsOrMatch;
    LinearLayout layout;
    CircleImageView matchImage;
    HorizontalScrollView horizontalScrollView;

    //public static String requestSelectedToReview;
    public static ArrayList<String> profileUrl = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.cameFromDateReview = true;
        Log.i("--All", "Set cameFromDateReview to true");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_review, container, false);

        textView = (TextView)view.findViewById(R.id.showDate);
        requestsOrMatch = (TextView)view.findViewById(R.id.requests);
        matchImage = (CircleImageView)view.findViewById(R.id.matchPhoto);
        layout = (LinearLayout) view.findViewById(R.id.requestsLinear);
        horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.requestsScroll);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        textView.setText(MyApplication.dateSelected.getDateOfDate());

        adapter = new DateReviewAdapter(getContext(), MyApplication.dateSelected.getEvents());



        listView = (ListView) getActivity().findViewById(R.id.dateReviewListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.cameFromYourDates=true;
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        if (MyApplication.dateSelected.getTheDate().equals("NA")){
            populateRequests();
        }

        else {
           setupDate();
        }
    }

    public void setupDate() {
        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE66666");
        requestsOrMatch.setText("Your Date!");

        for (int  i =0; i<MyApplication.allUsers.size(); i++){
            if ((MyApplication.allUsers.get(i).getId().equals(MyApplication.dateSelected.getTheDate())) && !(MyApplication.dateSelected.getTheDate().equals(MyApplication.currentUser.getId()))){
                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE234234" + MyApplication.allUsers.get(i).getProfilePic());
                MyApplication.otherPerson = MyApplication.allUsers.get(i);

                Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(matchImage);
                horizontalScrollView.setVisibility(View.GONE);
                matchImage.setVisibility(View.VISIBLE);

                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE345345" + MyApplication.otherPerson.getProfilePic());
                matchImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                        MyApplication.otherPerson = ((MainActivity)getActivity()).geteUser();
                        Log.i("--All", "Set cameFromDateReview to true");
                        MyApplication.cameFromDateReview = true;
                        ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.dateReview, "DateReview");
                    }
                });
            }
            else if ((MyApplication.allUsers.get(i).getId().equals(MyApplication.dateSelected.getPoster())) && !(MyApplication.dateSelected.getPoster().equals(MyApplication.currentUser.getId()))){
                MyApplication.otherPerson = MyApplication.allUsers.get(i);
                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);

                Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(matchImage);
                horizontalScrollView.setVisibility(View.INVISIBLE);
                matchImage.setVisibility(View.VISIBLE);

                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);

                matchImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.cameFromDateReview = true;
                        Log.i("--All", "Set cameFromDateReview to true");
                        MyApplication.otherPerson = ((MainActivity)getActivity()).geteUser();
                        ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.dateReview, "DateReview");
                    }
                });
            }
        }
    }

    public void populateRequests(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Requests/" + MyApplication.dateSelectedKey);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //ImageView imageView = new ImageView(getContext());
                CircleImageView imageView = new CircleImageView(getContext());
                profileUrl.add(dataSnapshot.getValue().toString());
                Picasso.with(getContext()).load(dataSnapshot.getValue().toString()).into(imageView);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                imageView.setTag(dataSnapshot.getKey());

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //requestSelectedToReview = v.getTag().toString();
                        for (int i=0; i <MyApplication.allUsers.size(); i++){
                            if (MyApplication.allUsers.get(i).getId().equals(v.getTag().toString())){
                                MyApplication.otherPerson = MyApplication.allUsers.get(i);
                            }
                        }

                        profileUrl = new ArrayList<String>();
                        ((MainActivity)getActivity()).addFragments(ReviewProfileFragment.class, R.id.dateReview, "DateReview");
                    }
                });

                layout.addView(imageView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
       /*
                for (int i=0; i<profileUrl.size(); i++){

                    Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE" + dataSnapshot.getValue().toString());

                    Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE" + profileUrl.get(i));
                    if (profileUrl.get(i).equals(dataSnapshot.getValue().toString())){
                        profileUrl.remove(i);
                        layout.removeViewAt(i);
                    }
                    else {


                        ImageView imageView = new ImageView(getContext());
                        Picasso.with(getContext()).load(profileUrl.get(i)).into(imageView);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                        imageView.setTag(dataSnapshot.getKey());

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestSelectedToReview = v.getTag().toString();
                                ((MainActivity)getActivity()).addFragments(ReviewProfileFragment.class, R.id.dateReview);
                            }
                        });

                        layout.addView(imageView);
                    }
                }
                */
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
            inflater.inflate(R.menu.date_review, menu);
        } else {
            inflater.inflate(R.menu.date_review_as_date, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.unmatchReviewDate:

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Dates/" + MyApplication.dateSelectedKey + "/" + "theDate");
                databaseReference.setValue("NA");

                databaseReference = database.getReference("Requests/" + MyApplication.dateSelectedKey + "/" + MyApplication.otherPerson.getId());
                databaseReference.removeValue();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = firebaseDatabase.getReference("AgreedChats/"+MyApplication.currentUser.getId()+"/"+MyApplication.dateSelectedKey);
                databaseReference1.removeValue();
                databaseReference1 = firebaseDatabase.getReference("AgreedChats/"+MyApplication.otherPerson.getId()+"/"+MyApplication.dateSelectedKey);
                databaseReference1.removeValue();

                if (MyApplication.dateSelected.getPoster().equals(MyApplication.currentUser.getId())) {
                    requestsOrMatch.setText("Your Requests");
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    matchImage.setVisibility(View.INVISIBLE);

                    MyApplication.dateSelected.setTheDate("NA");

                    for (int i=0; i<MyApplication.allDates.size(); i++){
                        if (MyApplication.allDates.get(i).getKey().equals(MyApplication.dateSelectedKey)){
                            MyApplication.allDates.set(i,MyApplication.dateSelected);
                            MyApplication.dateHashMap.put(MyApplication.dateSelectedKey, MyApplication.dateSelected);
                        }
                    }

                    for (int i=0; i<MyApplication.fullMatchesAsCreator.size(); i++){
                        if (MyApplication.fullMatchesAsCreator.get(i).getKey().equals(MyApplication.dateSelectedKey)){
                            MyApplication.fullMatchesAsCreator.remove(i);
                            MyApplication.fullMatchesAsCreatorHashMap.remove(MyApplication.dateSelectedKey);
                            MyApplication.pendingDates.add(MyApplication.dateSelected);
                            MyApplication.pendingDatesHashMap.put(MyApplication.dateSelectedKey, MyApplication.dateSelected);
                        }
                    }

                    for (int i=0; i<MyApplication.combinedDates.size(); i++){
                        if (MyApplication.combinedDates.get(i).getKey().equals(MyApplication.dateSelectedKey)){
                            MyApplication.combinedDates.set(i,MyApplication.dateSelected);
                            MyApplication.combinesDatesHashMap.put(MyApplication.dateSelectedKey, MyApplication.dateSelected);
                        }
                    }

                    MyApplication.otherPerson = null;
                    populateRequests();

                    for (int i=0; i<MyApplication.agreedChats.size(); i++){
                        if (MyApplication.agreedChats.get(i).getDateKey().equals(MyApplication.dateSelectedKey)){
                            MyApplication.agreedChats.remove(i);
                        }
                    }
                }

                else{
/*
                    for (int i=0; i<MyApplication.combinedDates.size(); i++){
                        if (MyApplication.combinedDates.get(i).getKey().equals(MyApplication.dateSelectedKey)){
                            MyApplication.combinedDates.remove(i);
                            MyApplication.combinesDatesHashMap.remove(MyApplication.dateSelectedKey);
                        }
                    }

                    YourDatesFragment.adapter.notifyDataSetChanged();*/
                    ((MainActivity)getActivity()).popBackStack();

                }


                break;
        }

        switch (item.getItemId()) {
            case R.id.cancelDate:
                for (int i = 0; i < MyApplication.combinedDates.size(); i++){
                    if (MyApplication.combinedDates.get(i).getKey().equals(MyApplication.dateSelectedKey)){
                        MyApplication.combinedDates.remove(i);
                        MyApplication.combinesDatesHashMap.remove(MyApplication.dateSelectedKey);
                        YourDatesFragment.adapter.notifyDataSetChanged();
                        ((MainActivity)getActivity()).popBackStack();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Dates/" + MyApplication.dateSelectedKey);
                        databaseReference.removeValue();
                    }
                }

                for (int i=0; i<MyApplication.agreedChats.size(); i++){
                    if (MyApplication.agreedChats.get(i).getDateKey().equals(MyApplication.dateSelectedKey)){
                        MyApplication.agreedChats.remove(i);
                    }

                    String dateId = MyApplication.dateHashMap.get(MyApplication.dateSelectedKey).getTheDate();
                    MyApplication.otherPerson = MyApplication.userHashMap.get(dateId);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference1 = firebaseDatabase.getReference("AgreedChats/"+MyApplication.currentUser.getId()+"/"+MyApplication.dateSelectedKey);
                    databaseReference1.removeValue();
                    databaseReference1 = firebaseDatabase.getReference("AgreedChats/"+MyApplication.otherPerson.getId()+"/"+MyApplication.dateSelectedKey);
                    databaseReference1.removeValue();
                }
                break;
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyApplication.dateSelected = null;
        MyApplication.dateSelectedKey="";
        MyApplication.cameFromDateReview = false;
        MyApplication.cameFromYourDates=false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
