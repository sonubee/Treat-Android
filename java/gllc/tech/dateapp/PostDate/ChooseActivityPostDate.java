package gllc.tech.dateapp.PostDate;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ListView;

import com.dd.processbutton.FlatButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.PlacesDetails;
import gllc.tech.dateapp.PostDate.CreateEvent3;
import gllc.tech.dateapp.PostDate.DisplayActivityGridViewAdapter;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/9/2017.
 */

public class ChooseActivityPostDate extends Fragment{

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
                bundle.putString("activitySelected", MyApplication.categories.get(position).getDisplayName());

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
