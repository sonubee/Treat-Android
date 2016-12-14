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

    public User() {
    }

    public User(String name, String email, String id, String gender, String profilePic, String fid, String bio) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.gender = gender;
        this.profilePic = profilePic;
        this.fid = fid;
        this.bio = bio;
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

}
