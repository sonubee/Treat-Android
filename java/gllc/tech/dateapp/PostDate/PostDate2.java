package gllc.tech.dateapp.PostDate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gllc.tech.dateapp.Automation.MapsActivity;
import gllc.tech.dateapp.UpComingDates.DisplayBothDates;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/10/2017.
 */

public class PostDate2 extends Fragment implements View.OnClickListener{

    public static ArrayList<EventsOfDate> listOfEvents;
    public static ArrayList<Place> listOfPlaces;
    String theDateString ="Enter Date", titleOfEvent ="", whoseTreat = "";
    FlatButton postDate;
    public static EditText titleDate;
    public static int selectedMap;
    TextView enterDate;
    CoordinatorLayout coordinatorLayout;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    LinearLayout enterInfoLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //listOfEvents.clear();
        //listOfPlaces.clear();

        listOfPlaces = new ArrayList<>();
        listOfEvents = new ArrayList<>();

        setHasOptionsMenu(true);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        setDateTimeField();

        datePickerDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Toast.makeText(getContext(), "When is the date?", Toast.LENGTH_LONG).show();

            }
        }, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_date2, container, false);

        enterDate = (TextView) view.findViewById(R.id.enterDate);
        titleDate = (EditText)view.findViewById(R.id.titleDate);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinatorLayout);
        enterInfoLayout = (LinearLayout)view.findViewById(R.id.postDateTopLayout);
        postDate = (FlatButton)view.findViewById(R.id.postDateButton);
        enterDate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        enterDate.setText("Date: " + theDateString);



        displayItems();



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

    public void displayItems() {



        for (int i=0; i < listOfEvents.size(); i++) {

            //LinearLayout bottomHalf;
            //bottomHalf = new LinearLayout(getContext());
            //bottomHalf.setOrientation(LinearLayout.VERTICAL);

            RelativeLayout eventAdapterLayout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.event_adapter3, null, false);

            ((TextView) eventAdapterLayout.findViewById(R.id.eventTitleEventAdapter)).setText(listOfEvents.get(i).getActivity());
            ((TextView) eventAdapterLayout.findViewById(R.id.addressEventAdapter)).setText(listOfEvents.get(i).getCity());
            ((TextView) eventAdapterLayout.findViewById(R.id.addressEventAdapter)).setText(listOfEvents.get(i).getActivity() + " at " + listOfEvents.get(i).getSpecific());
            ((TextView) eventAdapterLayout.findViewById(R.id.timeEventAdapter)).setText(listOfEvents.get(i).getBeginTime() + " - " + listOfEvents.get(i).getEndTime());
            Picasso.with(getContext()).load(listOfEvents.get(i).getPhoto()).into(((ImageView) eventAdapterLayout.findViewById(R.id.imageEventAdapter)));

            eventAdapterLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("cameFrom", "PostDate");
                    startActivity(intent);
                }
            });

            eventAdapterLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.click_anim));

                    return false;
                }
            });

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 10);
            eventAdapterLayout.setLayoutParams(lp);



            //bottomHalf.addView(eventAdapterLayout);

            enterInfoLayout.addView(eventAdapterLayout);
        }

        if (listOfEvents.size() > 0) {
            postDate.setVisibility(View.VISIBLE);
        }
    }

    public void askTreat() {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Finally, Whose Treat?");
        //builderSingle.setMessage("Will Anyone Cover the Date?");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);

        arrayAdapter.add("My Treat");
        arrayAdapter.add("Your Treat");
        arrayAdapter.add("Split Bill");
        arrayAdapter.add("Decide Later");
        arrayAdapter.add("What's this?");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (which == 4) {
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
                        whoseTreat = "Split Bill";
                    } else if (which == 3) {
                        whoseTreat = "Decide Later";
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
                            ((MainActivity)getActivity()).replaceFragments(DisplayBothDates.class, R.id.container, "DisplayBothDates", null);
                            Toast.makeText(getContext(), "Posted! Here Are Your Upcoming Dates", Toast.LENGTH_SHORT).show();
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

                ((MainActivity)getActivity()).addFragments(ChooseActivityPostDate.class, R.id.container, "ChooseActivity", null);
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

                /*
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Now Click the (+) Button to Add Event(s)", Snackbar.LENGTH_LONG);

                View snackbarView = snackbar.getView();
                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.BLACK);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.red_EC1C24));

                snackbarView.setBackgroundColor(Color.CYAN);
                snackbar.show();
*/
                ((MainActivity)getActivity()).addFragments(ChooseActivityPostDate.class, R.id.container, "ChooseActivity", null);


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("Enter The Date");
    }

    @Override
    public void onClick(View v) {
        datePickerDialog.show();

    }
}
