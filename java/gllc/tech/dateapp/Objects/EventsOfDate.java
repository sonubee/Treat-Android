package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 12/1/2016.
 */
public class EventsOfDate {

    String activitySpecificName;
    String activity;
    String address;
    String beginTime;
    String endTime;
    String placeName;
    double latitude;
    double longitude;
    String photo;
    String city;

    public EventsOfDate() {
    }

    public EventsOfDate(String activitySpecificName, String activity, String address, String beginTime, String endTime, String placeName, double latitude, double longitude,
                        String photo, String city) {
        this.activitySpecificName = activitySpecificName;
        this.activity = activity;
        this.address = address;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
        this.city = city;
    }

    public String getActivitySpecificName() {
        return activitySpecificName;
    }

    public void setActivitySpecificName(String activitySpecificName) {
        this.activitySpecificName = activitySpecificName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
