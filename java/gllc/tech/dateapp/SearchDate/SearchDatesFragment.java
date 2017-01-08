
package gllc.tech.dateapp.SearchDate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Build;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.GetDistance;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.Automation.SwipeDetector;
import gllc.tech.dateapp.Loading.Filters;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/4/2016.
 */

public class SearchDatesFragment extends Fragment {

    public static ListView listView;
    public static SearchDatesAdapter adapter;
    CircleImageView imageView;
    TextView name, shortBioSearch, dateTitle, karmaPoints, whoseTreat;
    User viewUser;
    public static int dateCounter;
    ImageView yes,no;
    SwipeDetector swipeDetector;

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
        yes = (ImageView) view.findViewById(R.id.yesImageView);
        no = (ImageView) view.findViewById(R.id.noImageView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        searchAlready();

        yes.setImageResource(R.drawable.yes);
        no.setImageResource(R.drawable.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateCounter < MyApplication.allDates.size() || dateCounter == 0) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" + MyApplication.allDates.get(dateCounter).getKey());
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
                    DatabaseReference myRef = database.getReference("RequestedDate/" + MyApplication.currentUser.getId() + "/" + MyApplication.allDates.get(dateCounter).getKey());
                    myRef.setValue(false);
                    dateCounter++;
                    showDate();
                }
            }
        });
    }

    public void showDate(){

        if (dateCounter < MyApplication.allDates.size() && MyApplication.allDates.size() > 0){

            //Toast.makeText(getContext(), "Distance is: " + getDistance(MyApplication.allDates.get(dateCounter).getEvents()) + " miles away", Toast.LENGTH_SHORT).show();

            if (!MyApplication.matchMap.containsKey(MyApplication.allDates.get(dateCounter).getKey())) {
                if (getDistance(MyApplication.allDates.get(dateCounter).getEvents()) < MyApplication.currentUser.getDistance()) {
                    if ((MyApplication.currentUser.isShowMen() && MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getGender().equals("male") ||
                            (MyApplication.currentUser.isShowWomen() && MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getGender().equals("female")))) {
                        if (!MyApplication.allDates.get(dateCounter).getPoster().equals(MyApplication.currentUser.getId())) {
                            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                            DatabaseReference myRef2 = database2.getReference("Users/" + MyApplication.allDates.get(dateCounter).getPoster());

                            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    viewUser = dataSnapshot.getValue(User.class);

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
                                    shortBioSearch.setText(viewUser.getBio());
                                    karmaPoints.setText("Karma Points: " + viewUser.getKarmaPoints());
                                    whoseTreat.setText(MyApplication.allDates.get(dateCounter).getWhoseTreat());

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
/*
                                        ImageView imageView1 = new ImageView(getContext());
                                        Picasso.with(getContext()).load(viewUser.getPhoto1()).resize((MyApplication.screenWidth-150),
                                                MyApplication.screenWidth-150).centerCrop().into(imageView1);


                                        layout.addView(imageView1);

                                        ImageView imageView2 = new ImageView(getContext());
                                        Picasso.with(getContext()).load(viewUser.getPhoto2()).resize((MyApplication.screenWidth-100),
                                                MyApplication.screenWidth-100).centerCrop().into(imageView2);

                                        layout.addView(imageView2);
*/
                                            dialog.show();

                                        /*
                                        final Dialog dialog = new Dialog(getContext());
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialog.setContentView(R.layout.full_image);

                                        final ImageView imageView = (ImageView)dialog.findViewById(R.id.popupFullImage);

                                        Picasso.with(getContext()).load(viewUser.getProfilePic()).into(imageView);

                                        imageView.setOnTouchListener(swipeDetector);

                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (swipeDetector.swipeDetected()) {
                                                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                                                        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE");


                                                    }
                                                }
                                            }
                                        });

                                        dialog.show();
                                        */
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            adapter = new SearchDatesAdapter(MyApplication.allDates.get(dateCounter).getEvents(), getContext());

                            dateTitle.setText(MyApplication.allDates.get(dateCounter).getDateTitle());

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
        }

        else {
            Toast.makeText(getContext(), "No More Dates", Toast.LENGTH_LONG).show();

            imageView.setVisibility(View.INVISIBLE);
            karmaPoints.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            shortBioSearch.setVisibility(View.INVISIBLE);
            dateTitle.setVisibility(View.INVISIBLE);
            whoseTreat.setVisibility(View.INVISIBLE);
            listView = (ListView) getActivity().findViewById(R.id.newSearchDatesListview);
            listView.setAdapter(null);
        }
    }


    public void searchAlready(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RequestedDate/"+MyApplication.currentUser.getId());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE3333");
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

    public Double getDistance(ArrayList<EventsOfDate> eventsOfDate) {

        ArrayList<EventsOfDate> allEvents = eventsOfDate;

        ArrayList<LatLng> points = new ArrayList<>();

        for (int i=0; i<allEvents.size(); i++) {
            LatLng latLng = new LatLng(allEvents.get(i).getLatitude(), allEvents.get(i).getLongitude());
            points.add(latLng);
        }

        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        LatLng center = new LatLng(latitude/n, longitude/n);

        Location locationA = new Location("point A");
        locationA.setLatitude(center.latitude);
        locationA.setLongitude(center.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(MyApplication.latitude);
        locationB.setLongitude(MyApplication.longitude);
        Float floatDistance = locationA.distanceTo(locationB) ;
        Double doubleDistance = floatDistance*0.000621371;

        return doubleDistance;
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