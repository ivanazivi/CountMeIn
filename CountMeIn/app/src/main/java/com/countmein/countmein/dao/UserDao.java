package com.countmein.countmein.dao;

import android.util.Log;

import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.eventBus.OttoBus;
import com.countmein.countmein.eventBus.event.UsersLoadedEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Home on 5/20/2017.
 */


@EBean(scope = EBean.Scope.Singleton)
public class UserDao {

    private static final String USERS_TAG = "users";
    private static final String FRIENDS_TAG = "usersfriends";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Map<String, UserBean> users;
    private UserBean currentUserBean;

    @Bean
    OttoBus bus;

    public void init() {
        final DatabaseReference usersRef = db.getReference(USERS_TAG);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, UserBean>>() {
                });
                bus.post(new UsersLoadedEvent());
                Log.d("USERS", "users loaded");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("USERS", "users cancelled");
            }
        });
    }

    public void write(UserBean userBean) {
        db.getReference(USERS_TAG).child(userBean.getId()).setValue(userBean);
    }

    public boolean userExists(String userId) {
        return users != null && users.containsKey(userId);
    }

    public UserBean getUserById(String userId) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.get(userId);
    }

    public UserBean getUserByUsername(String username) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        for (UserBean userBean : users.values()) {
            if (userBean.getUsername().equals(username)) {
                return userBean;
            }
        }
        return null;
    }
    public List<UserBean> getAllUsers(){
        return new ArrayList<UserBean>(users.values());
    }

    public UserBean getCurrentUserBean() {
        return currentUserBean;
    }

    public void setCurrentUserBean(UserBean currentUserBean) {
        this.currentUserBean = currentUserBean;
    }



}
