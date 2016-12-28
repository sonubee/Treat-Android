package gllc.tech.dateapp.SearchDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.tech.dateapp.Objects.EventsOfDate;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/4/2016.
 */

public class SearchDatesAdapter extends BaseAdapter {

    ArrayList<EventsOfDate> eventsOfDates;
    private LayoutInflater layoutInflater;
    EventDetail holder = new EventDetail();
    public SearchDatesAdapter(ArrayList<EventsOfDate> eventsOfDate, Context context) {
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

        convertView = layoutInflater.inflate(R.layout.listview_search_dates_adapter, null);

        holder.time = (TextView) convertView.findViewById(R.id.timeTextview);
        holder.event = (TextView) convertView.findViewById(R.id.whereTextView);

        holder.time.setText(eventsOfDates.get(position).getBeginTime() + " - " + eventsOfDates.get(position).getEndTime());
        holder.event.setText(eventsOfDates.get(position).getActivity() + " at " + eventsOfDates.get(position).getSpecific());

        return convertView;
    }

    static class EventDetail {
        TextView event;
        TextView time;
        ImageView eventImage;
    }
}
