package gllc.tech.dateapp.Messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gllc.tech.dateapp.MainActivity;
import gllc.tech.dateapp.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/7/2016.
 */

public class ShowAllMessages extends Fragment {

    ShowAllMessagesAdapter adapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_all_messages, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ShowAllMessagesAdapter(getContext());

        listView = (ListView) getActivity().findViewById(R.id.showAllMessagesListview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i< MyApplication.allDates.size(); i++)
                {
                    if (MyApplication.allDates.get(i).getKey().equals(MyApplication.agreedChats.get(position).getDateKey())){
                        MyApplication.dateSelected = MyApplication.allDates.get(i);
                        MyApplication.dateSelectedKey = MyApplication.allDates.get(i).getKey();

                        for (int j=0; j < MyApplication.agreedChats.size(); j++){
                            if (MyApplication.agreedChats.get(j).getPoster().equals(MyApplication.currentUser.getId())){
                                for (int k=0; k < MyApplication.allUsers.size(); k++){
                                    if (MyApplication.allUsers.get(k).getId().equals(MyApplication.agreedChats.get(j).getRequester())){
                                        MyApplication.otherPerson = MyApplication.allUsers.get(k);
                                        ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                                    }
                                }
                            }

                            else{
                                for (int k=0; k < MyApplication.allUsers.size(); k++) {
                                    if (MyApplication.allUsers.get(k).getId().equals(MyApplication.agreedChats.get(j).getPoster())) {
                                        MyApplication.otherPerson = MyApplication.allUsers.get(k);
                                        ((MainActivity)getActivity()).saveUser(MyApplication.otherPerson);
                                    }
                                }
                            }
                        }
                    }
                }
                ((MainActivity)getActivity()).addFragments(MessageFragment.class, R.id.showAllMessagesLayout, "ShowAllMessages");
            }
        });
    }
}