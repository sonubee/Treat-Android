package gllc.tech.dateapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhangoo on 12/16/2016.
 */

class DisplayAlbumImagesAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    Context context;

    public DisplayAlbumImagesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
        }

        picture = (ImageView) v.getTag(R.id.picture);

        Picasso.with(context).load(DisplayFacebookAlbums.imageURLs.get(i)).into(picture);

        return v;
    }

}
