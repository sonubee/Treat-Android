package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by bhangoo on 12/31/2016.
 */

public class TestProfile extends Fragment{

    private ViewGroup mLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile, container, false);

        LinearLayout test = (LinearLayout)view.findViewById(R.id.mainLinearLayout);
        LinearLayout second = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.about_you, null);
        test.addView(second);

        return view;
    }
}
