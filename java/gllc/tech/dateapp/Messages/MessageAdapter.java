package gllc.tech.dateapp.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.Message;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/6/2016.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    Context context;
    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    public static DatabaseReference myRef;
    public static ChildEventListener childEventListener;

    public MessageAdapter(Context context, int resource, ArrayList<Message> messages) {
        super(context, resource, messages);
        this.context = context;
        messageArrayList = messages;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Messages/" + MyApplication.dateSelectedKey + "/" + MessageFragment.messageKey);

        myRef.addChildEventListener(childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);
                messageArrayList.add(message);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.message_adapter2, parent, false);

        TextView text = (TextView) view.findViewById(R.id.message_text);

        if (messageArrayList.get(position).getSender().equals(MyApplication.otherPerson.getId())){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)text.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            text.setLayoutParams(params);
        }

        text.setText(messageArrayList.get(position).getMessage());

        text.setTextSize(15);

        return view;
    }
}
