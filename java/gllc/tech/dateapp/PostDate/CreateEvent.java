package gllc.tech.dateapp.PostDate;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jaredrummler.materialspinner.MaterialSpinner;

import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/11/2016.
 */

public class CreateEvent extends Fragment {

    FlatButton clickNext;
    MaterialSpinner chooseAcitivty;
    EditText other, specificEditText;
    String activity, specific, address, startTime, endTime;
    TextView header, subheader, getStartTime, getEndTime;
    public static TextView addressTextView;
    ProgressBar progressBar;
    LinearLayout specificLayout, endTimeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event2, container, false);

        activity="";
        specific ="";
        address="";
        startTime="";
        endTime="";

        endTimeLayout = (LinearLayout)view.findViewById(R.id.endTimeLayout);
        specificLayout = (LinearLayout)view.findViewById(R.id.specificLayout);
        chooseAcitivty = (MaterialSpinner) view.findViewById(R.id.chooseActivity);
        clickNext = (FlatButton)view.findViewById(R.id.nextActivity);
        other = (EditText)view.findViewById(R.id.otherField);
        specificEditText = (EditText)view.findViewById(R.id.enterLocation);
        addressTextView = (TextView) view.findViewById(R.id.enterAddress);
        header = (TextView)view.findViewById(R.id.headerText);
        subheader = (TextView)view.findViewById(R.id.subheaderText);
        getEndTime = (TextView)view.findViewById(R.id.endTime);
        getStartTime = (TextView)view.findViewById(R.id.startTime);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //final String[] items = new String[]{"Walk", "Hike", "Movie", "Other"};
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        //adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        //chooseAcitivty.setAdapter(adapter);

        chooseAcitivty.setItems("Choose Activity", "Bowling", "Coffee", "Concert", "Dinner", "Event", "Lunch", "Go-Kart", "Minigolf", "Music Festival",
                "Hike", "Movie", "Walk", "Other");

        chooseAcitivty.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                activity = chooseAcitivty.getText().toString();

                if (activity.equals("Other")) {
                    other.setVisibility(View.VISIBLE);

                    header.setText("First Choose Your Activity");
                    subheader.setText("(e.g. Hike, Movie, Bowling, Walk, Coffee)");

                    specificEditText.setVisibility(View.INVISIBLE);
                    specificEditText.setText("");

                    progressBar.setProgress(0);
                } else if (activity.equals("Choose Activity")) {
                    specificEditText.setVisibility(View.INVISIBLE);
                    specificEditText.setText("");

                    header.setText("First Choose Your Activity");
                    subheader.setText("(e.g. Hike, Movie, Bowling, Walk, Coffee)");
                } if (activity.equals("Bowling")) {
                    other.setVisibility(View.INVISIBLE);

                    new AlertDialog.Builder(getContext())
                            .setTitle("Bowling")
                            .setMessage("Do you know where you want to go Bowling?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })


                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                    if (!activity.equals("Choose Activity")) {
                        specificEditText.setVisibility(View.VISIBLE);
                        header.setText("What's The Specific Activity?");
                        subheader.setText("(e.g. Mission Peak, Finding Dory, Golfland, Xmas in the Park");
                        progressBar.setProgress(20);
                    }


/*
                if (!activity.equals("Other")) {
                    other.setVisibility(View.INVISIBLE);

                    if (!activity.equals("Choose Activity")) {
                        specificEditText.setVisibility(View.VISIBLE);
                        header.setText("What's The Specific Activity?");
                        subheader.setText("(e.g. Mission Peak, Finding Dory, Golfland, Xmas in the Park");
                        progressBar.setProgress(20);
                    }
                }
                */
            }
        });

        clickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.equals("") && !activity.equals("Choose Activity")){
                    if (activity.equals("Other")) {
                        activity = other.getText().toString();

                        specificEditText.setVisibility(View.VISIBLE);
                        header.setText("What's The Specific Activity?");
                        subheader.setText("(e.g. Mission Peak, Finding Dory, Golfland, Xmas in the Park");
                        progressBar.setProgress(20);
                    }
                    /*
                    specificEditText.setVisibility(View.VISIBLE);
                    header.setText("What's The Specific Activity?");
                    subheader.setText("(e.g. Mission Peak, Finding Dory, Golfland, Xmas in the Park");
                    progressBar.setProgress(20);
*/

                }

                if (!activity.equals("") && !specificEditText.getText().toString().equals("")){
                    specific = specificEditText.getText().toString();

                    addressTextView.setVisibility(View.VISIBLE);

                    header.setText("Choose The Location");
                    subheader.setText("Type in the Name of the Place in the Search Bar");

                    addressTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent =
                                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                                .build(getActivity());
                                getActivity().startActivityForResult(intent, MyApplication.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                            } catch (GooglePlayServicesRepairableException e) {
                                // TODO: Handle the error.
                            } catch (GooglePlayServicesNotAvailableException e) {
                                // TODO: Handle the error.
                            }
                        }
                    });

                    specificLayout.setBackgroundColor(Color.parseColor("#31413f"));
                    progressBar.setProgress(40);
                }

                if (!activity.equals("") && !specificEditText.getText().toString().equals("") && !addressTextView.getText().toString().equals("Click To Get Address")){

                    header.setText("Enter Start Time");
                    subheader.setVisibility(View.INVISIBLE);

                    getStartTime.setVisibility(View.VISIBLE);
                    progressBar.setProgress(60);
                }

                if (!activity.equals("") && !specificEditText.getText().toString().equals("") && !addressTextView.getText().toString().equals("") &&
                        !startTime.equals("") && !endTime.equals("")) {

                    Place place = MyApplication.placeChosen;

                    double latitude;
                    double longitude;
                    String placeName;

                    if (place.getAddress().toString().contains(place.getName())) {
                        placeName = "Address Only";
                    } else {
                        placeName = MyApplication.placeChosen.getName().toString();
                    }

                    address = MyApplication.placeChosen.getAddress().toString().replaceFirst(",",".\n");

                    latitude = MyApplication.placeChosen.getLatLng().latitude;
                    longitude = MyApplication.placeChosen.getLatLng().longitude;

                    PostDateFragment.listOfEvents.add(new EventsOfDate(specific, activity, address, startTime, endTime, placeName, latitude, longitude, "NA"));
                    PostDateFragment.listOfPlaces.add(MyApplication.placeChosen);

                    PostDateFragment.adapter.notifyDataSetChanged();
                    PostDateFragment.titleDate.setVisibility(View.VISIBLE);

                    subheader.setText("");
                    subheader.setVisibility(View.VISIBLE);

                    ((MainActivity)getActivity()).popBackStack();


                }
            }
        });

        getStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >=13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            }
                            else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }

                        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE");

                        getStartTime.setText( "Start Time: " + hourOfDay + ":" + mm_precede + minute + AM_PM);
                        startTime = hourOfDay + ":"  + mm_precede + minute + AM_PM;

                        getEndTime.setVisibility(View.VISIBLE);
                        endTimeLayout.setBackgroundColor(Color.parseColor("#31413f"));
                        progressBar.setProgress(80);

                        header.setText("Select End Time");
                    }
                }, 12, 0, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }

        });

        getEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >=13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            }
                            else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }

                        getEndTime.setText( "End Time: " + hourOfDay + ":" + mm_precede + minute + AM_PM);
                        endTime = hourOfDay + ":"  + mm_precede + minute + AM_PM;

                        header.setText("Done! Click Add Event.");

                        clickNext.setText("Add Event!");
                        header.setText("Now Review Everything and Click Add Event!");
                        progressBar.setProgress(100);
                        //separatorBelowEndTime.setVisibility(View.VISIBLE);
                    }
                }, 12, 0, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });

    }
}
