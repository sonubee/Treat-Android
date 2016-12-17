package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by bhangoo on 12/16/2016.
 */

public class DisplayAlbumImages extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test_layout, container, false);

        ((MainActivity)getActivity()).popBackStack();

        Log.i("--All", "FIIIIIIIIIIIIIIIIIINDMEEEE11111");
/*
        GridView gridview = (GridView) view.findViewById(R.id.albumImagesGridview);
        gridview.setAdapter(new AlbumImagesAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
