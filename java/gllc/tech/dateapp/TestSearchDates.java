package gllc.tech.dateapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.EventsOfDate;

/**
 * Created by bhangoo on 1/9/2017.
 */

public class TestSearchDates extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_dates2, container, false);

        LinearLayout searchDatesLinearLayout = (LinearLayout)view.findViewById(R.id.searchDatesLinearLayout);

        ArrayList<EventsOfDate> events = MyApplication.allDates.get(2).getEvents();

        for (int i=0; i < events.size(); i++) {
            RelativeLayout eventAdapterLayout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.event_adapter3, null);

            TextView textView = (TextView)eventAdapterLayout.findViewById(R.id.eventTitleEventAdapter);
            textView.setText("Test");

            searchDatesLinearLayout.addView(eventAdapterLayout);
        }

        /*
        LinearLayout searchDatesLinearLayout = (LinearLayout)view.findViewById(R.id.searchDatesLinearLayout);

        TextView textView1 = new TextView(getContext());
        textView1.setText("Test");
        textView1.setTextColor(Color.WHITE);

        LinearLayout second = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.profile_review_details, null);
        searchDatesLinearLayout.addView(second);

        searchDatesLinearLayout.addView(textView1);

        ArrayList<EventsOfDate> events = MyApplication.allDates.get(0).getEvents();

        for (int i=0; i < events.size(); i++) {

            Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE1");

            //LayoutInflater inflater = LayoutInflater.from(getContext());
            //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //RelativeLayout eventAdapterLayout = (RelativeLayout)inflater.inflate(R.layout.event_adapter3, null, false);
            RelativeLayout eventAdapterLayout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.event_adapter3, null);

            //TextView textView = (TextView)eventAdapterLayout.findViewById(R.id.eventTitleEventAdapter);


            //searchDatesLinearLayout.addView(eventAdapterLayout);
        }
*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
