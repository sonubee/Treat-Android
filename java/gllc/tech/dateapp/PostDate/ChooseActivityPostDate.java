package gllc.tech.dateapp.PostDate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.dd.processbutton.FlatButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.PlacesDetails;
import gllc.tech.dateapp.PostDate.CreateEvent3;
import gllc.tech.dateapp.PostDate.DisplayActivityGridViewAdapter;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/9/2017.
 */

public class ChooseActivityPostDate extends Fragment{

    ProgressDialog pleaseWait;
    ArrayList<PlacesDetails> placesDetailsArrayList;
    PostDateSuggestionsAdapter postDateSuggestionsAdapter;
    int counter=0, counterHolder=0;
    String globalResponse;
    String photoURL, place, city,address, activity, activitySpecificName="", posterPath="";
    Double latitude, longitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_choose_activity, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.chooseActivityGridView);
        gridview.setAdapter(new DisplayActivityGridViewAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int activityPosition, long id) {

                if (MyApplication.categories.get(activityPosition).getDisplayName().equals("Movie")) {

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    params.put("api_key", "b26520fd19b1c1f349e2ffb81a3050ef");
                    params.put("language", "en-US");
                    params.put("sort_by", "popularity.desc");
                    params.put("include_adult", "false");
                    params.put("include_video", "false");
                    params.put("page", "1");

                    client.get("https://api.themoviedb.org/3/discover/movie?", params,
                            new TextHttpResponseHandler() {

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.i("--All", "Failure response TMD Query: " + responseString);
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    //Log.i("--All", "Search Response: " + responseString);
                                    try {
                                        JSONObject fullResponse = new JSONObject(responseString);
                                        JSONArray movies = fullResponse.getJSONArray("results");

                                        final ArrayList<String> movieNames = new ArrayList<String>();
                                        final ArrayList<String> posterPathArrayList = new ArrayList<String>();

                                        for (int i = 0; i < movies.length(); i++) {
                                            JSONObject movie = movies.getJSONObject(i);

                                            movieNames.add(movie.getString("original_title"));
                                            posterPathArrayList.add(movie.getString("poster_path"));
                                        }

                                        final Dialog dialog = new Dialog(getContext());
                                        dialog.setContentView(R.layout.which_movie);
                                        dialog.setTitle("Which Movie?");

                                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

                                        WhichMovieAdapter whichMovieAdapter = new WhichMovieAdapter(getContext(), movieNames, posterPathArrayList);

                                        ListView whichMovieListView = (ListView) dialog.findViewById(R.id.whichMovieListView);
                                        whichMovieListView.setAdapter(whichMovieAdapter);

                                        whichMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                activitySpecificName = movieNames.get(position);
                                                posterPath = posterPathArrayList.get(position);

                                                dialog.dismiss();
                                                getLocation(activityPosition, -1);
                                            }
                                        });

                                        dialog.show();

                                    } catch (JSONException e) {
                                        Log.i("--All", "Error Parsing TMD Data: " + e.getMessage());
                                    }
                                }
                            });


                } else if (MyApplication.categories.get(activityPosition).getDisplayName().equals("Restaurants")) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.which_restaurant);
                    dialog.setTitle("What Type of Food?");

                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

                    WhichRestaurantAdapter whichRestaurantAdapter = new WhichRestaurantAdapter(getContext());

                    ListView whichRestaurantListView = (ListView) dialog.findViewById(R.id.whichRestaurantListView);
                    whichRestaurantListView.setAdapter(whichRestaurantAdapter);

                    whichRestaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            activitySpecificName = MyApplication.restaurants.get(position).getDisplayName();
                            getLocation(activityPosition, position);
                        }
                    });

                    dialog.show();

                } else {
                    getLocation(activityPosition, -1);
                }


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void parseData() {
        try {
            //Log.v("--All", "Yelp Response: " + globalResponse);

            JSONObject fullResponse = new JSONObject(globalResponse);
            JSONArray businesses = fullResponse.getJSONArray("businesses");

            for (counter = counterHolder; counter < businesses.length(); counter++) {
                JSONObject business = businesses.getJSONObject(counter);
                JSONObject location = business.getJSONObject("location");
                JSONArray address = location.getJSONArray("display_address");
                String formattedAddress = address.get(0).toString();
                for (int j = 1; j < address.length(); j++) {
                    formattedAddress += "\n" + address.get(j).toString();
                }
                JSONObject coordinate = business.getJSONObject("coordinates");

                placesDetailsArrayList.add(new PlacesDetails(activity, business.getString("name"), location.getString("city"),
                        Integer.parseInt(business.getString("review_count")), business.getString("image_url").replace("ms", "ls"), formattedAddress,
                        coordinate.getDouble("latitude"), coordinate.getDouble("longitude"), business.getDouble("rating")));

            }


            postDateSuggestionsAdapter.notifyDataSetChanged();
            pleaseWait.hide();


        } catch (Exception e) {
            Log.i("--All", "Error Parsing JSON: " + e.getMessage());
            counterHolder = counter;
            counterHolder++;
            parseData();

        }
    }

    public void getLocation(int positionOfLocation, int restaurantLocation) {

        placesDetailsArrayList = new ArrayList<>();
        postDateSuggestionsAdapter = new PostDateSuggestionsAdapter(getContext(), placesDetailsArrayList);

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_location_dialog);
        dialog.setTitle("Some Suggestions");

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

        FlatButton dialogButton = (FlatButton) dialog.findViewById(R.id.chooseOwnLocationButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placesDetailsArrayList.clear();
                postDateSuggestionsAdapter.notifyDataSetChanged();
                dialog.dismiss();

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.choose_place);
                dialog.setTitle("Search Here");
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);

                ListView searchPlaceListView = (ListView) dialog.findViewById(R.id.searchPlaceListView);
                searchPlaceListView.setAdapter(postDateSuggestionsAdapter);

                FlatButton searchButton = (FlatButton) dialog.findViewById(R.id.searchPlaceButton);
                final EditText editText = (EditText)dialog.findViewById(R.id.searchPlaceEditText);

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        placesDetailsArrayList.clear();
                        postDateSuggestionsAdapter.notifyDataSetChanged();


                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();

                        params.put("term", editText.getText().toString());
                        params.put("latitude", MyApplication.currentUser.getLatitude());
                        params.put("longitude", MyApplication.currentUser.getLongitude());
                        params.put("categories", MyApplication.categoriesMap.get(activity).getCategory());

                        client.addHeader("Authorization", "Bearer "+MyApplication.yelpToken);

                        client.get("https://api.yelp.com/v3/businesses/search?", params,
                                new TextHttpResponseHandler() {

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        Log.i("--All", "Failure response Yelp Query: " + responseString);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        counterHolder=0;
                                        globalResponse = responseString;
                                        parseData();
                                    }
                                });
                    }
                });

                searchPlaceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        selectedPlace(dialog, position);
                        /*
                        photoURL = placesDetailsArrayList.get(position).getPhoto();
                        place = placesDetailsArrayList.get(position).getName();
                        city = placesDetailsArrayList.get(position).getCity();
                        address = placesDetailsArrayList.get(position).getAddress();
                        latitude = placesDetailsArrayList.get(position).getLatitude();
                        longitude = placesDetailsArrayList.get(position).getLongitude();

                        Bundle bundle = new Bundle();
                        bundle.putString("activitySelected", activity);
                        bundle.putString("place", place);
                        bundle.putString("city", city);
                        bundle.putString("address", address);
                        bundle.putDouble("latitude", latitude);
                        bundle.putDouble("longitude", longitude);
                        bundle.putString("activitySpecificName", activitySpecificName);
                        if (posterPath.equals("")) {
                            bundle.putString("photoURL", photoURL);
                        } else {
                            bundle.putString("photoURL", "https://image.tmdb.org/t/p/w500"+posterPath);
                        }

                        dialog.dismiss();

                        ((MainActivity)getActivity()).addFragments(CreateEvent3.class, R.id.container, "CreateEvent", bundle);
                        */
                    }
                });

                dialog.show();

            }
        });
        dialog.show();

        pleaseWait = new ProgressDialog(getContext());
        pleaseWait.setMessage("Please Wait");
        pleaseWait.setCancelable(false);
        pleaseWait.setInverseBackgroundForced(false);
        pleaseWait.show();

        ListView suggestionsListView = (ListView) dialog.findViewById(R.id.suggestionsListView);
        suggestionsListView.setAdapter(postDateSuggestionsAdapter);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        activity = MyApplication.categories.get(positionOfLocation).getDisplayName();

        params.put("latitude", MyApplication.currentUser.getLatitude());
        params.put("longitude", MyApplication.currentUser.getLongitude());
        if (restaurantLocation >= 0) {
            params.put("categories", MyApplication.restaurantsHashMap.get(MyApplication.restaurants.get(restaurantLocation).getDisplayName()).getCategory());
        } else {
            params.put("categories", MyApplication.categoriesMap.get(MyApplication.categories.get(positionOfLocation).getDisplayName()).getCategory());
        }

        //params.put("limit", 8);

        client.addHeader("Authorization", "Bearer "+MyApplication.yelpToken);

        client.get("https://api.yelp.com/v3/businesses/search?", params,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("--All", "Failure response Yelp Query: " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        counterHolder=0;
                        globalResponse = responseString;
                        parseData();
                    }
                });

        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedPlace(dialog, position);
                /*

                photoURL = placesDetailsArrayList.get(position).getPhoto();
                place = placesDetailsArrayList.get(position).getName();
                city = placesDetailsArrayList.get(position).getCity();
                address = placesDetailsArrayList.get(position).getAddress();
                latitude = placesDetailsArrayList.get(position).getLatitude();
                longitude = placesDetailsArrayList.get(position).getLongitude();

                Bundle bundle = new Bundle();
                bundle.putString("activitySelected", activity);
                bundle.putString("place", place);
                bundle.putString("city", city);
                bundle.putString("address", address);
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                bundle.putString("activitySpecificName", activitySpecificName);
                if (posterPath.equals("")) {
                    bundle.putString("photoURL", photoURL);
                } else {
                    bundle.putString("photoURL", "https://image.tmdb.org/t/p/w500"+posterPath);
                }

                dialog.dismiss();

                ((MainActivity)getActivity()).addFragments(CreateEvent3.class, R.id.container, "CreateEvent", bundle);
                */
            }
        });
    }

    public void selectedPlace(Dialog dialog, int position) {

        photoURL = placesDetailsArrayList.get(position).getPhoto();
        place = placesDetailsArrayList.get(position).getName();
        city = placesDetailsArrayList.get(position).getCity();
        address = placesDetailsArrayList.get(position).getAddress();
        latitude = placesDetailsArrayList.get(position).getLatitude();
        longitude = placesDetailsArrayList.get(position).getLongitude();

        Bundle bundle = new Bundle();
        bundle.putString("activitySelected", activity);
        bundle.putString("place", place);
        bundle.putString("city", city);
        bundle.putString("address", address);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putString("activitySpecificName", activitySpecificName);
        if (posterPath.equals("")) {
            bundle.putString("photoURL", photoURL);
        } else {
            bundle.putString("photoURL", "https://image.tmdb.org/t/p/w500"+posterPath);
        }

        dialog.dismiss();

        ((MainActivity)getActivity()).addFragments(CreateEvent3.class, R.id.container, "CreateEvent", bundle);
    }
}
