package com.countmein.countmein.fragments.people;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.holders.PeopleViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Home on 5/21/2017.
 */

public class AllPeopleFragment  extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String USERFRIENDS = "userfriends";
    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<UserBean,PeopleViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    Map<String,String> newFriend;

    public  AllPeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newFriend = new HashMap<>();
        HomeActivity_.toolbar.setTitle("Search people");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recycle_view, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter  = new FirebaseRecyclerAdapter<UserBean,PeopleViewHolder>(UserBean.class,
                    R.layout.people_card_view, PeopleViewHolder.class,
                    FirebaseDatabase.getInstance().getReference().child("users")) {

            @Override
            protected void populateViewHolder(final PeopleViewHolder viewHolder, final UserBean model, int position) {
                if(FirebaseAuth.getInstance().getCurrentUser().getUid()==model.getId()){
                    viewHolder.cv.setVisibility(View.GONE);
                    viewHolder.cv.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                } else {
                    viewHolder.messageUser.setText(model.getUsername());
                    viewHolder.userPhoto.setImageURI(model.getPhotoUrl());
                    viewHolder.button.setTag(model);
                    viewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setVisibility(View.GONE);
                            newFriend.put("id", model.getId());
                            Toast.makeText(rootView.getContext(), "You have added succesfully added a friend", Toast.LENGTH_LONG).show();
                            FirebaseDatabase.getInstance().getReference().child(USERFRIENDS)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                    .setValue(newFriend);
                        }
                    });
                    viewHolder.checkBox.setVisibility(View.GONE);}
            }
        };

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

}
