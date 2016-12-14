package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 12/7/2016.
 */

public class AgreedChats {

    String poster;
    String requester;
    String dateKey;

    public AgreedChats(String poster, String requester, String dateKey) {
        this.poster = poster;
        this.requester = requester;
        this.dateKey = dateKey;
    }

    public AgreedChats() {
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }


}
