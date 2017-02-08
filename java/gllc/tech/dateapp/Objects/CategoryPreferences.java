package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 2/7/2017.
 */

public class CategoryPreferences {

    boolean bowling;
    boolean coffee;
    boolean dog_parks;
    boolean gokarts;
    boolean hiking;
    boolean horsebackriding;
    boolean hotdogs;
    boolean mini_golf;
    boolean movietheaters;
    boolean restaurants;
    boolean skydiving;
    boolean snorkeling;
    boolean tennis;
    boolean zipline;
    boolean zoos;

    public CategoryPreferences(boolean bowling, boolean coffee, boolean dog_parks, boolean gokarts, boolean hiking, boolean horsebackriding, boolean hotdogs,
                               boolean mini_golf, boolean movietheaters, boolean restaurants, boolean skydiving, boolean snorkeling, boolean tennis, boolean zipline,
                               boolean zoos) {
        this.bowling = bowling;
        this.coffee = coffee;
        this.dog_parks = dog_parks;
        this.gokarts = gokarts;
        this.hiking = hiking;
        this.horsebackriding = horsebackriding;
        this.hotdogs = hotdogs;
        this.mini_golf = mini_golf;
        this.movietheaters = movietheaters;
        this.restaurants = restaurants;
        this.skydiving = skydiving;
        this.snorkeling = snorkeling;
        this.tennis = tennis;
        this.zipline = zipline;
        this.zoos = zoos;
    }

    public CategoryPreferences() {
    }

    public boolean isBowling() {
        return bowling;
    }

    public void setBowling(boolean bowling) {
        this.bowling = bowling;
    }

    public boolean isCoffee() {
        return coffee;
    }

    public void setCoffee(boolean coffee) {
        this.coffee = coffee;
    }

    public boolean isDog_parks() {
        return dog_parks;
    }

    public void setDog_parks(boolean dog_parks) {
        this.dog_parks = dog_parks;
    }

    public boolean isGokarts() {
        return gokarts;
    }

    public void setGokarts(boolean gokarts) {
        this.gokarts = gokarts;
    }

    public boolean isHiking() {
        return hiking;
    }

    public void setHiking(boolean hiking) {
        this.hiking = hiking;
    }

    public boolean isHorsebackriding() {
        return horsebackriding;
    }

    public void setHorsebackriding(boolean horsebackriding) {
        this.horsebackriding = horsebackriding;
    }

    public boolean isHotdogs() {
        return hotdogs;
    }

    public void setHotdogs(boolean hotdogs) {
        this.hotdogs = hotdogs;
    }

    public boolean isMini_golf() {
        return mini_golf;
    }

    public void setMini_golf(boolean mini_golf) {
        this.mini_golf = mini_golf;
    }

    public boolean isMovietheaters() {
        return movietheaters;
    }

    public void setMovietheaters(boolean movietheaters) {
        this.movietheaters = movietheaters;
    }

    public boolean isRestaurants() {
        return restaurants;
    }

    public void setRestaurants(boolean restaurants) {
        this.restaurants = restaurants;
    }

    public boolean isSkydiving() {
        return skydiving;
    }

    public void setSkydiving(boolean skydiving) {
        this.skydiving = skydiving;
    }

    public boolean isSnorkeling() {
        return snorkeling;
    }

    public void setSnorkeling(boolean snorkeling) {
        this.snorkeling = snorkeling;
    }

    public boolean isTennis() {
        return tennis;
    }

    public void setTennis(boolean tennis) {
        this.tennis = tennis;
    }

    public boolean isZipline() {
        return zipline;
    }

    public void setZipline(boolean zipline) {
        this.zipline = zipline;
    }

    public boolean isZoos() {
        return zoos;
    }

    public void setZoos(boolean zoos) {
        this.zoos = zoos;
    }
}
