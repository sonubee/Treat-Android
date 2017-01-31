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
    double latitude;
    double longitude;
    String birthdate;
    String school;
    String work;
    boolean gaveFullBirthday;
    boolean whoseTreatPosterTreat;
    boolean whoseTreatDateTreat;
    boolean whoseTreatSplitBill;
    boolean whoseTreatDecideLater;

    public User(String name, String email, String id, String gender, String profilePic, String fid, String bio, String photo1, String photo2, String photo3, String photo4,
                int karmaPoints, String pushToken, boolean showMen, boolean showWomen, int ageMin, int ageMax, int distance, double latitude, double longitude,
                String birthdate, String school, String work, boolean gaveFullBirthday, boolean whoseTreatPosterTreat, boolean whoseTreatDateTreat,
                boolean whoseTreatSplitBill, boolean whoseTreatDecideLater) {
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
        this.latitude = latitude;
        this.longitude = longitude;
        this.birthdate = birthdate;
        this.school = school;
        this.work = work;
        this.gaveFullBirthday = gaveFullBirthday;
        this.whoseTreatPosterTreat = whoseTreatPosterTreat;
        this.whoseTreatDateTreat = whoseTreatDateTreat;
        this.whoseTreatSplitBill = whoseTreatSplitBill;
        this.whoseTreatDecideLater = whoseTreatDecideLater;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public boolean isGaveFullBirthday() {
        return gaveFullBirthday;
    }

    public void setGaveFullBirthday(boolean gaveFullBirthday) {
        this.gaveFullBirthday = gaveFullBirthday;
    }

    public boolean isWhoseTreatPosterTreat() {
        return whoseTreatPosterTreat;
    }

    public void setWhoseTreatPosterTreat(boolean whoseTreatPosterTreat) {
        this.whoseTreatPosterTreat = whoseTreatPosterTreat;
    }

    public boolean isWhoseTreatDateTreat() {
        return whoseTreatDateTreat;
    }

    public void setWhoseTreatDateTreat(boolean whoseTreatDateTreat) {
        this.whoseTreatDateTreat = whoseTreatDateTreat;
    }

    public boolean isWhoseTreatSplitBill() {
        return whoseTreatSplitBill;
    }

    public void setWhoseTreatSplitBill(boolean whoseTreatSplitBill) {
        this.whoseTreatSplitBill = whoseTreatSplitBill;
    }

    public boolean isWhoseTreatDecideLater() {
        return whoseTreatDecideLater;
    }

    public void setWhoseTreatDecideLater(boolean whoseTreatDecideLater) {
        this.whoseTreatDecideLater = whoseTreatDecideLater;
    }
}
