package gllc.tech.dateapp.CompletedDates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/19/2016.
 */

public class CompletedDatesAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    CompletedDatesHolder holder = new CompletedDatesHolder();

    public CompletedDatesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyApplication.completedDates.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.completedDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.completed_dates_adapter, null);

        holder.title = (TextView)convertView.findViewById(R.id.titleHereTextViewCompleted);
        holder.date = (TextView)convertView.findViewById(R.id.dateOfCompletedDate);
        holder.createdBy = (TextView)convertView.findViewById(R.id.createdByCompleted);
        holder.status = (TextView)convertView.findViewById(R.id.dateStatusCompleted);

        holder.title.setText(MyApplication.completedDates.get(position).getDateTitle());
        holder.date.setText(MyApplication.completedDates.get(position).getDateOfDate());

        if ((MyApplication.completedDates.get(position).getPoster().equals(MyApplication.currentUser.getId()) &&
                !MyApplication.completedDates.get(position).isPosterKarma()) || (MyApplication.completedDates.get(position).
                getTheDate().equals(MyApplication.currentUser.getId()) && !MyApplication.completedDates.get(position).isTheDateKarma())) {
            holder.status.setText("Click to Set Karma Points!");
        } else {
            holder.status.setText("Karma Points Set!");
        }

        for (int i=0; i < MyApplication.allUsers.size(); i++){
            if (MyApplication.allUsers.get(i).getId().equals(MyApplication.completedDates.get(position).getPoster())){
                holder.createdBy.setText("");
            }
        }



        return convertView;
    }

    static class CompletedDatesHolder {
        TextView title;
        TextView date;
        TextView createdBy;
        TextView status;
    }

}
