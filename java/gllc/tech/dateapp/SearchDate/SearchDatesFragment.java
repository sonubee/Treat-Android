package gllc.tech.dateapp.SearchDate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Automation.SendPush;
import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.Loading.MyApplication;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateCounter = 0;
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

        //showDate();

    }

    public void showDate(){

        if (dateCounter < MyApplication.allDates.size() && MyApplication.allDates.size() > 0){

            if (!MyApplication.matchMap.containsKey(MyApplication.allDates.get(dateCounter).getKey())) {
                if ((MyApplication.currentUser.isShowMen() && MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getGender().equals("male") ||
                        (MyApplication.currentUser.isShowWomen() && MyApplication.userHashMap.get(MyApplication.allDates.get(dateCounter).getPoster()).getGender().equals("female")))) {
                    if (!MyApplication.allDates.get(dateCounter).getPoster().equals(MyApplication.currentUser.getId())) {
                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        DatabaseReference myRef2 = database2.getReference("Users/" + MyApplication.allDates.get(dateCounter).getPoster());

                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                viewUser = dataSnapshot.getValue(User.class);

                                Picasso.with(getContext()).load(viewUser.getProfilePic()).into(imageView);
                                name.setText(viewUser.getName());
                                shortBioSearch.setText(viewUser.getBio());
                                karmaPoints.setText("Karma Points: " + viewUser.getKarmaPoints());
                                whoseTreat.setText(MyApplication.allDates.get(dateCounter).getWhoseTreat());
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
                                MyApplication.cameFromSearchDates = true;
                                Intent intent = new Intent(getActivity(), MapsActivity.class);
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

                    /*
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date strDate = sdf.parse(MyApplication.dateHashMap.get(dataSnapshot.getKey()).getDateOfDate());

                        if (System.currentTimeMillis() > strDate.getTime()) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("RequestedDate/" + MyApplication.currentUser.getId() + " / " + dataSnapshot.getKey());
                            databaseReference.removeValue();
                        }
                    } catch (Exception e) {
                        Log.i("--All", "Error in searchAlready SearchDatesFragment: " + e.getMessage());
                    }
                    */

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


}