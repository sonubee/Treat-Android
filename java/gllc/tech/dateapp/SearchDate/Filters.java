package gllc.tech.dateapp.SearchDate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import gllc.tech.dateapp.Loading.MyApplication;
import gllc.tech.dateapp.Objects.Categories;
import gllc.tech.dateapp.Objects.CategoryPreferences;
import gllc.tech.dateapp.R;

/**
 * Created by bhangoo on 12/29/2016.
 */

public class Filters extends Fragment {

    CheckBox showMen, showWomen, posterTreat, dateTreat, splitBill, decideLater;
    RangeSeekBar<Integer> distanceSeekBar, ageSeekBar;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    LinearLayout categoryLayout;

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
        categoryLayout = (LinearLayout)view.findViewById(R.id.categoryLayout);

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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Categories");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Categories categories = dataSnapshot.getValue(Categories.class);
                MyApplication.categories.add(categories);
                MyApplication.categoriesMap.put(categories.getDisplayName(), categories);
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
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int extraRow = MyApplication.categories.size()%3;

                int numRows;

                if (extraRow > 0) {
                    numRows = ((MyApplication.categories.size() / 3) + 1);
                } else {
                    numRows = MyApplication.categories.size()/3;
                }

                int startZero = 0;

                for (int j=0; j <numRows; j++) {
                    LinearLayout addRow = new LinearLayout(getContext());
                    addRow.setOrientation(LinearLayout.HORIZONTAL);


                    for (int k=0; k < 3; k++) {

                        if (startZero < MyApplication.categories.size()) {
                            AppCompatCheckBox category = new AppCompatCheckBox(getContext());

                            category.setTag(MyApplication.categories.get(startZero).getDisplayName());

                            category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    databaseReference = firebaseDatabase.getReference("Preferences/"+MyApplication.currentUser.getId()+"/"+
                                            MyApplication.categoriesMap.get(buttonView.getTag().toString()).getCategory());
                                    databaseReference.setValue(isChecked);

                                    if (buttonView.getTag().toString().equals("Bowling")) {
                                        //MyApplication.categoryPreferences.setBowling(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Coffee")) {
                                        //MyApplication.categoryPreferences.setCoffee(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Dog Park")) {
                                        //MyApplication.categoryPreferences.setDog_parks(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Go-Kart")) {
                                        //MyApplication.categoryPreferences.setGokarts(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Hiking")) {
                                        //MyApplication.categoryPreferences.setHiking(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Horseback Riding")) {
                                        //MyApplication.categoryPreferences.setHorsebackriding(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Fast Food")) {
                                        //MyApplication.categoryPreferences.setHotdogs(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Mini Golf")) {
                                        //MyApplication.categoryPreferences.setMini_golf(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Movie")) {
                                        //MyApplication.categoryPreferences.setMovietheaters(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Restaurants")) {
                                        //MyApplication.categoryPreferences.setRestaurants(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Skydiving")) {
                                        //MyApplication.categoryPreferences.setSkydiving(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Snorkeling")) {
                                        //MyApplication.categoryPreferences.setSnorkeling(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Tennis")) {
                                        //MyApplication.categoryPreferences.setTennis(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Zipline")) {
                                        //MyApplication.categoryPreferences.setZipline(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }

                                    if (buttonView.getTag().toString().equals("Zoo")) {
                                        //MyApplication.categoryPreferences.setZoos(isChecked);
                                        MyApplication.categoryPreferencesValue.put(buttonView.getTag().toString(), isChecked);
                                    }
                                }
                            });

                            category.setText(MyApplication.categories.get(startZero).getDisplayName());
                            startZero++;
                            category.setTextColor(Color.WHITE);
                            category.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));


                            addRow.addView(category);
                        }
                    }
                    categoryLayout.addView(addRow);
                }

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Preferences/"+MyApplication.currentUser.getId());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CategoryPreferences categoryPreferences = dataSnapshot.getValue(CategoryPreferences.class);
                        //MyApplication.categoryPreferences = categoryPreferences;

                        if (categoryPreferences.isBowling()) {
                            MyApplication.categoryPreferencesValue.put("Bowling", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) categoryLayout.findViewWithTag("Bowling");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Bowling", false);
                        }

                        if (categoryPreferences.isCoffee()) {
                            MyApplication.categoryPreferencesValue.put("Coffee", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Coffee");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Coffee", false);
                        }

                        if (categoryPreferences.isDog_parks()) {
                            MyApplication.categoryPreferencesValue.put("Dog Park", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Dog Park");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Dog Park", false);
                        }

                        if (categoryPreferences.isHotdogs()) {
                            MyApplication.categoryPreferencesValue.put("Fast Food", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Fast Food");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Fast Food", false);
                        }

                        if (categoryPreferences.isGokarts()) {
                            MyApplication.categoryPreferencesValue.put("Go-Kart", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Go-Kart");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Go-Kart", false);
                        }

                        if (categoryPreferences.isHiking()) {
                            MyApplication.categoryPreferencesValue.put("Hiking", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Hiking");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Hiking", false);
                        }

                        if (categoryPreferences.isHorsebackriding()) {
                            MyApplication.categoryPreferencesValue.put("Horseback Riding", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Horseback Riding");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Horseback Riding", false);
                        }

                        if (categoryPreferences.isMini_golf()) {
                            MyApplication.categoryPreferencesValue.put("Mini Golf", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Mini Golf");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Mini Golf", false);
                        }

                        if (categoryPreferences.isMovietheaters()) {
                            MyApplication.categoryPreferencesValue.put("Movie", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Movie");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Movie", false);
                        }

                        if (categoryPreferences.isRestaurants()) {
                            MyApplication.categoryPreferencesValue.put("Restaurants", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Restaurants");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Restaurants", false);
                        }

                        if (categoryPreferences.isSkydiving()) {
                            MyApplication.categoryPreferencesValue.put("Skydiving", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Skydiving");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Skydiving", false);
                        }

                        if (categoryPreferences.isSnorkeling()) {
                            MyApplication.categoryPreferencesValue.put("Snorkeling", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Snorkeling");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Snorkeling", false);
                        }

                        if (categoryPreferences.isTennis()) {
                            MyApplication.categoryPreferencesValue.put("Tennis", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Tennis");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Tennis", false);
                        }

                        if (categoryPreferences.isZipline()) {
                            MyApplication.categoryPreferencesValue.put("Zipline", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Zipline");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Zipline", false);
                        }

                        if (categoryPreferences.isZoos()) {
                            MyApplication.categoryPreferencesValue.put("Zoo", true);
                            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox)categoryLayout.findViewWithTag("Zoo");
                            appCompatCheckBox.setChecked(true);
                        } else {
                            MyApplication.categoryPreferencesValue.put("Zoo", false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/*
        for (int i=0; i <MyApplication.categories.size(); i++) {
            AppCompatCheckBox category = new AppCompatCheckBox(getContext());
            category.setText(MyApplication.categories.get(i).getDisplayName());
            category.setTextColor(Color.WHITE);
            categoryLayout.addView(category);
        }
*/
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
