package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.tech.dateapp.Automation.SimpleCalculations;
import gllc.tech.dateapp.Objects.PlacesDetails;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/13/2017.
 */

public class PostDateSuggestionsAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<PlacesDetails> placesDetailsArrayList;
    PlacesDetailsHolder holder = new PlacesDetailsHolder();

    public PostDateSuggestionsAdapter(Context context, ArrayList<PlacesDetails> placesDetailsArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.placesDetailsArrayList = placesDetailsArrayList;
    }

    @Override
    public int getCount() {
        return placesDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return placesDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.choose_location_adapter, null);

        holder.name = (TextView)convertView.findViewById(R.id.nameChooseLocation);
        holder.city = (TextView)convertView.findViewById(R.id.cityChooseLocation);
        holder.reviews = (TextView)convertView.findViewById(R.id.reviewsChooseLocation);
        holder.placeImage = (ImageView)convertView.findViewById(R.id.placeImage);

        holder.name.setText(placesDetailsArrayList.get(position).getName());
        holder.city.setText(placesDetailsArrayList.get(position).getCity() + " - " +
                SimpleCalculations.GetTheDistanceOnePoint(placesDetailsArrayList.get(position).getLatitude(), placesDetailsArrayList.get(position).getLongitude()) +
                " Miles From You");
        holder.reviews.setText("Reviews: "+placesDetailsArrayList.get(position).getReviews() + " - Rating: " + placesDetailsArrayList.get(position).getRating());

        if (!placesDetailsArrayList.get(position).getPhoto().equals("NA") && !placesDetailsArrayList.get(position).getPhoto().equals("")) {
            Picasso.with(context).load(placesDetailsArrayList.get(position).getPhoto()).into(holder.placeImage);
        }

        return convertView;
    }


    static class PlacesDetailsHolder {
        ImageView placeImage;
        TextView name;
        TextView city;
        TextView reviews;
    }
}
