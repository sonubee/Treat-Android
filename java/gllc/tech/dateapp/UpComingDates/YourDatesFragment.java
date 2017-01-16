package gllc.tech.dateapp.UpComingDates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gllc.tech.dateapp.Loading.MainActivity;
import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/5/2016.
 */

public class YourDatesFragment extends Fragment {

    ListView listView;
    public static YourDatesAdapter adapter;
    public static int selectedMap;
    //public static int chosenDateToReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_dates, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        adapter = new YourDatesAdapter(getContext());

        listView = (ListView) getActivity().findViewById(R.id.yourDatesListview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.dateSelected = MyApplication.combinedDates.get(position);
                MyApplication.dateSelectedKey = MyApplication.combinedDates.get(position).getKey();

                selectedMap = position;

                Bundle bundle = new Bundle();
                bundle.putString("dateSelectedKey", MyApplication.dateSelectedKey);

                ((MainActivity)getActivity()).addFragments(DateReviewFragment.class, R.id.container, "DatesReview", bundle);
            }
        });
    }

    public static YourDatesFragment newInstance() {

        YourDatesFragment f = new YourDatesFragment();
               return f;
    }

}
