package com.countmein.countmein.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.BaseModel;
import com.countmein.countmein.beans.JustId;
import com.countmein.countmein.beans.MockUpActivity;
import com.countmein.countmein.beans.User;
import com.countmein.countmein.holders.ActivityViewHolder;
import com.countmein.countmein.holders.PeopleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendingMyActFragment extends DialogFragment {
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ActivityBean> activities;
    private FirebaseRecyclerAdapter<JustId,PeopleViewHolder > adapter;
    private FirebaseUser ccUser;
    public String whoisattendingD;

    public AttendingMyActFragment() {
        // Required empty public constructor
    }

    public static AttendingMyActFragment newIstance(){
        return new AttendingMyActFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attending_my_act, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        ccUser = FirebaseAuth.getInstance().getCurrentUser();
        final String selectedActivityId = getArguments().getString("id");
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter  = new FirebaseRecyclerAdapter<JustId,PeopleViewHolder >(JustId.class,
                R.layout.people_card_view,PeopleViewHolder.class, FirebaseDatabase.getInstance().getReference().child("whoisattending")) {

            @Override
            protected void populateViewHolder(final PeopleViewHolder viewHolder, final JustId model, int position) {


                if(selectedActivityId.equals(model.activityId)) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(model.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            viewHolder.messageUser.setText(dataSnapshot.getValue(User.class).getUsername());
                            viewHolder.userPhoto.setImageURI(dataSnapshot.getValue(User.class).getPhotoUrl());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.checkBox.setVisibility(View.GONE);
            }
        };

        mRecyclerView.setAdapter(adapter);


        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        return dialog;
    }

}
