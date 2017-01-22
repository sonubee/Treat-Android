package gllc.tech.dateapp.Objects;

import android.graphics.drawable.Drawable;

import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/22/2017.
 */

public class Categories {

    String displayName;
    String category;
    String defaultImage;

    public Categories(String displayName, String category, String defaultImage) {
        this.displayName = displayName;
        this.category = category;
        this.defaultImage = defaultImage;
    }

    public Categories() {
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

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }


}
