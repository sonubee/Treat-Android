package gllc.tech.dateapp.PostDate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.PlacesDetails;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.Automation.YelpService;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bhangoo on 1/2/2017.
 */

public class CreateEvent3 extends Fragment{

    FlatButton clickNext;
    MaterialSpinner chooseAcitivty;
    String prefix, main, suffix, address, startingTime, endingTime, fullEventTitle, photo, city;
    double latitude, longitude;
    TextView eventTitle, placeAddress, startTime, endTime;
    CircleImageView activityImage;
    ProgressDialog pleaseWait;

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
        activityImage = (CircleImageView) view.findViewById(R.id.activityImage);
        clickNext = (FlatButton) view.findViewById(R.id.nextActivity);

        eventTitle.setVisibility(View.INVISIBLE);
        placeAddress.setVisibility(View.INVISIBLE);
        startTime.setVisibility(View.INVISIBLE);
        endTime.setVisibility(View.INVISIBLE);
        clickNext.setVisibility(View.INVISIBLE);


        chooseAcitivty.setVisibility(View.INVISIBLE);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setHasOptionsMenu(true);

        main = getArguments().getString("activitySelected");

        eventTitle.setVisibility(View.VISIBLE);
        eventTitle.setText(main);
        eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //whereToGo();
                makeSuggestions3();
            }
        });

        if (main.equals("MiniGolf") || main.equals("Dinner")) {
            String pic = "minigolf";
            int id = getResources().getIdentifier(pic, "drawable", getActivity().getPackageName());
            activityImage.setImageResource(id);
            //activityImage.setImageResource(R.drawable.minigolf);
        }

        Log.i("--All", "Making Suggestion");
        makeSuggestions4();

        clickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PostDateFragment.listOfEvents.add(new EventsOfDate(suffix, main, address, startingTime, endingTime, suffix, latitude, longitude, photo, city));
                PostDate2.listOfEvents.add(new EventsOfDate(suffix, main, address, startingTime, endingTime, suffix, latitude, longitude, photo, city));
                //PostDateFragment.adapter.notifyDataSetChanged();
                ((MainActivity)getActivity()).popAllFragments();
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
                ((MainActivity)getActivity()).replaceFragments(CreateEvent3.class, R.id.container, "CreateEvent", null);
        }

        return true;
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
        Toast.makeText(getContext(), "Choose Start Time", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(), "Choose End Time", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getContext(), "All Done!", Toast.LENGTH_SHORT).show();
                }

                callCount++;
            }
        }, 12, 0, false);//Yes 24 hour time
        mTimePicker2.setTitle("Select End Time");
        mTimePicker2.show();
    }

    public void makeSuggestions3() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_location_dialog);
        dialog.setTitle("Some Suggestions");

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

        FlatButton dialogButton = (FlatButton) dialog.findViewById(R.id.chooseOwnLocationButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        pleaseWait = new ProgressDialog(getContext());
        pleaseWait.setMessage("Please Wait");
        pleaseWait.setCancelable(false);
        pleaseWait.setInverseBackgroundForced(false);
        pleaseWait.show();

        final ArrayList<PlacesDetails> placesDetailsArrayList = new ArrayList<>();
        final PostDateSuggestionsAdapter postDateSuggestionsAdapter = new PostDateSuggestionsAdapter(getContext(), placesDetailsArrayList);

        ListView suggestionsListView = (ListView) dialog.findViewById(R.id.suggestionsListView);
        suggestionsListView.setAdapter(postDateSuggestionsAdapter);

        final YelpService yelpService = new YelpService();
        yelpService.suggestions(main, new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("--All", "Error Yelp: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    //Log.v("--All", "Yelp Response: " + jsonData);

                    JSONObject fullResponse = new JSONObject(jsonData);
                    JSONArray businesses = fullResponse.getJSONArray("businesses");

                    for (int i=0; i<businesses.length(); i++) {
                        JSONObject business = businesses.getJSONObject(i);
                        JSONObject location = business.getJSONObject("location");
                        JSONArray address = location.getJSONArray("display_address");
                        String formattedAddress = address.get(0).toString();
                        for (int j=1; j <address.length();j++) {
                            formattedAddress += "\n" + address.get(j).toString();
                        }
                        JSONObject coordinate = location.getJSONObject("coordinate");
/*
                        placesDetailsArrayList.add(new PlacesDetails("PLACE ID", business.getString("name"), location.getString("city"),
                                Integer.parseInt(business.getString("review_count")), business.getString("image_url").replace("ms", "l"), formattedAddress,
                                Double.parseDouble(coordinate.getString("latitude")), Double.parseDouble(coordinate.getString("longitude")), business.getDouble("rating")));
*/

                        placesDetailsArrayList.add(new PlacesDetails("PLACE ID", business.getString("name"), location.getString("city"),
                                Integer.parseInt(business.getString("review_count")), business.getString("image_url").replace("ms", "l"), formattedAddress,
                                coordinate.getDouble("latitude"), coordinate.getDouble("longitude"), business.getDouble("rating")));

                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                postDateSuggestionsAdapter.notifyDataSetChanged();
                                pleaseWait.hide();

                            }
                        });
                    }

                } catch (Exception e) {
                    Log.i("--All", "Error Parsing JSON: " + e.getMessage());
                }


            }
        });

        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                photo = placesDetailsArrayList.get(position).getPhoto();
                Picasso.with(getContext()).load(photo).into(activityImage);

                suffix = placesDetailsArrayList.get(position).getName();

                fullEventTitle = main + " at " + suffix;
                eventTitle.setText(fullEventTitle);

                city = placesDetailsArrayList.get(position).getCity();

                address = placesDetailsArrayList.get(position).getAddress();
                //address = address.replaceFirst(",", "\n");
                placeAddress.setText(address);

                latitude = placesDetailsArrayList.get(position).getLatitude();
                longitude = placesDetailsArrayList.get(position).getLongitude();

                placeAddress.setVisibility(View.VISIBLE);
                chooseAcitivty.setVisibility(View.GONE);

                startTime.setVisibility(View.VISIBLE);
                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getStartTime();
                    }
                });

                dialog.dismiss();

                getStartTime();
            }
        });
    }

    public void makeSuggestions4() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_location_dialog);
        dialog.setTitle("Some Suggestions");

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

        FlatButton dialogButton = (FlatButton) dialog.findViewById(R.id.chooseOwnLocationButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        pleaseWait = new ProgressDialog(getContext());
        pleaseWait.setMessage("Please Wait");
        pleaseWait.setCancelable(false);
        pleaseWait.setInverseBackgroundForced(false);
        pleaseWait.show();

        final ArrayList<PlacesDetails> placesDetailsArrayList = new ArrayList<>();
        final PostDateSuggestionsAdapter postDateSuggestionsAdapter = new PostDateSuggestionsAdapter(getContext(), placesDetailsArrayList);

        ListView suggestionsListView = (ListView) dialog.findViewById(R.id.suggestionsListView);
        suggestionsListView.setAdapter(postDateSuggestionsAdapter);


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("latitude", MyApplication.currentUser.getLatitude());
        params.put("longitude", MyApplication.currentUser.getLongitude());

        String category = "";
        if (main.equals("MiniGolf")) {category = "mini_golf";}
        if (main.equals("Bowling")) {category = "bowling";}
        if (main.equals("Dinner")) {category = "restaurants";}

        params.put("categories", category);

        client.addHeader("Authorization", "Bearer "+MyApplication.yelpToken);

        Log.i("--All", "Token Create Event: " + MyApplication.yelpToken);

        client.get("https://api.yelp.com/v3/businesses/search?", params,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("--All", "Failure response Yelp Query: " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        //Log.i("--All", "Result2: " + responseString);

                        try {
                            //String jsonData = response.body().string();
                            //Log.v("--All", "Yelp Response: " + jsonData);

                            JSONObject fullResponse = new JSONObject(responseString);
                            JSONArray businesses = fullResponse.getJSONArray("businesses");

                            for (int i=0; i<businesses.length(); i++) {
                                JSONObject business = businesses.getJSONObject(i);
                                JSONObject location = business.getJSONObject("location");
                                JSONArray address = location.getJSONArray("display_address");
                                String formattedAddress = address.get(0).toString();
                                for (int j=1; j <address.length();j++) {
                                    formattedAddress += "\n" + address.get(j).toString();
                                }
                                JSONObject coordinate = business.getJSONObject("coordinates");

                                placesDetailsArrayList.add(new PlacesDetails("PLACE ID", business.getString("name"), location.getString("city"),
                                        Integer.parseInt(business.getString("review_count")), business.getString("image_url").replace("ms", "l"), formattedAddress,
                                        coordinate.getDouble("latitude"), coordinate.getDouble("longitude"), business.getDouble("rating")));

                            }


                            postDateSuggestionsAdapter.notifyDataSetChanged();
                            pleaseWait.hide();

                        } catch (Exception e) {
                            Log.i("--All", "Error Parsing JSON: " + e.getMessage());
                        }
                    }
                });

        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                photo = placesDetailsArrayList.get(position).getPhoto();
                Picasso.with(getContext()).load(photo).into(activityImage);

                suffix = placesDetailsArrayList.get(position).getName();

                fullEventTitle = main + " at " + suffix;
                eventTitle.setText(fullEventTitle);

                city = placesDetailsArrayList.get(position).getCity();

                address = placesDetailsArrayList.get(position).getAddress();
                //address = address.replaceFirst(",", "\n");
                placeAddress.setText(address);

                latitude = placesDetailsArrayList.get(position).getLatitude();
                longitude = placesDetailsArrayList.get(position).getLongitude();

                placeAddress.setVisibility(View.VISIBLE);
                chooseAcitivty.setVisibility(View.GONE);

                startTime.setVisibility(View.VISIBLE);
                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getStartTime();
                    }
                });

                dialog.dismiss();

                getStartTime();
            }
        });
    }
}

