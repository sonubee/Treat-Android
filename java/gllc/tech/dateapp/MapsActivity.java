package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.TheDate;
import gllc.tech.dateapp.PostDate.PostDateFragment;

/**
 * Created by bhangoo on 12/12/2016.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (MyApplication.cameFromPost) {
            LatLng sydney = new LatLng(PostDateFragment.listOfPlaces.get(PostDateFragment.selectedMap).getLatLng().latitude,
                    PostDateFragment.listOfPlaces.get(PostDateFragment.selectedMap).getLatLng().longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title(PostDateFragment.listOfEvents.get(PostDateFragment.selectedMap).getAddress()));
            mMap.setMinZoomPreference(13);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        if (MyApplication.cameFromYourDates) {

            TheDate theDate = MyApplication.dateHashMap.get(MyApplication.dateSelectedKey);

            ArrayList<EventsOfDate> allEvents;

            allEvents = theDate.getEvents();

            ArrayList<Marker> allMarkers = new ArrayList<>();

            for (int i=0; i<allEvents.size(); i++) {
                LatLng latLng = new LatLng(allEvents.get(i).getLatitude(), allEvents.get(i).getLongitude());
                allMarkers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(allEvents.get(i).getPlaceName())));
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : allMarkers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            //int padding = 0; // offset from edges of the map in pixels
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.25); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            googleMap.moveCamera(cu);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.cameFromPost=false;
        MyApplication.cameFromYourDates=false;
    }
}
