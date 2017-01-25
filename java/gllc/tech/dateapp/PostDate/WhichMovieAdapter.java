package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.tech.dateapp.Objects.PlacesDetails;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/24/2017.
 */

public class WhichMovieAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<String> movieNames;
    MovieHolder holder = new MovieHolder();

    public WhichMovieAdapter(Context context, ArrayList<String>movieNames) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.movieNames = movieNames;
    }


    @Override
    public int getCount() {
        return movieNames.size();
    }

    @Override
    public Object getItem(int position) {
        return movieNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.which_movie_adapter, null);

        holder.movieName = (TextView)convertView.findViewById(R.id.movieTitle);
        holder.movieName.setText(movieNames.get(position));

        return convertView;
    }

    static class MovieHolder {
        TextView movieName;
    }

}
