package gllc.tech.dateapp.FacebookAlbums;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gllc.tech.dateapp.Profile;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/15/2016.
 */

public class DisplayFacebookAlbumsAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    AlbumHolder albumHolder = new AlbumHolder();

    public DisplayFacebookAlbumsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return Profile.albumIds.size();
    }

    @Override
    public Object getItem(int position) {
        return Profile.albumIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.facebook_album_adapter, null);

        albumHolder.albumImage = (ImageView)convertView.findViewById(R.id.albumImage);
        albumHolder.albumName = (TextView) convertView.findViewById(R.id.albumName);

        String link = Profile.albumIdToLink.get(Profile.albumIds.get(position));


        if (!link.equals("NA")) {
            Picasso.with(context).load(link).into(albumHolder.albumImage);
        }else{albumHolder.albumImage.setImageResource(R.drawable.placeholder);}

        albumHolder.albumName.setText(Profile.albumNames.get(position));

        return convertView;
    }

    static class AlbumHolder {
        ImageView albumImage;
        TextView albumName;
    }

}
