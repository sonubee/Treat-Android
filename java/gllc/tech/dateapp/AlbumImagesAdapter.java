package gllc.tech.dateapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by bhangoo on 12/16/2016.
 */

public class AlbumImagesAdapter extends BaseAdapter {

    private Context mContext;

    public AlbumImagesAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return DisplayFacebookAlbums.imageURLs.size();
    }

    public Object getItem(int position) {
        return DisplayFacebookAlbums.imageURLs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, GridView.LayoutParams.FILL_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(DisplayFacebookAlbums.imageURLs.get(position)).into(imageView);

        Log.i("--All", "Position: " + position);
        Log.i("--All", "Size: " + DisplayFacebookAlbums.imageURLs.size());
        return imageView;
    }
}
