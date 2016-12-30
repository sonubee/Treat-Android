package gllc.tech.dateapp.Loading;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/29/2016.
 */

public class Filters extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_search_filter, container, false);

        return view;
    }
}
