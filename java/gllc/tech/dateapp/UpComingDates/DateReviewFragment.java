package gllc.tech.dateapp.UpComingDates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.Messages.MessageFragment;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.PostDate.EventAdapter;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/5/2016.
 */

public class DateReviewFragment extends Fragment{

    ListView listView;
    EventAdapter adapter;
    TextView textView, requestsOrMatch;
    public static LinearLayout layout;
    CircleImageView matchImage;
    HorizontalScrollView horizontalScrollView;
    ChildEventListener childEventListener;
    DatabaseReference populateRequestsReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<String> allRequests = new ArrayList<>();
    TheDate dateSelected;
    String dateKey;

    //public static String requestSelectedToReview;
    public static ArrayList<String> profileUrl = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        dateKey = getArguments().getString("dateSelectedKey");
        dateSelected = MyApplication.dateHashMap.get(dateKey);

        setHasOptionsMenu(true);

        textView.setText("Date: " + dateSelected.getDateOfDate());

        adapter = new EventAdapter(getContext(), dateSelected.getEvents());

        listView = (ListView) getActivity().findViewById(R.id.dateReviewListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("cameFrom", "DateReview");
                startActivity(intent);
            }
        });

        decideDate();
    }

    public void decideDate() {
        if (dateSelected.getTheDate().equals("NA")){populateRequests();}
        else {setupDate();}
    }

    public void setupDate() {
        requestsOrMatch.setText("Your Date!");

        if (!MyApplication.userHashMap.get(dateSelected.getTheDate()).getId().equals(MyApplication.currentUser.getId())) {
            MyApplication.otherPerson = MyApplication.userHashMap.get(dateSelected.getTheDate());
        } else {MyApplication.otherPerson = MyApplication.userHashMap.get(dateSelected.getPoster());}

        Picasso.with(getContext()).load(MyApplication.otherPerson.getProfilePic()).into(matchImage);
        horizontalScrollView.setVisibility(View.INVISIBLE);
        matchImage.setVisibility(View.VISIBLE);

        matchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cameFrom", "DateReview");
                bundle.putString("otherPerson", MyApplication.otherPerson.getId());
                bundle.putString("dateSelectedKey", dateKey);
                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.container, "MessageFragment", bundle);
            }
        });
    }

    public void populateRequests(){

        requestsOrMatch.setText("No Requests Yet!");

        populateRequestsReference = firebaseDatabase.getReference("Requests/" + dateKey);

        populateRequestsReference.addChildEventListener(childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                allRequests.add(dataSnapshot.getKey());

                if (!dataSnapshot.getValue().equals("Rejected")) {

                    CircleImageView imageView = new CircleImageView(getContext());
                    profileUrl.add(dataSnapshot.getValue().toString());
                    Picasso.with(getContext()).load(dataSnapshot.getValue().toString()).into(imageView);

                    imageView.setTag(dataSnapshot.getKey());

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MyApplication.otherPerson = MyApplication.userHashMap.get(v.getTag().toString());

                            Bundle bundle = new Bundle();
                            bundle.putString("cameFrom", "DateReview");
                            bundle.putString("dateSelectedKey", dateKey);
                            bundle.putString("otherPerson", MyApplication.otherPerson.getId());

                            populateRequestsReference.removeEventListener(childEventListener);
                            profileUrl = new ArrayList<String>();
                            ((MainActivity)getActivity()).addFragments(ReviewProfileFragment.class, R.id.container, "ReviewProfileFragment", bundle);
                        }
                    });

                    requestsOrMatch.setText("Possible Dates");




                    layout.addView(imageView);
                }

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

        if (dateSelected.getPoster().equals(MyApplication.currentUser.getId()) && !dateSelected.getTheDate().equals("NA")) {
            inflater.inflate(R.menu.date_review, menu);
        } else if (dateSelected.getTheDate().equals("NA") && MyApplication.currentUser.getId().equals(dateSelected.getPoster())) {
            inflater.inflate(R.menu.date_review_cancel_only, menu);
        } else {
            inflater.inflate(R.menu.date_review_as_date, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.unmatchReviewDate:

                databaseReference = firebaseDatabase.getReference("Dates/" + dateKey + "/" + "theDate");
                databaseReference.setValue("NA");

                databaseReference = firebaseDatabase.getReference("Requests/" + dateKey + "/" + MyApplication.otherPerson.getId());
                databaseReference.removeValue();

                databaseReference = firebaseDatabase.getReference("AgreedChats/"+MyApplication.currentUser.getId()+"/"+dateKey);
                databaseReference.removeValue();

                databaseReference = firebaseDatabase.getReference("AgreedChats/"+MyApplication.otherPerson.getId()+"/"+dateKey);
                databaseReference.removeValue();

                if (dateSelected.getPoster().equals(MyApplication.currentUser.getId())) {
                    requestsOrMatch.setText("Your Requests");
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    matchImage.setVisibility(View.INVISIBLE);

                    dateSelected.setTheDate("NA");

                    for (int i=0; i<MyApplication.allDates.size(); i++){
                        if (MyApplication.allDates.get(i).getKey().equals(dateKey)){
                            MyApplication.allDates.set(i,dateSelected);
                            MyApplication.dateHashMap.put(dateKey, dateSelected);
                        }
                    }

                    for (int i=0; i<MyApplication.fullMatchesAsCreator.size(); i++){
                        if (MyApplication.fullMatchesAsCreator.get(i).getKey().equals(dateKey)){
                            MyApplication.fullMatchesAsCreator.remove(i);
                            MyApplication.fullMatchesAsCreatorHashMap.remove(dateKey);
                            MyApplication.pendingDates.add(dateSelected);
                            MyApplication.pendingDatesHashMap.put(dateKey, dateSelected);
                        }
                    }

                    for (int i=0; i<MyApplication.combinedDates.size(); i++){
                        if (MyApplication.combinedDates.get(i).getKey().equals(dateKey)){
                            MyApplication.combinedDates.set(i,dateSelected);
                            MyApplication.combinesDatesHashMap.put(dateKey, dateSelected);
                        }
                    }

                    MyApplication.otherPerson = null;
                    populateRequests();
                }

                else{((MainActivity)getActivity()).popBackStack();}


                break;
        }

        switch (item.getItemId()) {
            case R.id.cancelDate:

                String dateId = MyApplication.dateHashMap.get(dateKey).getTheDate();

                for (int i=0; i <allRequests.size(); i++) {
                    databaseReference = firebaseDatabase.getReference("RequestedDate/"+allRequests.get(i)+"/"+dateKey);
                    databaseReference.removeValue();
                }

                if (!dateId.equals("NA")) {

                    MyApplication.otherPerson = MyApplication.userHashMap.get(dateId);

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + dateKey);
                    databaseReference.removeValue();

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.otherPerson.getId() + "/" + dateKey);
                    databaseReference.removeValue();
                } else {
                    for (int i=0; i <allRequests.size(); i++) {
                        databaseReference = firebaseDatabase.getReference("AgreedChats/"+allRequests.get(i)+"/"+dateKey);
                        databaseReference.removeValue();
                    }

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + dateKey);
                    databaseReference.removeValue();
                }

                if (!dateId.equals("NA")) {

                    MyApplication.otherPerson = MyApplication.userHashMap.get(dateId);

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + dateKey);
                    databaseReference.removeValue();

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.otherPerson.getId() + "/" + dateKey);
                    databaseReference.removeValue();
                } else {
                    for (int i=0; i <allRequests.size(); i++) {
                        databaseReference = firebaseDatabase.getReference("AgreedChats/"+allRequests.get(i)+"/"+dateKey);
                        databaseReference.removeValue();
                    }

                    databaseReference = firebaseDatabase.getReference("AgreedChats/" + MyApplication.currentUser.getId() + "/" + dateKey);
                    databaseReference.removeValue();
                }

                databaseReference = firebaseDatabase.getReference("Requests/"+dateKey);
                databaseReference.removeValue();

                databaseReference = firebaseDatabase.getReference("Messages/"+dateKey);
                databaseReference.removeValue();

                for (int i = 0; i < MyApplication.combinedDates.size(); i++){
                    if (MyApplication.combinedDates.get(i).getKey().equals(dateKey)){
                        MyApplication.combinedDates.remove(i);
                        MyApplication.combinesDatesHashMap.remove(dateKey);
                        YourDatesFragment.adapter.notifyDataSetChanged();
                        ((MainActivity)getActivity()).popBackStack();

                        databaseReference = firebaseDatabase.getReference("Dates/" + dateKey);
                        databaseReference.removeValue();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (dateSelected != null) {
            if (dateSelected.getTheDate().equals("NA") && dateSelected.getPoster().equals(MyApplication.currentUser.getId())) {
                populateRequestsReference.removeEventListener(childEventListener);
            }
        }

        dateSelected = null;
        dateKey="";
        allRequests.clear();
        Log.i("--All", "Detach");

    }

}
