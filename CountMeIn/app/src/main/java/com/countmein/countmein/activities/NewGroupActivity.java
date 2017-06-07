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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.GroupBean;
import com.countmein.countmein.beans.User;
import com.countmein.countmein.fragments.group.GroupFriendFragment;
import com.countmein.countmein.fragments.group.NewGroupDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewGroupActivity extends AppCompatActivity {

    NewGroupDetailsFragment newGroupDetailsFragment;
    GroupFriendFragment peopleFragment;


    private FirebaseDatabase mDatabase;
    private FirebaseUser ccUser;
    public GroupBean eGroup;
    public int isEdit;
    private NewGroupActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Bundle bundle = getIntent().getExtras();
        newGroupDetailsFragment=new NewGroupDetailsFragment();
        peopleFragment=new GroupFriendFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.new_group);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSectionsPagerAdapter = new NewGroupActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        try{
            eGroup = (GroupBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

            if(isEdit == 1){
                toolbar.setTitle(R.string.edit_group);
                newGroupDetailsFragment.setArguments(bundle);
                peopleFragment.setArguments(bundle);

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

                String gName = null;
                String gDesc = null;
                List<User> selectedusers=new ArrayList<User>();
                GroupBean group;

                GroupBean newGroup;

                if(isEdit != 1) {
                    switch (item.getItemId()) {
                        case R.id.miSave:

                            try {


                                gDesc=newGroupDetailsFragment.gDesc.getText().toString();
                                gName=newGroupDetailsFragment.gName.getText().toString();
                                selectedusers=peopleFragment.getSelectedusers();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            group = new GroupBean(gName,gDesc,selectedusers);
                            addNewGroup(group);

                            Toast.makeText(getApplicationContext(), "Group was made successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                } else
                {
                    switch (item.getItemId()) {
                        case R.id.miSave:

                            try {

                                selectedusers=new ArrayList<User>();
                                gDesc=newGroupDetailsFragment.gDesc.getText().toString();
                                gName=newGroupDetailsFragment.gName.getText().toString();
                                selectedusers=peopleFragment.getSelectedusers();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            group = new GroupBean(eGroup.getId(),gName,gDesc,selectedusers,eGroup.getId());
                            editGroup(group);


                            Toast.makeText(getApplicationContext(), "Group was made successfully", Toast.LENGTH_SHORT).show();
                            finish();

                    }
                }
                return false;
            }
        });


    }

    private void addNewGroup(GroupBean group) {

        String key=mDatabase.getInstance().getReference().child("usergroup").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().getKey();
        group.setId(key);
        group.setTopictoken(key);
        mDatabase.getInstance().getReference().child("usergroup").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(group);

    }
    private void editGroup(GroupBean group) {
         mDatabase.getInstance().getReference().child("usergroup").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(group.getId()).setValue(group);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
    }



    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newGroupDetailsFragment;
                case 1:
                    return peopleFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "FRIENDS";

            }
            return null;
        }
    }
}
