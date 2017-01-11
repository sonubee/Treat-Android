package gllc.tech.dateapp.PostDate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gllc.tech.dateapp.DisplayBothDates;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.Loading.MyApplication;
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
    String theDateString ="Enter Date", titleOfEvent ="", whoseTreat = "";
    FlatButton postDate;
    public static ListView listView;
    public static EventAdapter adapter;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    public static EditText titleDate;
    public static int selectedMap;
    RadioButton myTreat, yourTreat, noTreat;
    TextView noEvents, enterDate;
    LinearLayout enterDateLayout;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        setDateTimeField();

        datePickerDialog.show();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_date, container, false);

        enterDateLayout = (LinearLayout)view.findViewById(R.id.enterDateLayout);
        listView = (ListView) view.findViewById(R.id.eventListView);
        postDate = (FlatButton)view.findViewById(R.id.postDate);
        enterDate = (TextView) view.findViewById(R.id.enterDate);
        titleDate = (EditText)view.findViewById(R.id.titleDate);
        yourTreat = (RadioButton)view.findViewById(R.id.yourTreat);
        myTreat = (RadioButton)view.findViewById(R.id.myTreat);
        noTreat = (RadioButton)view.findViewById(R.id.noTreat);
        noEvents = (TextView)view.findViewById(R.id.noEvents);

        enterDateLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAdapter();

        enterDate.setText(theDateString);

        postDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listOfEvents.size() < 1) {
                    Toast.makeText(getContext(), "Please Click (+) Sign at Top to Add and Event First", Toast.LENGTH_LONG).show();
                }

                else if (titleOfEvent.equals("") && titleDate.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    final EditText edittext = new EditText(getContext());
                    alert.setMessage("Make a Short Title for your Date");
                    alert.setTitle("Date Title");

                    alert.setView(edittext);

                    alert.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.)

                            titleOfEvent = edittext.getText().toString();

                            if (titleOfEvent.equals("")) {
                                Toast.makeText(getContext(), "Please Enter a Short Title", Toast.LENGTH_LONG).show();
                            } else if (titleOfEvent.length() < 10 || titleOfEvent.length() > 50) {
                                Toast.makeText(getContext(), "Please enter at Least 10 Characters, up to 50", Toast.LENGTH_LONG).show();
                                titleOfEvent = "";
                            } else {
                                titleDate.setVisibility(View.VISIBLE);
                                titleDate.setText(titleOfEvent);

                                askTreat();
                            }
                        }
                    });

                    alert.show();
                } else {
                    askTreat();
                }
            }
        });
    }

    public void askTreat() {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Finally, Whose Treat?");
        //builderSingle.setMessage("Will Anyone Cover the Date?");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);

        arrayAdapter.add("My Treat");
        arrayAdapter.add("Your Treat");
        arrayAdapter.add("NA");
        arrayAdapter.add("What's this?");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (which == 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Whose Treat");
                    alert.setMessage("Date Creators can choose who will cover the date. If you cover the date, you can get twice " +
                            "as many Karma Points. Choose \"NA\" if it's a free date or both pay");

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askTreat();
                        }
                    });

                    alert.show();

                } else {

                    if (which == 0) {
                        whoseTreat = "My Treat";
                    } else if (which == 1) {
                        whoseTreat = "Your Treat";
                    } else if (which == 2) {
                        whoseTreat = "NA";
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String currentDateandTime = sdf.format(new Date());

                    String titleToUse;

                    if (titleOfEvent.equals("")) {
                        titleToUse = titleDate.getText().toString();
                    } else {
                        titleToUse = titleOfEvent;
                    }

                    TheDate newDate = new TheDate(MyApplication.currentUser.getId(), "NA", currentDateandTime, theDateString, titleToUse, "NA",
                            listOfEvents, false, false, whoseTreat);


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Dates");
                    myRef.push().setValue(newDate);

                    listOfPlaces = new ArrayList<Place>();
                    listOfEvents = new ArrayList<>();

                    final ProgressDialog pleaseWait;
                    pleaseWait = new ProgressDialog(getContext());
                    pleaseWait.setMessage("Please Wait");
                    pleaseWait.setCancelable(false);
                    pleaseWait.setInverseBackgroundForced(false);
                    pleaseWait.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            ((MainActivity)getActivity()).replaceFragments(DisplayBothDates.class, R.id.container, "DisplayBothDates");
                            Toast.makeText(getContext(), "Posted! Here Are Your Upcoming Dates", Toast.LENGTH_LONG).show();
                            pleaseWait.hide();
                        }
                    }, 1000);



                }

            }
        });
        builderSingle.show();
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

                ((MainActivity)getActivity()).addFragments(CreateEvent3.class, R.id.container, "CreateEvent", null);
        }

        return true;
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                enterDate.setText("Date: " + dateFormatter.format(newDate.getTime()));
                theDateString = dateFormatter.format(newDate.getTime());


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("Enter The Date");
    }



    public void setupAdapter () {

        adapter = new EventAdapter(getContext(), listOfEvents);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMap = position;
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("cameFrom", "PostDate");
                startActivity(intent);

            }
        });


        if (listOfEvents.size() == 0) {
            listView.setVisibility(View.INVISIBLE);
        } else {
            noEvents.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
       datePickerDialog.show();
    }
}
