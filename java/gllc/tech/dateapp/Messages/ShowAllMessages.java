package gllc.tech.dateapp.Messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.Objects.AgreedChats;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/7/2016.
 */

public class ShowAllMessages extends Fragment {

    ShowAllMessagesAdapter2 adapter;
    ListView listView;
    public static ArrayList<AgreedChats> agreedChatsArrayList;
    public static ArrayList<AgreedChats> agreedChatsArrayListCopy;
    public static ChildEventListener childEventListener;
    public static DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_all_messages, container, false);

        agreedChatsArrayList = new ArrayList<>();
        agreedChatsArrayListCopy = agreedChatsArrayList;

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ShowAllMessagesAdapter2(getContext(), R.id.showAllMessagesListview, agreedChatsArrayListCopy);

        adapter.notifyDataSetChanged();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("AgreedChats/" + MyApplication.currentUser.getId());

        myRef.addChildEventListener(childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AgreedChats chat = dataSnapshot.getValue(AgreedChats.class);
                agreedChatsArrayListCopy.add(chat);
                adapter.notifyDataSetChanged();
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



        listView = (ListView) getActivity().findViewById(R.id.showAllMessagesListview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyApplication.dateSelectedKey = ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getDateKey();

                if (MyApplication.dateHashMap.containsKey(agreedChatsArrayList.get(position).getDateKey())) {
                    MyApplication.dateSelected = MyApplication.dateHashMap.get(ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getDateKey());
                }
                if (MyApplication.completedDatesHashMap.containsKey(agreedChatsArrayList.get(position).getDateKey())) {
                    MyApplication.dateSelected = MyApplication.completedDatesHashMap.get(ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getDateKey());
                }

                if (ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getPoster().equals(MyApplication.currentUser.getId())) {
                    MyApplication.otherPerson = MyApplication.userHashMap.get(ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getRequester());
                } else {
                    MyApplication.otherPerson = MyApplication.userHashMap.get(ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getPoster());
                }

                ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
/*
                for (int i = 0; i< MyApplication.allDates.size(); i++)
                {
                    if (MyApplication.allDates.get(i).getKey().equals(ShowAllMessagesAdapter2.agreedChatsArrayList.get(position).getDateKey())){
                        MyApplication.dateSelected = MyApplication.allDates.get(i);
                        MyApplication.dateSelectedKey = MyApplication.allDates.get(i).getKey();

                        for (int j=0; j < ShowAllMessagesAdapter2.agreedChatsArrayList.size(); j++){
                            if (ShowAllMessagesAdapter2.agreedChatsArrayList.get(j).getPoster().equals(MyApplication.currentUser.getId())){
                                for (int k=0; k < MyApplication.allUsers.size(); k++){
                                    if (MyApplication.allUsers.get(k).getId().equals(ShowAllMessagesAdapter2.agreedChatsArrayList.get(j).getRequester())){
                                        MyApplication.otherPerson = MyApplication.allUsers.get(k);
                                        ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                                    }
                                }
                            }

                            else{
                                for (int k=0; k < MyApplication.allUsers.size(); k++) {
                                    if (MyApplication.allUsers.get(k).getId().equals(ShowAllMessagesAdapter2.agreedChatsArrayList.get(j).getPoster())) {
                                        MyApplication.otherPerson = MyApplication.allUsers.get(k);
                                        ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                                    }
                                }
                            }
                        }
                    }
                }

                */
                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.container, "ShowAllMessages");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("--All", "On Destroy from SAM");
        //ShowAllMessagesAdapter2.myRef.removeEventListener(ShowAllMessagesAdapter2.childEventListener);
        //ShowAllMessagesAdapter2.agreedChatsArrayList.clear();
        agreedChatsArrayList.clear();
        adapter.notifyDataSetChanged();
        listView.setAdapter(null);
        listView.invalidateViews();

    }
}