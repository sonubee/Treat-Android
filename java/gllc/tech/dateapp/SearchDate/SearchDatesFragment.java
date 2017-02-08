
package gllc.tech.dateapp.SearchDate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.Automation.SimpleCalculations;
import gllc.tech.dateapp.Automation.SwipeDetector;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.PostDate.EventAdapter;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/4/2016.
 */

public class SearchDatesFragment extends Fragment {

    public static ListView listView;
    //public static SearchDatesAdapter adapter;
    EventAdapter adapter;
    CircleImageView imageView;
    TextView name, shortBioSearch, dateTitle, karmaPoints, whoseTreat, distance;
    public static int dateCounter;
    //ImageView yes,no;
    SwipeDetector swipeDetector;
    //RelativeLayout relativeListView;
    LinearLayout searchDatesLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateCounter = 0;
        swipeDetector = new SwipeDetector();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_dates, container, false);

        imageView = (CircleImageView)view.findViewById(R.id.imageViewSearch);
        name = (TextView)view.findViewById(R.id.nameTextView);
        dateTitle = (TextView)view.findViewById(R.id.dateTitle);
        karmaPoints = (TextView)view.findViewById(R.id.karmaPointsSearch);
        shortBioSearch = (TextView)view.findViewById(R.id.shortBioSearch);
        whoseTreat = (TextView)view.findViewById(R.id.whoseTreatSearch);
        //yes = (ImageView) view.findViewById(R.id.yesImageView);
        //no = (ImageView) view.findViewById(R.id.noImageView);
        //relativeListView = (RelativeLayout)view.findViewById(R.id.relativeListViewSearch);
        distance = (TextView)view.findViewById(R.id.distanceFromYou);
        searchDatesLinearLayout = (LinearLayout)view.findViewById(R.id.searchDatesLinearLayout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        searchAlready();
/*
        yes.setImageResource(R.drawable.yes);
        no.setImageResource(R.drawable.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateCounter < MyApplication.allDates.size() || dateCounter == 0) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" +
                            MyApplication.allDates.get(dateCounter).getKey());
                    myRef.setValue(true);

                    DatabaseReference myRef2 = database.getReference("Requests/" + MyApplication.allDates.get(dateCounter).getKey() + "/" + MyApplication.currentUser.getId());
                    myRef2.setValue(MyApplication.currentUser.getProfilePic());

                    new SendPush(MyApplication.currentUser.getName() + " has requested to be your date!",
                            MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getPushToken(), "Date Request");

                    dateCounter++;

                    showDate();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateCounter < MyApplication.allDates.size() || dateCounter == 0) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" +
                            MyApplication.allDates.get(dateCounter).getKey());
                    myRef.setValue(false);
                    dateCounter++;
                    showDate();
                }
            }
        });
        */
    }

    public void showDate(){

        if (dateCounter < MyApplication.allDates.size() && MyApplication.allDates.size() > 0){

            User viewUser = MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster());

            ArrayList<EventsOfDate> eventsOfDates = MyApplication.allDates.get(dateCounter).getEvents();

            boolean matchedCategory = false;
            for (int i=0; i<eventsOfDates.size(); i++) {
                if (MyApplication.categoryPreferencesValue.get(eventsOfDates.get(i).getActivity())) {
                    matchedCategory=true;
                }
            }

            //checking if a category matches
            if (matchedCategory) {

                //checking if treat matches
                if ((MyApplication.currentUser.isWhoseTreatDateTreat() && MyApplication.allDates.get(dateCounter).getWhoseTreat().equals("Date's Treat")) ||
                        (MyApplication.currentUser.isWhoseTreatPosterTreat() && MyApplication.allDates.get(dateCounter).getWhoseTreat().equals("My Treat")) ||
                        (MyApplication.currentUser.isWhoseTreatSplitBill() && MyApplication.allDates.get(dateCounter).getWhoseTreat().equals("Split Bill")) ||
                        (MyApplication.currentUser.isWhoseTreatDecideLater() && MyApplication.allDates.get(dateCounter).getWhoseTreat().equals("Decide Later"))) {

                    //checking if already tried a match
                    if (!MyApplication.matchMap.containsKey(MyApplication.allDates.get(dateCounter).getKey())) {
                        //checking if within distance
                        if ((SimpleCalculations.GetTheDistance(MyApplication.allDates.get(dateCounter).getEvents()) < MyApplication.currentUser.getDistance()) ||
                                MyApplication.currentUser.getId().equals(MyApplication.myId)) {
                            //gender check
                            if ((MyApplication.currentUser.isShowMen() && viewUser.getGender().equals("male") || (MyApplication.currentUser.isShowWomen() &&
                                    viewUser.getGender().equals("female")))) {
                                //age filter
                                if (!viewUser.isGaveFullBirthday() || ((SimpleCalculations.getAge(viewUser) > MyApplication.currentUser.getAgeMin()) &&
                                        SimpleCalculations.getAge(viewUser) < MyApplication.currentUser.getAgeMax())) {
                                    //checking if it's not posted from user
                                    if (!MyApplication.allDates.get(dateCounter).getPoster().equals(MyApplication.currentUser.getId())) {

                                        final ArrayList<String> allImages = new ArrayList<>();

                                        if (viewUser.getPhoto1().equals("NA")) {
                                            allImages.add(viewUser.getProfilePic());
                                        } else {
                                            allImages.add(viewUser.getPhoto1());
                                        }

                                        if (!viewUser.getPhoto2().equals("NA")) {
                                            allImages.add(viewUser.getPhoto2());
                                        }
                                        if (!viewUser.getPhoto3().equals("NA")) {
                                            allImages.add(viewUser.getPhoto3());
                                        }
                                        if (!viewUser.getPhoto4().equals("NA")) {
                                            allImages.add(viewUser.getPhoto4());
                                        }

                                        Picasso.with(getContext()).load(viewUser.getProfilePic()).into(imageView);
                                        name.setText(viewUser.getName());
                                        if (viewUser.isGaveFullBirthday()) {
                                            name.append(", " + SimpleCalculations.getAge(viewUser));
                                        }
                                        shortBioSearch.setText("Bio: " + viewUser.getBio());
                                        karmaPoints.setText("Karma Points: " + viewUser.getKarmaPoints());
                                        whoseTreat.setText(MyApplication.allDates.get(dateCounter).getWhoseTreat());
                                        distance.setText(SimpleCalculations.GetTheDistance(MyApplication.allDates.get(dateCounter).getEvents()) + " Miles from You");
                                        dateTitle.setText(MyApplication.allDates.get(dateCounter).getDateTitle());

                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                final Dialog dialog = new Dialog(getContext());
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.setContentView(R.layout.full_image_horizontal);

                                                LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.fullImageLinear);

                                                //use a GradientDrawable with only one color set, to make it a solid color
                                                GradientDrawable border = new GradientDrawable();
                                                border.setColor(0xFFFFFFFF); //white background
                                                border.setStroke(1, 0xFF000000); //black border with full opacity

                                                layout.setBackground(border);

                                                for (int i = 0; i < allImages.size(); i++) {
                                                    ImageView imageView = new ImageView(getContext());
                                                    imageView.setPadding(10, 10, 10, 10);

                                                    Picasso.with(getContext()).load(allImages.get(i)).resize((MyApplication.screenWidth - 150),
                                                            MyApplication.screenHeight - 250).centerCrop().into(imageView);

                                                    layout.addView(imageView);
                                                }

                                                dialog.show();
                                            }
                                        });

                                        final LinearLayout bottomHalf = new LinearLayout(getContext());
                                        bottomHalf.setOrientation(LinearLayout.VERTICAL);

                                        ArrayList<EventsOfDate> events = MyApplication.allDates.get(dateCounter).getEvents();

                                        for (int i=0; i < events.size(); i++) {
                                            //RelativeLayout tempRelativeLayout = new RelativeLayout(getContext());
                                            RelativeLayout eventAdapterLayout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.event_adapter3, null, false);

                                            if (MyApplication.allDates.get(dateCounter).getEvents().get(i).getActivitySpecificName().equals("")) {
                                                ((TextView) eventAdapterLayout.findViewById(R.id.eventTitleEventAdapter)).setText(MyApplication.allDates.get(dateCounter).getEvents().
                                                        get(i).getActivity());
                                            } else {
                                                ((TextView) eventAdapterLayout.findViewById(R.id.eventTitleEventAdapter)).setText(MyApplication.allDates.get(dateCounter).getEvents().
                                                        get(i).getActivity() + " - " + MyApplication.allDates.get(dateCounter).getEvents().get(i).getActivitySpecificName());
                                            }

                                            ((TextView) eventAdapterLayout.findViewById(R.id.addressEventAdapter)).setText(MyApplication.allDates.get(dateCounter).getEvents().
                                                    get(i).getCity());
                                            ((TextView) eventAdapterLayout.findViewById(R.id.addressEventAdapter)).setText(MyApplication.allDates.get(dateCounter).getEvents().
                                                    get(i).getPlaceName() + " in " + MyApplication.allDates.get(dateCounter).getEvents().get(i).getCity());
                                            ((TextView) eventAdapterLayout.findViewById(R.id.timeEventAdapter)).setText(MyApplication.allDates.get(dateCounter).getEvents().
                                                    get(i).getBeginTime() + " - " + MyApplication.allDates.get(dateCounter).getEvents().get(i).getEndTime());
                                            Picasso.with(getContext()).load(MyApplication.allDates.get(dateCounter).getEvents().get(i).getPhoto()).
                                                    into(((ImageView) eventAdapterLayout.findViewById(R.id.imageEventAdapter)));

                                            eventAdapterLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("cameFrom", "SearchDates");
                                                    startActivity(intent);
                                                }
                                            });

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.setMargins(0, 0, 0, 10);
                                            eventAdapterLayout.setLayoutParams(lp);

                                            bottomHalf.addView(eventAdapterLayout);
                                        }

                                        final RelativeLayout relativeLayout1 = new RelativeLayout(getContext());
                                        relativeLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                        ImageView yesButton = new ImageView(getContext());
                                        yesButton.setImageResource(R.drawable.yes);
                                        yesButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (dateCounter < MyApplication.allDates.size() || dateCounter == 0) {
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" +
                                                            MyApplication.allDates.get(dateCounter).getKey());
                                                    myRef.setValue(true);

                                                    DatabaseReference myRef2 = database.getReference("Requests/" + MyApplication.allDates.get(dateCounter).getKey() + "/" +
                                                            MyApplication.currentUser.getId());
                                                    myRef2.setValue(MyApplication.currentUser.getProfilePic());

                                                    new SendPush(MyApplication.currentUser.getName() + " has requested to be your date!",
                                                            MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getPushToken(), "Date Request");

                                                    bottomHalf.removeAllViews();

                                                    dateCounter++;

                                                    showDate();
                                                }
                                            }
                                        });




                                        ImageView noButton = new ImageView(getContext());
                                        noButton.setImageResource(R.drawable.no);
                                        noButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (dateCounter < MyApplication.allDates.size() || dateCounter == 0) {
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" +
                                                            MyApplication.allDates.get(dateCounter).getKey());
                                                    myRef.setValue(false);

                                                    bottomHalf.removeAllViews();


                                                    dateCounter++;
                                                    showDate();
                                                }
                                            }
                                        });

                                        final float scale = getContext().getResources().getDisplayMetrics().density;
                                        int pixels = (int) (50 * scale + 0.5f);

                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pixels, pixels);
                                        lp.setMargins(200, 30, 0, 0);
                                        yesButton.setLayoutParams(lp);

                                        relativeLayout1.addView(yesButton);

                                        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(pixels, pixels);
                                        lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        lp2.setMargins(0,30,200,0);
                                        noButton.setLayoutParams(lp2);

                                        relativeLayout1.addView(noButton);
                                        bottomHalf.addView(relativeLayout1);

                                        searchDatesLinearLayout.addView(bottomHalf);
/*
                                adapter = new EventAdapter(getContext(),MyApplication.allDates.get(dateCounter).getEvents());

                                listView = (ListView) getActivity().findViewById(R.id.newSearchDatesListview);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                                        intent.putExtra("cameFrom", "SearchDates");
                                        startActivity(intent);
                                    }
                                });

                                */
                                    } else {
                                        dateCounter++;
                                        showDate();
                                    }
                                }
                                else {
                                    dateCounter++;
                                    showDate();
                                }
                            } else {
                                dateCounter++;
                                showDate();
                            }
                        } else {
                            dateCounter++;
                            showDate();
                        }

                    } else {
                        dateCounter++;
                        showDate();
                    }
                } else {
                    dateCounter++;
                    showDate();
                }
            }else {
                dateCounter++;
                showDate();
            }

        }

        else {
            Toast.makeText(getContext(), "No More Dates", Toast.LENGTH_LONG).show();

            imageView.setVisibility(View.INVISIBLE);
            karmaPoints.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            shortBioSearch.setVisibility(View.INVISIBLE);
            dateTitle.setVisibility(View.INVISIBLE);
            whoseTreat.setVisibility(View.INVISIBLE);
            //relativeListView.setVisibility(View.INVISIBLE);
            distance.setVisibility(View.INVISIBLE);
            //listView = (ListView) getActivity().findViewById(R.id.newSearchDatesListview);
            //listView.setAdapter(null);
        }
    }


    public void searchAlready(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RequestedDate/"+MyApplication.currentUser.getId());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showDate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!MyApplication.dateHashMap.containsKey(dataSnapshot.getKey())) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" + dataSnapshot.getKey());
                    databaseReference.removeValue();
                } else {
                    MyApplication.matchMap.put(dataSnapshot.getKey(), (Boolean)dataSnapshot.getValue());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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

        inflater.inflate(R.menu.filter, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chooseFilters:
                ((MainActivity)getActivity()).addFragments(Filters.class, R.id.container, "Filters", null);
                break;
        }
        return true;
    }



}