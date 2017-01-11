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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/2/2017.
 */

public class CreateEvent3 extends Fragment {

    FlatButton clickNext;
    MaterialSpinner chooseAcitivty;
    String prefix, main, suffix, address, startingTime, endingTime, fullEventTitle, photo, city;
    double latitude, longitude;
    TextView eventTitle, placeAddress, startTime, endTime;
    ImageView activityImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event3, container, false);

        prefix = "";
        main = "";
        suffix = "";
        address = "";
        startingTime = "";
        endingTime = "";
        latitude=0.0;
        latitude=0.0;
        photo = "NA";
        city = "";

        chooseAcitivty = (MaterialSpinner) view.findViewById(R.id.chooseActivity);
        eventTitle = (TextView) view.findViewById(R.id.eventTitle);
        placeAddress = (TextView) view.findViewById(R.id.placeAddress);
        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        activityImage = (ImageView) view.findViewById(R.id.activityImage);
        clickNext = (FlatButton) view.findViewById(R.id.nextActivity);

        eventTitle.setVisibility(View.INVISIBLE);
        placeAddress.setVisibility(View.INVISIBLE);
        startTime.setVisibility(View.INVISIBLE);
        endTime.setVisibility(View.INVISIBLE);
        clickNext.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        chooseAcitivty.setDropdownHeight(800);
        chooseAcitivty.setBackgroundColor(Color.parseColor("#31413f"));
        chooseAcitivty.setTextColor(Color.WHITE);
        chooseAcitivty.setHintTextColor(Color.WHITE);

        chooseAcitivty.setItems("Choose Activity", "Bowling", "Coffee", "Concert", "Dinner", "Event", "Lunch", "Go-Kart", "Minigolf", "Music Festival",
                "Hike", "Movie", "Walk", "Other");

        chooseAcitivty.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                main = chooseAcitivty.getText().toString();

                eventTitle.setVisibility(View.VISIBLE);
                eventTitle.setText(main);
                eventTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whereToGo();
                    }
                });

                chooseAcitivty.setVisibility(View.INVISIBLE);

                if (main.equals("Minigolf") || main.equals("Dinner")) {
                    activityImage.setImageResource(R.drawable.minigolf);
                }

                whereToGo();

            }
        });

        clickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PostDateFragment.listOfEvents.add(new EventsOfDate(suffix, main, address, startingTime, endingTime, suffix, latitude, longitude, photo, city));
                PostDate2.listOfEvents.add(new EventsOfDate(suffix, main, address, startingTime, endingTime, suffix, latitude, longitude, photo, city));
                //PostDateFragment.adapter.notifyDataSetChanged();
                ((MainActivity)getActivity()).popBackStack();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.create_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.resetEvent:
                ((MainActivity)getActivity()).replaceFragments(CreateEvent3.class, R.id.container, "CreateEvent");
        }

        return true;
    }

    public void whereToGo() {

        new AlertDialog.Builder(getContext())
                .setTitle(main)
                .setMessage("Do You Know Where You Want To Go?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        makeSuggestions();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
                })
                .show();
    }

    public void getPlacesDetails(String placeId) {

        new AsyncHttpClient().post("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDQbqcJuQtmi88_82Sq8Ixipv8NjpCMMeY&placeid=" + placeId, null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("--All", "In Failure");
                        Log.i("--All", "Failure response: " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        //Log.i("--All", "Result: " + responseString);

                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONObject result = jsonObject.getJSONObject("result");

                            address = result.getString("formatted_address");

                            String segments[] = result.getString("vicinity").split(",");

                            city = segments[1];


                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = Double.parseDouble(location.getString("lat"));
                            longitude = Double.parseDouble(location.getString("lng"));

                            address = address.replaceFirst(",", "\n");
                            placeAddress.setText(address);

                            suffix = result.getString("name");

                            fullEventTitle = main + " at " + suffix;
                            eventTitle.setText(fullEventTitle);

                            JSONArray photoArray = result.getJSONArray("photos");
                            JSONObject firstPhoto = photoArray.getJSONObject(0);
                            String photoReference = firstPhoto.getString("photo_reference");

                            getPhoto(photoReference);

                        } catch (Exception e) {
                            Log.i("--All", "Error: " + e.getMessage());
                            getStartTime();
                        }

                        placeAddress.setVisibility(View.VISIBLE);
                        chooseAcitivty.setVisibility(View.GONE);
                    }
                });

    }

    public void getPhoto(final String photoReference) {

        new AsyncHttpClient().post("https://maps.googleapis.com/maps/api/place/photo?key=AIzaSyDQbqcJuQtmi88_82Sq8Ixipv8NjpCMMeY&maxwidth=600&photoreference=" + photoReference, null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        String segments[] = responseString.split("\"");

                        photo = segments[5];

                        Picasso.with(getContext()).load(photo).into(activityImage);

                        startTime.setVisibility(View.VISIBLE);
                        startTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getStartTime();
                            }
                        });

                        getStartTime();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    }
                });
    }

    public void getStartTime() {
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            int callCount = 0;

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                if (callCount < 1) {
                    String AM_PM = " AM";
                    String mm_precede = "";
                    if (hourOfDay >= 12) {
                        AM_PM = " PM";
                        if (hourOfDay >= 13 && hourOfDay < 24) {
                            hourOfDay -= 12;
                        } else {
                            hourOfDay = 12;
                        }
                    } else if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                    if (minute < 10) {
                        mm_precede = "0";
                    }

                    startTime.setText("Start Time: " + hourOfDay + ":" + mm_precede + minute + AM_PM);
                    startingTime = hourOfDay + ":" + mm_precede + minute + AM_PM;

                    endTime.setVisibility(View.VISIBLE);
                    endTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getEndTime();
                        }
                    });

                    getEndTime();
                }

                callCount++;



            }
        }, 12, 0, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Start Time");
        mTimePicker.show();
    }

    public void getEndTime() {

        TimePickerDialog mTimePicker2;

        mTimePicker2 = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            int callCount=0;

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                if (callCount < 1) {

                    String AM_PM = " AM";
                    String mm_precede = "";
                    if (hourOfDay >= 12) {
                        AM_PM = " PM";
                        if (hourOfDay >= 13 && hourOfDay < 24) {
                            hourOfDay -= 12;
                        } else {
                            hourOfDay = 12;
                        }
                    } else if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                    if (minute < 10) {
                        mm_precede = "0";
                    }

                    endTime.setText("End Time: " + hourOfDay + ":" + mm_precede + minute + AM_PM);
                    endingTime = hourOfDay + ":" + mm_precede + minute + AM_PM;


                    clickNext.setText("Add Event!");
                    clickNext.setVisibility(View.VISIBLE);
                }

                callCount++;
            }
        }, 12, 0, false);//Yes 24 hour time
        mTimePicker2.setTitle("Select End Time");
        mTimePicker2.show();
    }

    public void makeSuggestions() {

        new AsyncHttpClient().post("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDQbqcJuQtmi88_82Sq8Ixipv8NjpCMMeY&location="+
                MyApplication.currentUser.getLatitude()+","+ MyApplication.currentUser.getLongitude()+"&rankby=distance&keyword="+main, null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        try{
                            ArrayList<String> names = new ArrayList<>();
                            final ArrayList<String> placeIds = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONArray result = jsonObject.getJSONArray("results");

                            for (int i=0 ; i<result.length(); i++) {
                                JSONObject firstResult = result.getJSONObject(i);
                                names.add(firstResult.getString("name"));
                                placeIds.add(firstResult.getString("place_id"));
                            }

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                            builderSingle.setTitle("Some Nearby Suggestions:");

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);

                            for (int j = 0 ; j <names.size(); j++) {
                                arrayAdapter.add(names.get(j));
                            }

                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getPlacesDetails(placeIds.get(which));
                                }
                            });
                            builderSingle.show();

                        } catch (Exception e){
                            Log.i("--All", "Error: " + e.getMessage());
                        }

                    }
                });
    }
}

