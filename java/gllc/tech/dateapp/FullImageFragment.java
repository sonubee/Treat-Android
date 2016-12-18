package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by bhangoo on 12/14/2016.
 */

public class FullImageFragment extends Fragment {

    ImageView imageView;
    Button selectButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_screen_image, container, false);

        imageView = (ImageView)view.findViewById(R.id.fullScreenImage);
        selectButton = (Button)view.findViewById(R.id.selectPhotoButton);

        Picasso.with(getContext()).load(MyApplication.selectedImageUrl).into(imageView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).popAllFragments();
                ((MainActivity)getActivity()).changePhotos(2, MyApplication.selectedImageUrl);
            }
        });
    }
}
