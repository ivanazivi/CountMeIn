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
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.fragments.group.GroupFriendFragment;
import com.countmein.countmein.fragments.group.NewGroupDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGroupActivity extends AppCompatActivity {

    public static final String  USERGROUP = "usergroup";
    public static final String  FRIENDSINGROUP = "friendsingroup";
    NewGroupDetailsFragment newGroupDetailsFragment;
    GroupFriendFragment peopleFragment;
    private FirebaseDatabase mDatabase;
    private FirebaseUser ccUser;
    public GroupBean eGroup;
    public int isEdit;
    public List<UserBean> selectedusers;
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
                selectedusers=new ArrayList<UserBean>();
                GroupBean group;
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

                            group = new GroupBean(gName,gDesc);
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

                                selectedusers=new ArrayList<UserBean>();
                                gDesc=newGroupDetailsFragment.gDesc.getText().toString();
                                gName=newGroupDetailsFragment.gName.getText().toString();
                                selectedusers=peopleFragment.getSelectedusers();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            group = new GroupBean(eGroup.getId(),gName,gDesc,eGroup.getId());
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
        Map<String,String> friendsInGroup = new HashMap<>();

        String key=mDatabase.getInstance().getReference().child(USERGROUP)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().getKey();
        group.setId(key);
        group.setTopictoken(key);

        mDatabase.getInstance().getReference().child(USERGROUP)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(key).setValue(group);

        for(UserBean friend: selectedusers) {
            friendsInGroup.put("id", friend.getId());
            mDatabase.getInstance().getReference().child(FRIENDSINGROUP)
                    .child(key).push()
                    .setValue(friendsInGroup);
        }
    }
    private void editGroup(GroupBean group) {//cuvam grupu, ali moram i FRIENDSINGROUP UPDATE!!
        Map<String,String> friendsInGroup = new HashMap<>();

        mDatabase.getInstance().getReference().child(USERGROUP)
                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                 .child(group.getId()).setValue(group);

        FirebaseDatabase.getInstance().getReference().child(FRIENDSINGROUP)
                .child(group.getId()).removeValue();

        for(UserBean friend: selectedusers) {
            friendsInGroup.put("id", friend.getId());
            mDatabase.getInstance().getReference().child(FRIENDSINGROUP)
                    .child(group.getId()).push()
                    .setValue(friendsInGroup);
        }
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
