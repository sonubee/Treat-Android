package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class RequestDetails {

    String userId;
    String profilePic;

    public RequestDetails(String userId, String profilePic) {
        this.userId = userId;
        this.profilePic = profilePic;
    }

    public RequestDetails() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


}
