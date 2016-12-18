package gllc.tech.dateapp.SearchDate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.Objects.User;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/4/2016.
 */

public class SearchDatesFragment extends Fragment {

    public static ListView listView;
    public static SearchDatesAdapter adapter;
    CircleImageView imageView;
    TextView name, shortBioSearch, dateTitle;
    User viewUser;
    int dateCounter;
    ImageView yes,no;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateCounter = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_search_dates, container, false);

        imageView = (CircleImageView)view.findViewById(R.id.imageViewSearch);
        name = (TextView)view.findViewById(R.id.nameTextView);
        dateTitle = (TextView)view.findViewById(R.id.dateTitle);
        shortBioSearch = (TextView)view.findViewById(R.id.shortBioSearch);
        yes = (ImageView) view.findViewById(R.id.yesImageView);
        no = (ImageView) view.findViewById(R.id.noImageView);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        downloadMatches();
        yes.setImageResource(R.drawable.yes);
        no.setImageResource(R.drawable.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("Matches/" + MyApplication.currentUser.getId() + "/" + MyApplication.allDates.get(dateCounter).getKey());
                //myRef.setValue(true);

                DatabaseReference myRef2 = database.getReference("Requests/" + MyApplication.allDates.get(dateCounter).getKey() + "/" + MyApplication.currentUser.getId());
                myRef2.setValue(MyApplication.currentUser.getProfilePic());

                dateCounter++;
                showDate();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("Matches/" + MyApplication.currentUser.getId() + "/" + MyApplication.allDates.get(dateCounter).getKey());
                //myRef.setValue(false);
                dateCounter++;
                showDate();
            }
        });

        showDate();

    }

    public void showDate(){

        if (dateCounter < MyApplication.allDates.size() || dateCounter == 0){

            if (!MyApplication.allDates.get(dateCounter).getPoster().equals(MyApplication.currentUser.getId())){
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference("Users/" + MyApplication.allDates.get(dateCounter).getPoster());

                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewUser = dataSnapshot.getValue(User.class);

                        Picasso.with(getContext()).load(viewUser.getProfilePic()).into(imageView);
                        name.setText(viewUser.getName());
                        shortBioSearch.setText(viewUser.getBio());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                adapter = new SearchDatesAdapter(MyApplication.allDates.get(dateCounter).getEvents(), getContext());

                dateTitle.setText(MyApplication.allDates.get(dateCounter).getDateTitle());

                listView = (ListView) getActivity().findViewById(R.id.newSearchDatesListview);
                listView.setAdapter(adapter);
            }

            else {
                dateCounter++;
                showDate();
            }
        }

        else {
            Toast.makeText(getContext(), "No More Dates", Toast.LENGTH_LONG).show();

            imageView.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            shortBioSearch.setVisibility(View.INVISIBLE);
            dateTitle.setVisibility(View.INVISIBLE);
            listView = (ListView) getActivity().findViewById(R.id.newSearchDatesListview);
            listView.setAdapter(null);
        }


    }



    public void downloadMatches(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Matches/"+MyApplication.currentUser.getId());

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyApplication.matchMap.put(dataSnapshot.getKey(), (Boolean)dataSnapshot.getValue());

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