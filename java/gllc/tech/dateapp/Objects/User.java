package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 12/1/2016.
 */
public class User {

    String name;
    String email;
    String id;
    String gender;
    String profilePic;
    String fid;
    String bio;
    String photo1;
    String photo2;
    String photo3;
    String photo4;
    int karmaPoints;
    String pushToken;
    boolean showMen;
    boolean showWomen;
    int ageMin;
    int ageMax;
    int distance;

    public User(String name, String email, String id, String gender, String profilePic, String fid, String bio, String photo1, String photo2,
                String photo3, String photo4, int karmaPoints, String pushToken, boolean showMen, boolean showWomen, int ageMin, int ageMax, int distance) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.gender = gender;
        this.profilePic = profilePic;
        this.fid = fid;
        this.bio = bio;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.photo4 = photo4;
        this.karmaPoints = karmaPoints;
        this.pushToken = pushToken;
        this.showMen = showMen;
        this.showWomen = showWomen;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.distance = distance;
    }




    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public int getKarmaPoints() {
        return karmaPoints;
    }

    public void setKarmaPoints(int karmaPoints) {
        this.karmaPoints = karmaPoints;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public boolean isShowMen() {
        return showMen;
    }

    public void setShowMen(boolean showMen) {
        this.showMen = showMen;
    }

    public boolean isShowWomen() {
        return showWomen;
    }

    public void setShowWomen(boolean showWomen) {
        this.showWomen = showWomen;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
