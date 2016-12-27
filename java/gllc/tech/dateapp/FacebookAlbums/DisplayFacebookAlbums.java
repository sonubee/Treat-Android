package gllc.tech.dateapp.FacebookAlbums;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.Automation.Profile;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/15/2016.
 */

public class DisplayFacebookAlbums extends Fragment {

    ListView listView;
    DisplayFacebookAlbumsAdapter adapter;
    public static ArrayList<String> imageURLs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_facebook_albums, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new DisplayFacebookAlbumsAdapter(getContext());

        listView = (ListView)getActivity().findViewById(R.id.facebookAlbumListview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String albumId = Profile.albumIds.get(position);

                getAlbumImages(albumId);
            }
        });


    }

    public void getAlbumImages(String albumId) {
//        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        //parameters.putInt("limit", 5000);
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //Log.i("--All", "Response: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");
                                        if (jaImages.length() > 0) {
                                            imageURLs.add(jaImages.getJSONObject(0).getString("source"));
                                            //Log.i("--All", "Picture Link: "+jaImages.getJSONObject(0).getString("source"));
                                            //Log.i("--All", "Size Now: " + imageURLs.size());
                                        }
                                    }
                                    //set your adapter here
                                    //Log.i("--All", "Album Size: "+imageURLs.size());
                                    ((MainActivity)getActivity()).addFragments(DisplayAlbumImages.class, R.id.container, "Test");

                                }
                            } else {
                                Log.v("--All", "Error: " + response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }
}
