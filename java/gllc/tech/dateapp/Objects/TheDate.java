package gllc.tech.dateapp.Objects;

import java.util.ArrayList;

/**
 * Created by bhangoo on 12/1/2016.
 */
public class TheDate {

    String poster;
    String theDate;
    String dateCreated;
    String dateOfDate;
    String dateTitle;
    String key;
    ArrayList<EventsOfDate> events;
    boolean posterKarma;
    boolean theDateKarma;

    public TheDate() {
    }

    public TheDate(String poster, String theDate, String dateCreated, String dateOfDate, String dateTitle, String key, ArrayList<EventsOfDate> events, boolean posterKarma, boolean theDateKarma) {
        this.poster = poster;
        this.theDate = theDate;
        this.dateCreated = dateCreated;
        this.dateOfDate = dateOfDate;
        this.dateTitle = dateTitle;
        this.key = key;
        this.events = events;
        this.posterKarma = posterKarma;
        this.theDateKarma = theDateKarma;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTheDate() {
        return theDate;
    }

    public void setTheDate(String theDate) {
        this.theDate = theDate;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateOfDate() {
        return dateOfDate;
    }

    public void setDateOfDate(String dateOfDate) {
        this.dateOfDate = dateOfDate;
    }

    public String getDateTitle() {
        return dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<EventsOfDate> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventsOfDate> events) {
        this.events = events;
    }

    public boolean isPosterKarma() {return posterKarma;}

    public void setPosterKarma(boolean posterKarma) {
        this.posterKarma = posterKarma;
    }

    public boolean isTheDateKarma() {
        return theDateKarma;
    }

    public void setTheDateKarma(boolean theDateKarma) {
        this.theDateKarma = theDateKarma;
    }

}
