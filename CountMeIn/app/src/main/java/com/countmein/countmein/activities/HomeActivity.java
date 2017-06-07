package com.countmein.countmein.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.User;
import com.countmein.countmein.dao.UserDao;
import com.countmein.countmein.eventBus.event.UsersLoadedEvent;
import com.countmein.countmein.fragments.AboutFragment;
import com.countmein.countmein.fragments.InvitedFragment;
import com.countmein.countmein.fragments.other.SettingsFragment;
import com.countmein.countmein.fragments.people.AllPeopleFragment;
import com.countmein.countmein.fragments.AttendingActivitiesFragment;
import com.countmein.countmein.fragments.people.FriendFragment;
import com.countmein.countmein.fragments.group.GroupFragment;
import com.countmein.countmein.fragments.ActivitiesFragment;
import com.facebook.CallbackManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_nav_drawer)
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    public static Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static double hLat;
    public static double hLog;

    SimpleDraweeView simpleDraweeView;
    CallbackManager callbackManager;

    @Bean
    UserDao userDao;


    @AfterViews
    public void create() {
        userDao.init();
        final FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        if (userDao.userExists(userFirebase.getUid())) {
            userDao.setCurrentUser(userDao.getUserById(userFirebase.getUid()));
        } else {
            final User user = new User(userFirebase.getUid(), userFirebase.getDisplayName(), userFirebase.getPhotoUrl().toString(), FirebaseInstanceId.getInstance().getToken());
            userDao.write(user);
            userDao.setCurrentUser(user);
        }

        ActivitiesFragment fragment = new ActivitiesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.home);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewActivityActivity_.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nameView);
        nav_user.setText(userFirebase.getDisplayName());

        TextView nav_email = (TextView)hView.findViewById(R.id.textView);
        nav_email.setText(userFirebase.getEmail());

        SimpleDraweeView nav_image = (SimpleDraweeView)hView.findViewById(R.id.navImageView);
      //  nav_image.setImageURI(null);
        nav_image.setImageURI(userFirebase.getPhotoUrl());

        // simpleDraweeView.setImageURI(userFirebase.getPhotoUrl().toString());

        //Getting location for camera position
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Subscribe
    public void userLoaded(UsersLoadedEvent event) {
        final FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        if (userDao.userExists(userFirebase.getUid())) {
            userDao.setCurrentUser(userDao.getUserById(userFirebase.getUid()));
        } else {
            final User user = new User(userFirebase.getUid(), userFirebase.getDisplayName(), userFirebase.getPhotoUrl().toString());
            userDao.write(user);
            userDao.setCurrentUser(user);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home) {
            InvitedFragment fragment =new InvitedFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("mainFragment");
            fragmentTransaction.commit();




        } else if (id == R.id.nav_my_activities) {

            ActivitiesFragment fragment = new ActivitiesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("mainFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, NewActivityActivity_.class);
                    startActivity(i);
                }
            });

        } else if (id == R.id.nav_attending) {

            AttendingActivitiesFragment fragment = new AttendingActivitiesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("attendingActivitiesFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.hide();

        } else if (id == R.id.nav_my_groups) {
            GroupFragment fragment = new GroupFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("groupFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, NewGroupActivity.class);
                    startActivity(i);
                }
            });

        } else if (id == R.id.nav_my_friends) {
            FriendFragment fragment = new FriendFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("friendFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.hide();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_all_people) {
            AllPeopleFragment fragment = new AllPeopleFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("allPeopleFragment");
            fragmentTransaction.commit();

            //set specific floating action
            // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            // fab.hide();

        } else if(id == R.id.nav_about){
            AboutFragment fragment = new AboutFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("aboutFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.hide();

        } else if(id == R.id.nav_setting){
            SettingsFragment fragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("settingsFragment");
            fragmentTransaction.commit();

            //set specific floating action
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.hide();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation !=null){
            hLat = mLastLocation.getLatitude();
            hLog = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
