package gllc.tech.dateapp.SearchDate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/29/2016.
 */

public class Filters extends Fragment {

    CheckBox showMen, showWomen, posterTreat, dateTreat, splitBill, decideLater;
    RangeSeekBar<Integer> distanceSeekBar, ageSeekBar;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_search_filter, container, false);

        distanceSeekBar = (RangeSeekBar)view.findViewById(R.id.distanceSeekBar);
        ageSeekBar = (RangeSeekBar)view.findViewById(R.id.ageSeekBar);
        showMen = (CheckBox)view.findViewById(R.id.menCheckBox);
        showWomen = (CheckBox)view.findViewById(R.id.womenCheckBox);
        posterTreat = (CheckBox)view.findViewById(R.id.posterTreat);
        dateTreat = (CheckBox)view.findViewById(R.id.dateTreat);
        splitBill = (CheckBox)view.findViewById(R.id.splitBill);
        decideLater = (CheckBox)view.findViewById(R.id.decideLater);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        showMen.setChecked(MyApplication.currentUser.isShowMen());
        showWomen.setChecked(MyApplication.currentUser.isShowWomen());
        posterTreat.setChecked(MyApplication.currentUser.isWhoseTreatPosterTreat());
        dateTreat.setChecked(MyApplication.currentUser.isWhoseTreatDateTreat());
        splitBill.setChecked(MyApplication.currentUser.isWhoseTreatSplitBill());
        decideLater.setChecked(MyApplication.currentUser.isWhoseTreatDecideLater());


        showMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setShowMen(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/showMen");
                databaseReference.setValue(isChecked);
            }
        });

        showWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setShowWomen(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/showWomen");
                databaseReference.setValue(isChecked);
            }
        });

        posterTreat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setWhoseTreatPosterTreat(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/whoseTreatPosterTreat");
                databaseReference.setValue(isChecked);
            }
        });

        dateTreat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setWhoseTreatDateTreat(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/whoseTreatDateTreat");
                databaseReference.setValue(isChecked);
            }
        });

        splitBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setWhoseTreatSplitBill(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/whoseTreatSplitBill");
                databaseReference.setValue(isChecked);
            }
        });

        decideLater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.currentUser.setWhoseTreatDecideLater(isChecked);
                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/whoseTreatDecideLater");
                databaseReference.setValue(isChecked);
            }
        });

        distanceSeekBar.setSelectedMaxValue(MyApplication.currentUser.getDistance());
        ageSeekBar.setSelectedMaxValue(MyApplication.currentUser.getAgeMax());
        ageSeekBar.setSelectedMinValue(MyApplication.currentUser.getAgeMin());

        distanceSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                MyApplication.currentUser.setDistance(maxValue);

                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/distance");
                databaseReference.setValue(maxValue);
            }
        });

        ageSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                MyApplication.currentUser.setAgeMin(minValue);
                MyApplication.currentUser.setAgeMax(maxValue);

                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/ageMax");
                databaseReference.setValue(maxValue);

                databaseReference = firebaseDatabase.getReference("Users/"+MyApplication.currentUser.getId()+"/ageMin");
                databaseReference.setValue(minValue);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static Filters newInstance() {

        Filters f = new Filters();
        return f;
    }
}
