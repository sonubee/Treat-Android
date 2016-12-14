package gllc.tech.dateapp.Messages;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.R;
import gllc.tech.dateapp.UpComingDates.YourDatesAdapter;

/**
 * Created by bhangoo on 12/7/2016.
 */

public class ShowAllMessagesAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    MessageInfo holder = new MessageInfo();
    Context context;

    public ShowAllMessagesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyApplication.agreedChats.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.agreedChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.show_all_messages_adapter, null);

        holder.name = (TextView)convertView.findViewById(R.id.nameAllMessages);
        holder.title = (TextView)convertView.findViewById(R.id.titleOfDate);
        holder.image = (CircleImageView)convertView.findViewById(R.id.profilePicAllMessages);

        //for (int i=0; i < MyApplication.agreedChats.size(); i++){
            if (MyApplication.agreedChats.get(position).getPoster().equals(MyApplication.currentUser.getId())){
                //Log.i("--All", "Should be once");
                for (int j=0; j < MyApplication.allUsers.size(); j++){
                    if (MyApplication.allUsers.get(j).getId().equals(MyApplication.agreedChats.get(position).getRequester())){
                        holder.name.setText(MyApplication.allUsers.get(j).getName());
                        Picasso.with(context).load(MyApplication.allUsers.get(j).getProfilePic()).into(holder.image);
                        for (int k=0; k<MyApplication.allDates.size(); k++){
                            if (MyApplication.allDates.get(k).getKey().equals(MyApplication.agreedChats.get(position).getDateKey())){
                                holder.title.setText(MyApplication.allDates.get(k).getDateTitle());
                            }
                        }
                    }
                }
            }

            else {
                for (int j=0; j < MyApplication.allUsers.size(); j++){
                    if (MyApplication.allUsers.get(j).getId().equals(MyApplication.agreedChats.get(position).getPoster())){
                        holder.name.setText(MyApplication.allUsers.get(j).getName());
                        Picasso.with(context).load(MyApplication.allUsers.get(j).getProfilePic()).into(holder.image);

                        for (int k=0; k<MyApplication.allDates.size(); k++){
                            if (MyApplication.allDates.get(k).getKey().equals(MyApplication.agreedChats.get(position).getDateKey())){
                                holder.title.setText(MyApplication.allDates.get(k).getDateTitle());
                            }
                        }
                    }
                }
            }
        //}

        return convertView;
    }

    static class MessageInfo {
        TextView name;
        TextView title;
        CircleImageView image;
    }
}
