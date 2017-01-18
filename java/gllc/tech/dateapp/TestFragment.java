package gllc.tech.dateapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.PostDate.CreateEvent3;
import gllc.tech.dateapp.PostDate.DisplayActivityGridViewAdapter;

/**
 * Created by bhangoo on 1/9/2017.
 */

public class TestFragment extends Fragment{
    String[] activitiesString = new String[] {"Bowling", "Coffee", "Concert", "Dinner", "Event", "Lunch", "Go-Kart", "Minigolf", "Music Festival",
            "Hike", "Movie", "Walk", "Other", "FastFood"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_choose_activity, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.chooseActivityGridView);
        gridview.setAdapter(new DisplayActivityGridViewAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("activitySelected", activitiesString[position]);

                ((MainActivity)getActivity()).addFragments(CreateEvent3.class, R.id.container, "CreateEvent", bundle);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
