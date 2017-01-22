package gllc.tech.dateapp.Automation;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.Objects.User;

/**
 * Created by bhangoo on 1/8/2017.
 */

public class SimpleCalculations {

    public static int GetTheDistance(ArrayList<EventsOfDate> eventsOfDate) {

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
        locationB.setLatitude(MyApplication.currentUser.getLatitude());
        locationB.setLongitude(MyApplication.currentUser.getLongitude());
        //locationB.setLongitude(MyApplication.longitude);
        Float distance = locationA.distanceTo(locationB) ;
        Double distance2 = distance*0.000621371;

        return Integer.parseInt(String.format("%.0f", distance2));
    }

    public static int GetTheDistanceOnePoint(double latitude, double longitude) {

        LatLng point = new LatLng(latitude, longitude);

        Location businessLocation = new Location("Point A");
        businessLocation.setLatitude(latitude);
        businessLocation.setLongitude(longitude);

        Location myLocation = new Location("Point B");
        myLocation.setLatitude(MyApplication.currentUser.getLatitude());
        myLocation.setLongitude(MyApplication.currentUser.getLongitude());

        Float distance = businessLocation.distanceTo(myLocation);
        Double distance2 = distance*0.000621371;

        return Integer.parseInt(String.format("%.0f", distance2));
    }

    public static int getAge(User user){

        String segments[] = user.getBirthdate().split("/");
        int year = Integer.parseInt(segments[2]);
        int month = Integer.parseInt(segments[0]);
        int day = Integer.parseInt(segments[1]);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageInt;
    }
}
