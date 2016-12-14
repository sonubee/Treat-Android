package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    public EventAdapter(Context context, ArrayList<EventsOfDate> listOfEvents){
        layoutInflater = LayoutInflater.from(context);
        this.listOfEvents = listOfEvents;
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

        convertView = layoutInflater.inflate(R.layout.event_adapter, null);

        //holder.beginTime = (TextView) convertView.findViewById(R.id.beginTime);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.activity = (TextView) convertView.findViewById(R.id.activity);
        //holder.specific = (TextView) convertView.findViewById(R.id.specific);

        //holder.beginTime.setText(listOfEvents.get(position).getBeginTime());
        //holder.specific.setText(listOfEvents.get(position).getSpecific());

        holder.time.setText(listOfEvents.get(position).getBeginTime() + " - " + listOfEvents.get(position).getEndTime());
        holder.activity.setText(listOfEvents.get(position).getActivity() + " at " + listOfEvents.get(position).getSpecific());

        return convertView;
    }

    static class EventHolder {
        TextView location;
        TextView activity;
        TextView beginTime;
        TextView time;
    }
}
