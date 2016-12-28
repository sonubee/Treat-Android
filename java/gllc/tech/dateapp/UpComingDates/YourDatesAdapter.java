package gllc.tech.dateapp.UpComingDates;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/5/2016.
 */

public class YourDatesAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    YourDatesHolder holder = new YourDatesHolder();

    public YourDatesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

        MyApplication.combinedDates.clear();

        for (int i = 0; i < MyApplication.fullMatchesAsCreator.size(); i ++){
            MyApplication.combinedDates.add(MyApplication.fullMatchesAsCreator.get(i));
        }

        for (int i = 0; i < MyApplication.fullMatchesAsDate.size(); i ++){
            MyApplication.combinedDates.add(MyApplication.fullMatchesAsDate.get(i));
        }

        for (int i = 0; i < MyApplication.pendingDates.size(); i ++){
            MyApplication.combinedDates.add(MyApplication.pendingDates.get(i));
        }
    }

    @Override
    public int getCount() {
        return MyApplication.combinedDates.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.combinedDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.your_dates_adapter, null);

        holder.title = (TextView) convertView.findViewById(R.id.titleHereTextview);
        holder.date = (TextView)convertView.findViewById(R.id.dateOfDate);
        holder.createdBy = (TextView)convertView.findViewById(R.id.createdBy);
        holder.dateStatus = (TextView)convertView.findViewById(R.id.dateStatus);

        holder.date.setText(MyApplication.combinedDates.get(position).getDateOfDate());

        for (int i=0; i < MyApplication.allUsers.size(); i++){
            if (MyApplication.allUsers.get(i).getId().equals(MyApplication.combinedDates.get(position).getPoster())){
                //holder.createdBy.setText("Created By: "+MyApplication.allUsers.get(i).getName());
                holder.createdBy.setText("");
            }
        }

        holder.title.setText(MyApplication.combinedDates.get(position).getDateTitle());
        holder.title.setTextColor(Color.CYAN);



        if (MyApplication.fullMatchesAsCreatorHashMap.get(MyApplication.combinedDates.get(position).getKey()) != null){
            if (MyApplication.combinedDates.get(position).getKey().equals(MyApplication.fullMatchesAsCreatorHashMap.get(MyApplication.combinedDates.get(position).getKey()).getKey())){
                holder.dateStatus.setText("You Chose Your Date!");
            }
        }

        if (MyApplication.fullMatchesAsDateHashMap.get(MyApplication.combinedDates.get(position).getKey()) != null){
            if (MyApplication.combinedDates.get(position).getKey().equals(MyApplication.fullMatchesAsDateHashMap.get(MyApplication.combinedDates.get(position).getKey()).getKey())){
                holder.dateStatus.setText("You Got The Date!");
            }
        }

        if (MyApplication.pendingDatesHashMap.get(MyApplication.combinedDates.get(position).getKey()) != null){
            if (MyApplication.combinedDates.get(position).getKey().equals(MyApplication.pendingDatesHashMap.get(MyApplication.combinedDates.get(position).getKey()).getKey())){
                holder.dateStatus.setText("No Date Chosen!");
            }
        }

        return convertView;
    }

    static class YourDatesHolder {
        TextView title;
        TextView date;
        TextView createdBy;
        TextView dateStatus;
    }
}
