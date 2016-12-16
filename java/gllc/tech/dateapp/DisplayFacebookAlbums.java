package gllc.tech.dateapp;

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

/**
 * Created by bhangoo on 12/15/2016.
 */

public class DisplayFacebookAlbums extends Fragment {

    ListView listView;
    DisplayFacebookAlbumsAdapter adapter;
    ArrayList<String> imageURLs = new ArrayList<>();

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

                //GetFacebookImages(albumId); //find Album ID and get All Images from album
            }
        });
    }

    public void GetFacebookImages(final String albumId) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "cover_photo");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.v("--All", "Facebook Photos response: " + response);
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObject = response.getJSONObject();

                                Log.i("--All", "JSON: " + jsonObject.toString());

                                if (jsonObject.has("cover_photo")) {

                                    JSONObject coverPhotoJSON = jsonObject.getJSONObject("cover_photo");
                                    String coverPhotoId = coverPhotoJSON.get("id").toString();

                                    Log.i("--All", "Cover Photo ID: " + coverPhotoId);

                                    getCoverPhotoPicture(coverPhotoId, albumId);
                                }

                            } else {
                                Log.v("--All", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

    public void getCoverPhotoPicture(final String coverPhotoId, String albumId) {
        //        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //Log.v("--All", "Facebook Photos response: " + response);
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();

                                Log.i("--All", "JSON: "+ joMain.toString());
                                Log.i("--All", "Cover Photo ID: " + coverPhotoId);

                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");// get images Array in JSONArray format
                                        if (jaImages.length() > 0) {
                                            imageURLs.add(jaImages.getJSONObject(0).getString("source"));
                                            Log.i("--All", "Picture Link: "+jaImages.getJSONObject(0).getString("source"));
                                        }
                                    }

                                    //set your adapter here
                                }

                            } else {
                                Log.v("--All", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();

    }
}
