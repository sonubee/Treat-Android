package gllc.tech.dateapp.PostDate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MapsActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.UpComingDates.YourDatesFragment;

/**
 * Created by bhangoo on 12/1/2016.
 */
public class PostDateFragment extends Fragment  implements View.OnClickListener {

    public static ArrayList<EventsOfDate> listOfEvents = new ArrayList<>();
    public static ArrayList<Place> listOfPlaces = new ArrayList<>();
    String theDateString ="";
    Button postDate;
    public static ListView listView;
    public static EventAdapter adapter;
    private EditText enterDate;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    public static EditText titleDate;
    public static TextView header;
    public static int selectedMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_date, container, false);
        postDate = (Button)view.findViewById(R.id.postDate);

        enterDate = (EditText) view.findViewById(R.id.enterDate);
        titleDate = (EditText)view.findViewById(R.id.titleDate);
        header = (TextView)view.findViewById(R.id.headerDate);
        enterDate.setInputType(InputType.TYPE_NULL);
        enterDate.requestFocus();
        setDateTimeField();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAdapter();

        postDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (theDateString.equals("") && titleDate.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter the Date and a Title", Toast.LENGTH_LONG).show();
                } else if (theDateString.equals("")) {
                    Toast.makeText(getContext(), "Enter the Date", Toast.LENGTH_LONG).show();
                } else if (titleDate.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter a Title", Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String currentDateandTime = sdf.format(new Date());

                    TheDate newDate = new TheDate(MyApplication.currentUser.getId(), "NA", currentDateandTime, theDateString, titleDate.getText().toString(), "NA", listOfEvents);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Dates");
                    myRef.push().setValue(newDate);

                    listOfPlaces = new ArrayList<Place>();
                    listOfEvents = new ArrayList<>();

                    ((MainActivity)getActivity()).replaceFragments(PostDateFragment.class, R.id.container);
                    Toast.makeText(getContext(), "Posted! Check Your Upcoming Dates", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case R.id.addEvent:

                ((MainActivity)getActivity()).addFragments(CreateEvent.class, R.id.container, "PostDate");
        }

        return true;
    }

    private void setDateTimeField() {
        enterDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                enterDate.setText(dateFormatter.format(newDate.getTime()));
                theDateString = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }



    public void setupAdapter () {

        adapter = new EventAdapter(getContext(), listOfEvents);

        listView = (ListView) getActivity().findViewById(R.id.eventListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMap = position;
                MyApplication.cameFromPost=true;
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View v) {
       datePickerDialog.show();
    }
}
