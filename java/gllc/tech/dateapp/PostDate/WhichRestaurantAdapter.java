package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/25/2017.
 */

public class WhichRestaurantAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    RestaurantHolder holder = new RestaurantHolder();

    public WhichRestaurantAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyApplication.restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.which_restaurant_adapter, null);

        holder.restaurantType = (TextView)convertView.findViewById(R.id.restaurantType);

        holder.restaurantType.setText(MyApplication.restaurants.get(position).getDisplayName());

        return convertView;
    }

    static class RestaurantHolder {
        TextView restaurantType;
    }

}
