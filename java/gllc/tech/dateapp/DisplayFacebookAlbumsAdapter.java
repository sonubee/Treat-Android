package gllc.tech.dateapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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


        Log.i("--All", "Album ID: " +Profile.albumIds.get(position));

        String link = Profile.albumIdToLink.get(Profile.albumIds.get(position));

        Log.i("--All", "Putting Picture: " + link);

        try{
            if (!link.equals("NA")) {
                Picasso.with(context).load(link).into(albumHolder.albumImage);
            }else{
                Log.i("--All", "Putting X");
                albumHolder.albumImage.setImageResource(R.drawable.no);
            }
        }catch (Exception e){
            albumHolder.albumImage.setImageResource(R.drawable.no);
        }

        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE"+ Profile.albumNames.size());
        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE"+Profile.albumIds.size());


        albumHolder.albumName.setText(Profile.albumNames.get(position));

        return convertView;
    }

    static class AlbumHolder {
        ImageView albumImage;
        TextView albumName;
    }

}
