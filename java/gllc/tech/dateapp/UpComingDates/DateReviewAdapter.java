package gllc.tech.dateapp.UpComingDates;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/5/2016.
 */

public class DateReviewAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    ArrayList<EventsOfDate> eventsOfDates;
    private EventsHolder holder1 = new EventsHolder();

    public DateReviewAdapter(Context context, ArrayList<EventsOfDate> eventsOfDate) {
        layoutInflater = LayoutInflater.from(context);
        this.eventsOfDates = eventsOfDate;
    }

    @Override
    public int getCount() {
        return eventsOfDates.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsOfDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.event_adapter, null);

        //holder1.beginTime = (TextView) convertView.findViewById(R.id.beginTime);
        holder1.time = (TextView) convertView.findViewById(R.id.time);
        holder1.activity = (TextView) convertView.findViewById(R.id.activity);
        //holder1.location = (TextView) convertView.findViewById(R.id.location);

        //holder1.beginTime.setText(eventsOfDates.get(position).getBeginTime());
        //holder1.endTime.setText(eventsOfDates.get(position).getEndTime());
        //holder1.location.setText(eventsOfDates.get(position).getSpecific());

        holder1.time.setText(eventsOfDates.get(position).getBeginTime() + " - " + eventsOfDates.get(position).getEndTime());
        holder1.activity.setText(eventsOfDates.get(position).getActivity() + " at " + eventsOfDates.get(position).getSpecific());

        holder1.activity.setTextColor(Color.CYAN);

        return convertView;
    }

    static class EventsHolder {
        TextView beginTime;
        TextView time;
        TextView activity;
        TextView location;
    }
}
