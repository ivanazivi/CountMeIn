package com.countmein.countmein.activities;


import android.content.Intent;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.GroupBean;
import com.countmein.countmein.fragments.group.GroupAddActivityFragment;
import com.countmein.countmein.fragments.other.DatePickerFragment;
import com.countmein.countmein.fragments.other.LocationFragment;
import com.countmein.countmein.fragments.other.MapFragment;
import com.countmein.countmein.fragments.NewActivityDetailsFragment;
import com.countmein.countmein.services.FcmNotificationBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_new_activity)
public class NewActivityActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser ccUser;
    public  ActivityBean eActivity;
    public int isEdit;
    private NewActivityActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private NewActivityDetailsFragment newActivityDetailsFragment;
    private LocationFragment mapFragment;
    private GroupAddActivityFragment groupFragment;
    private DatePickerFragment datePickerFragment;

    @AfterViews
    void init(){

        Bundle bundle = getIntent().getExtras();
        newActivityDetailsFragment = new NewActivityDetailsFragment();
        mapFragment = new LocationFragment();
        groupFragment=new GroupAddActivityFragment();
        datePickerFragment = new DatePickerFragment();
        ccUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("useractivities").child(ccUser.getUid());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.new_activity);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        try{
            eActivity = (ActivityBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

            if(isEdit == 1){
                toolbar.setTitle(R.string.edit_activity);
                newActivityDetailsFragment.setArguments(bundle);
                mapFragment.setArguments(bundle);

                datePickerFragment.setArguments(bundle);
                groupFragment.setArguments(bundle);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String aName = null;
                String aDesc = null;
                DatePicker aDate = null;
                double lLat = 0;
                double lLng = 0;
                List<GroupBean> list= new ArrayList<GroupBean>();
                ActivityBean newAct;

                if(isEdit != 1) {
                    switch (item.getItemId()) {
                        case R.id.miSave:

                            try {
                                list=new ArrayList<GroupBean>();
                                aName = newActivityDetailsFragment.aName.getText().toString();
                                aDesc = newActivityDetailsFragment.aDesc.getText().toString();

                                aDate = datePickerFragment.aDate;
                                lLng = MapFragment.mMarker.getPosition().longitude;
                                lLat = MapFragment.mMarker.getPosition().latitude;
                                list=groupFragment.getSelectedgroups();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            newAct = new ActivityBean(aName, aDesc, convertData(aDate), String.valueOf(lLat), String.valueOf(lLng),list);
                            addNewActivityAsaChild(newAct);

                            Toast.makeText(getApplicationContext(), "Activity was made successfully", Toast.LENGTH_SHORT).show();

                            Intent jj = new Intent(NewActivityActivity.this, HomeActivity_.class);
                            startActivity(jj);

                            int size = newAct.getGroup().size();
                            ArrayList<String> tokens = new ArrayList<String>();
                            for(int i=0; i<size; i++){
                                for(int j=0; j<newAct.getGroup().get(i).getFriends().size(); j++){
                                    String token  = newAct.getGroup().get(i).getFriends().get(j).getTokens();

                                    System.out.print(FirebaseDatabase.getInstance().getReference().child("topics").child(newAct.getId()).equalTo(token));
                                    //  if(FirebaseDatabase.getInstance().getReference().child("topics").child(newAct.getId()).equalTo(token) == null){
                                    Log.d("If_exists","Doesn't exist");
                                    FirebaseDatabase.getInstance().getReference().child("topics").child(newAct.getId()).push().setValue(token);
                                    //  }
                                    tokens.add(token);

                                }
                            }

                            sendPushNotification(tokens);
                            finish();
                            break;
                    }
                } else
                {
                    switch (item.getItemId()) {
                        case R.id.miSave:

                            try {
                                list=new ArrayList<GroupBean>();

                                aName = newActivityDetailsFragment.aName.getText().toString();
                                aDesc = newActivityDetailsFragment.aDesc.getText().toString();

                                aDate = datePickerFragment.aDate;
                                lLng = MapFragment.mMarker.getPosition().longitude;
                                lLat = MapFragment.mMarker.getPosition().latitude;
                                list=groupFragment.getSelectedgroups();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            newAct = new ActivityBean(eActivity.getId(), aName, aDesc, convertData(aDate), String.valueOf(lLat), String.valueOf(lLng),list);
                            updateActivity(newAct);


                            Toast.makeText(getApplicationContext(), "Activity was edited successfuly", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(NewActivityActivity.this, HomeActivity_.class);
                            startActivity(i);
                            finish();
                            break;
                    }
                }
                return false;
            }
        });
    }



    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public String convertData(DatePicker aDate){
        int day = aDate.getDayOfMonth();
        int mth = aDate.getMonth() + 1;
        int year = aDate.getYear();

        return new String(day+"-"+mth+"-"+year);
    }

    @Background
    public void addNewActivityAsaChild(ActivityBean newAct){

        String key = mDatabase.push().getKey();
        newAct.setId(key);

        mDatabase.child(key).setValue(newAct);
        if(!newAct.getGroup().isEmpty()){
            for(int i=0;i<newAct.getGroup().size();i++){
                if(!newAct.getGroup().get(i).getFriends().isEmpty()){
                    for(int j=0;j<newAct.getGroup().get(i).getFriends().size();j++){
                        FirebaseDatabase.getInstance().getReference().child("invitedactivities")
                                .child(newAct.getGroup().get(i).getFriends().get(j).getId())
                                .child(newAct.getId()).setValue(newAct.convertMockUp());
                    }
                }
            }
        }



        int size = newAct.getGroup().size();

        for(int i=0; i<size; i++){
            Log.d("size", Integer.toString(newAct.getGroup().get(i).getFriends().size()));
            for(int j=0; j<newAct.getGroup().get(i).getFriends().size(); j++){
                String token  = newAct.getGroup().get(i).getFriends().get(j).getTokens();
                Log.d("token",
                        FirebaseDatabase.getInstance().getReference().child("topics").child(newAct.getId()).child(token).toString());
                //    if(FirebaseDatabase.getInstance().getReference().child("topics").child(newAct.getId()).child(token) == null){
                Log.d("If_exists","Doesn't exist");
                FirebaseDatabase.getInstance().getReference().child("topics")
                        .child(newAct.getId()).push().setValue(token);
                //   }

            }
        }

    }

    public void updateActivity(ActivityBean activity){
        FirebaseDatabase.getInstance().getReference()
                .child("useractivities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(activity.getId())
                .setValue(activity);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newActivityDetailsFragment;
                case 1:
                    return datePickerFragment;
                case 2:
                    return mapFragment;
                case 3:
                    return groupFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "DATE";
                case 2:
                    return "LOCATION";
                case 3:
                    return "INVITES";
            }
            return null;
        }
    }

    @Background
    public void sendPushNotification(ArrayList<String> tokens){
        FcmNotificationBuilder notBuilder = FcmNotificationBuilder.initialize();
        notBuilder.message("You have been added to new activity.");
        notBuilder.receiversFirebaseTokens(tokens);
        notBuilder.title("CountMeIn App Notification");
        notBuilder.send();
    }
}
