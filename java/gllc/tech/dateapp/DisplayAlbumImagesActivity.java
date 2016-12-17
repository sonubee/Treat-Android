package gllc.tech.dateapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by bhangoo on 12/16/2016.
 */

public class DisplayAlbumImagesActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_album_images);

        GridView gridview = (GridView) findViewById(R.id.albumImagesGridview);
        gridview.setAdapter(new AlbumImagesAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(DisplayAlbumImagesActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
