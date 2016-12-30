package gllc.tech.dateapp.Automation;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;

/**
 * Created by bhangoo on 12/28/2016.
 */

public class GetDistance {

    public Double GetDistance(ArrayList<EventsOfDate> eventsOfDate) {

        ArrayList<EventsOfDate> allEvents = eventsOfDate;

        ArrayList<LatLng> points = new ArrayList<>();

        for (int i=0; i<allEvents.size(); i++) {
            LatLng latLng = new LatLng(allEvents.get(i).getLatitude(), allEvents.get(i).getLongitude());
            points.add(latLng);
        }

        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        LatLng center = new LatLng(latitude/n, longitude/n);

        Location locationA = new Location("point A");
        locationA.setLatitude(center.latitude);
        locationA.setLongitude(center.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(MyApplication.latitude);
        locationB.setLongitude(MyApplication.longitude);
        Float distance = locationA.distanceTo(locationB) ;
        Double distance2 = distance*0.000621371;

        return distance2;
    }
}
