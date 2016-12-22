package gllc.tech.dateapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bhangoo on 12/19/2016.
 */

public class CompletedDates extends Fragment {

    ListView listView;
    public static CompletedDatesAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_dates, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        adapter = new CompletedDatesAdapter(getContext());

        listView = (ListView) getActivity().findViewById(R.id.completedDatesListview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Date Karma")
                        .setMessage("Did You Enjoy the Date?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Log.i("--All", "Current Karma: "+MyApplication.currentUser.getKarmaPoints());

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                if (MyApplication.completedDates.get(position).getPoster().equals(MyApplication.currentUser.getId())) {
                                    MyApplication.completedDates.get(position).setPosterKarma(true);
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("CompletedDates/"+MyApplication.completedDates.get(position).
                                            getKey()+"/posterKarma");
                                    databaseReference.setValue(true);

                                    if (MyApplication.completedDates.get(position).isPosterKarma() && MyApplication.completedDates.get(position).isTheDateKarma()) {
                                        int karma = MyApplication.currentUser.getKarmaPoints();
                                        karma++;
                                        MyApplication.currentUser.setKarmaPoints(karma);

                                        editor = preferences.edit();
                                        editor.putInt("karmaPoints", karma);
                                        editor.apply();

                                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/karmaPoints");
                                        databaseReference1.setValue(karma);

                                        int dateKarma = MyApplication.userHashMap.get(MyApplication.completedDates.get(position).getTheDate()).getKarmaPoints();
                                        dateKarma++;
                                        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Users/"+MyApplication.completedDates.get(position).
                                                getTheDate()+"/karmaPoints");
                                        databaseReference2.setValue(dateKarma);
                                    }
                                }

                                if (MyApplication.completedDates.get(position).getTheDate().equals(MyApplication.currentUser.getId())) {
                                    MyApplication.completedDates.get(position).setTheDateKarma(true);
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("CompletedDates/"+MyApplication.completedDates.get(position).
                                            getKey()+"/theDateKarma");
                                    databaseReference.setValue(true);

                                    if (MyApplication.completedDates.get(position).isPosterKarma() && MyApplication.completedDates.get(position).isTheDateKarma()) {
                                        Log.i("--All", "Karma Here: " + MyApplication.currentUser.getKarmaPoints());
                                        int karma = MyApplication.currentUser.getKarmaPoints();
                                        karma++;
                                        MyApplication.currentUser.setKarmaPoints(karma);

                                        editor = preferences.edit();
                                        editor.putInt("karmaPoints", karma);
                                        editor.apply();

                                        Log.i("--All", "int Karma: " + karma);

                                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/karmaPoints");
                                        databaseReference1.setValue(karma);

                                        int posterKarma = MyApplication.userHashMap.get(MyApplication.completedDates.get(position).getPoster()).getKarmaPoints();
                                        posterKarma++;
                                        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Users/"+MyApplication.completedDates.get(position).
                                                getPoster()+"/karmaPoints");
                                        databaseReference2.setValue(posterKarma);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
