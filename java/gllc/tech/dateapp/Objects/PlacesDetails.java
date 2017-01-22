package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 1/13/2017.
 */

public class PlacesDetails {


    String placeId;
    String name;
    String city;
    int reviews;
    String photo;
    String address;
    double latitude;
    double longitude;
    double rating;

    public PlacesDetails(String placeId, String name, String city, int reviews, String photo, String address, double latitude, double longitude, double rating) {
        this.placeId = placeId;
        this.name = name;
        this.city = city;
        this.reviews = reviews;
        this.photo = photo;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public PlacesDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
