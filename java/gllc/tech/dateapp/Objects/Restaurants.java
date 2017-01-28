package gllc.tech.dateapp.Objects;

/**
 * Created by bhangoo on 1/25/2017.
 */

public class Restaurants {

    String displayName;
    String category;

    public Restaurants() {
    }

    public Restaurants(String category, String displayName) {
        this.category = category;
        this.displayName = displayName;

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
