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

/**
 * Created by bhangoo on 12/16/2016.
 */

public class DisplayAlbumImages extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.display_album_images, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.albumImagesGridView);
        gridview.setAdapter(new DisplayAlbumImagesAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.selectedImageUrl = DisplayFacebookAlbums.imageURLs.get(position);
                ((MainActivity)getActivity()).addFragments(FullImageFragment.class, R.id.container, "Another");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DisplayFacebookAlbums.imageURLs.clear();
    }
}
