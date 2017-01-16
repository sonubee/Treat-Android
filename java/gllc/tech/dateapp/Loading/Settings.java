package gllc.tech.dateapp.Loading;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/7/2017.
 */

public class Settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        return view;
    }

    public static Settings newInstance() {

        Settings f = new Settings();
        return f;
    }
}
