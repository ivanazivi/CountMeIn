package com.countmein.countmein.dao;

import android.os.Bundle;
import android.util.Log;

import com.countmein.countmein.beans.User;
import com.countmein.countmein.eventBus.OttoBus;
import com.countmein.countmein.eventBus.event.UsersLoadedEvent;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private Map<String, User> users;

    private User currentUser;

    @Bean
    OttoBus bus;

    public void init() {
        final DatabaseReference usersRef = db.getReference(USERS_TAG);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, User>>() {
                });
                bus.post(new UsersLoadedEvent());
                Log.d("users loaded", "users loaded");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("users cancelled", "users cancelled");
            }
        });
    }

    public void write(User user) {
        db.getReference(USERS_TAG).child(user.getId()).setValue(user);
    }



    public boolean userExists(String userId) {
        return users != null && users.containsKey(userId);
    }

    public User getUserById(String userId) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.get(userId);
    }

    public User getUserByUsername(String username) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }
    public List<User> getAllUsers(){


        return new ArrayList<User>(users.values());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }



}
