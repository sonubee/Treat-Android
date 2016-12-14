package gllc.tech.dateapp.PostDate;

import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/11/2016.
 */

public class CreateEvent extends Fragment {

    Button clickNext;
    Spinner chooseAcitivty;
    EditText other, specificEditText;
    String activity, specific, address, startTime, endTime;
    TextView header, subheader, getStartTime, getEndTime, specificTextView, activityTextView;
    public static TextView addressTextView;
    ProgressBar progressBar;
    View separatorBelowOther, separatorBelowSpecific, separatorBelowAddress, separatorBelowEndTime;
    ImageView editActivity, editSpecific;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);

        activity="";
        specific ="";
        address="";
        startTime="";
        endTime="";

        chooseAcitivty = (Spinner) view.findViewById(R.id.chooseActivity);
        clickNext = (Button)view.findViewById(R.id.nextActivity);
        other = (EditText)view.findViewById(R.id.otherField);

        specificEditText = (EditText)view.findViewById(R.id.enterLocation);
        addressTextView = (TextView) view.findViewById(R.id.enterAddress);
        header = (TextView)view.findViewById(R.id.headerText);
        subheader = (TextView)view.findViewById(R.id.subheaderText);
        activityTextView = (TextView)view.findViewById(R.id.activityTextView);
        getEndTime = (TextView)view.findViewById(R.id.endTime);
        getStartTime = (TextView)view.findViewById(R.id.startTime);
        specificTextView = (TextView)view.findViewById(R.id.specificTextView);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);
        separatorBelowOther = (View)view.findViewById(R.id.separatorLineBelowOther);
        separatorBelowSpecific = (View)view.findViewById(R.id.separatorLineBelowSpecific);
        separatorBelowAddress = (View)view.findViewById(R.id.separatorLineBelowAddress);
        separatorBelowEndTime = (View)view.findViewById(R.id.separatorLineBelowEndTime);
        editActivity = (ImageView)view.findViewById(R.id.editActivity);
        editSpecific = (ImageView)view.findViewById(R.id.editSpecific);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] items = new String[]{"Walk", "Hike", "Movie", "Other"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        chooseAcitivty.setAdapter(adapter);

        chooseAcitivty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                activity = chooseAcitivty.getSelectedItem().toString();
                if (activity.equals("Other")) {
                    other.setVisibility(View.VISIBLE);
                }

                if (!activity.equals("Other")) {
                    other.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        editActivity.setImageResource(R.drawable.save);
        editSpecific.setImageResource(R.drawable.save);

        clickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.equals("")){
                    if (activity.equals("Other")) {
                        activity = other.getText().toString();
                    }
                    specificEditText.setVisibility(View.VISIBLE);
                    header.setText("What's The Specific Activity?");
                    subheader.setText("(e.g. Mission Peak, Finding Dory, Golfland, Xmas in the Park");
                    separatorBelowOther.setVisibility(View.VISIBLE);
                    progressBar.setProgress(20);
                    activityTextView.setVisibility(View.VISIBLE);
                    if (activity.equals("Other")) {
                        activityTextView.setText(other.getText().toString());
                    } else {
                        activityTextView.setText(activity);
                    }

                    chooseAcitivty.setVisibility(View.INVISIBLE);
                    other.setVisibility(View.INVISIBLE);

                    activityTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityTextView.setVisibility(View.INVISIBLE);
                            chooseAcitivty.setVisibility(View.VISIBLE);
                            if (activity.equals("Other")) {
                                other.setVisibility(View.VISIBLE);
                            }

                            editActivity.setVisibility(View.VISIBLE);

                            editActivity.setVisibility(View.VISIBLE);
                            editActivity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    chooseAcitivty.setVisibility(View.INVISIBLE);
                                    other.setVisibility(View.INVISIBLE);
                                    editActivity.setVisibility(View.INVISIBLE);
                                    activityTextView.setVisibility(View.VISIBLE);
                                    activity = chooseAcitivty.getSelectedItem().toString();
                                    if (activity.equals("Other")) {
                                        activity = other.getText().toString();
                                    }
                                }
                            });
                        }
                    });
                }

                if (!activity.equals("") && !specificEditText.getText().toString().equals("")){
                    specific = specificEditText.getText().toString();

                    specificEditText.setVisibility(View.INVISIBLE);
                    specificTextView.setText(specific);
                    specificTextView.setVisibility(View.VISIBLE);

                    specificTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            specificTextView.setVisibility(View.INVISIBLE);
                            specificEditText.setVisibility(View.VISIBLE);
                            editSpecific.setVisibility(View.VISIBLE);

                            editSpecific.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    specificTextView.setVisibility(View.VISIBLE);
                                    specificEditText.setVisibility(View.INVISIBLE);
                                    specific = specificEditText.getText().toString();
                                    editSpecific.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });

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

                    separatorBelowSpecific.setVisibility(View.VISIBLE);
                    progressBar.setProgress(40);
                }

                if (!activity.equals("") && !specificEditText.getText().toString().equals("") && !addressTextView.getText().toString().equals("Click To Get Address")){

                    header.setText("Enter Start Time");
                    subheader.setVisibility(View.INVISIBLE);

                    getStartTime.setVisibility(View.VISIBLE);
                    separatorBelowAddress.setVisibility(View.VISIBLE);
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

                    PostDateFragment.listOfEvents.add(new EventsOfDate(specific, activity, address, startTime, endTime, placeName, latitude, longitude));
                    PostDateFragment.listOfPlaces.add(MyApplication.placeChosen);

                    PostDateFragment.adapter.notifyDataSetChanged();
                    PostDateFragment.titleDate.setVisibility(View.VISIBLE);
                    PostDateFragment.header.setText("Now Add a Short Description and Post the Date or Add another Event");

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

                        getStartTime.setText( "Start Time: " + hourOfDay + ":" + mm_precede + minute + AM_PM);
                        startTime = hourOfDay + ":"  + mm_precede + minute + AM_PM;

                        getEndTime.setVisibility(View.VISIBLE);
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
