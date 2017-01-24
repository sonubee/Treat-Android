package gllc.tech.dateapp.PostDate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 1/14/2017.
 */

public class DisplayActivityGridViewAdapter extends BaseAdapter {


    private final LayoutInflater mInflater;
    Context context;

    public DisplayActivityGridViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return MyApplication.categories.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView activity;
        ImageView picture;

        if (v == null) {
            v = mInflater.inflate(R.layout.gridview_adapter_choose_activity, parent, false);
            v.setTag(R.id.gridImageView, v.findViewById(R.id.gridImageView));
        }

        picture = (ImageView) v.getTag(R.id.gridImageView);
        activity = (TextView) v.findViewById(R.id.gridActivityName);

        String pic = MyApplication.categoriesMap.get(MyApplication.categories.get(position).getDisplayName()).getDefaultImage();
        int id = context.getResources().getIdentifier(pic, "drawable", context.getPackageName());
        picture.setImageResource(id);

        activity.setText(MyApplication.categories.get(position).getDisplayName());

        return v;
    }
}
