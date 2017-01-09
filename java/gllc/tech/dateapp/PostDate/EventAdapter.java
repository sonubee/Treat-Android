package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/1/2016.
 */
public class EventAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private EventHolder holder = new EventHolder();
    ArrayList<EventsOfDate> listOfEvents = new ArrayList<>();
    Context context;

    public EventAdapter(Context context, ArrayList<EventsOfDate> listOfEvents){
        layoutInflater = LayoutInflater.from(context);
        this.listOfEvents = listOfEvents;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listOfEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.event_adapter3, null);


        holder.time = (TextView) convertView.findViewById(R.id.timeEventAdapter);
        holder.activity = (TextView) convertView.findViewById(R.id.eventTitleEventAdapter);
        holder.imageView = (ImageView)convertView.findViewById(R.id.imageEventAdapter);
        holder.address = (TextView)convertView.findViewById(R.id.addressEventAdapter);

        holder.time.setText(listOfEvents.get(position).getBeginTime() + " - " + listOfEvents.get(position).getEndTime());
        holder.activity.setText(listOfEvents.get(position).getActivity() + " at " + listOfEvents.get(position).getSpecific());
        holder.address.setText(listOfEvents.get(position).getCity());
        Picasso.with(context).load(listOfEvents.get(position).getPhoto()).into(holder.imageView);

        return convertView;
    }

    static class EventHolder {
        TextView activity;
        TextView time;
        ImageView imageView;
        TextView address;
    }
}
