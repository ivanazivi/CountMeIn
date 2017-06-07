package com.countmein.countmein.fragments.other;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.countmein.countmein.R;
import com.countmein.countmein.activities.MainActivity;
import com.countmein.countmein.beans.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    View rootView;
    private FirebaseUser ccUser;
    private User user;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ccUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(ccUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        try {
                            ((EditText) rootView.findViewById(R.id.giveNickname)).setText(user.getUsername());
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Button btnSave = (Button) rootView.findViewById(R.id.saveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = ((EditText) rootView.findViewById(R.id.giveNickname)).getText().toString();
                CheckBox boxDel = (CheckBox) rootView.findViewById(R.id.checkbox_delete_acnt);
                user.setUsername(newName);
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(ccUser.getUid()).setValue(user);

                if(boxDel.isChecked()){
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);

                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(ccUser.getUid()).removeValue();
                }
            }
        });



        return rootView;
    }

}
