package com.countmein.countmein.fragments.people;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.countmein.countmein.R;

import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.beans.BaseModel;
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.holders.PeopleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FriendFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String USERFRIENDS = "userfriends";
    private static final String USERS = "users";

    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<IdBean,PeopleViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity_.toolbar.setTitle(R.string.my_friends);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_view, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter  = new FirebaseRecyclerAdapter<IdBean,PeopleViewHolder>(IdBean.class,
                R.layout.people_card_view,PeopleViewHolder.class, FirebaseDatabase.getInstance().getReference().child(USERFRIENDS).child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            @Override
            protected void populateViewHolder(final PeopleViewHolder viewHolder, IdBean model, int position) {

                FirebaseDatabase.getInstance().getReference().child(USERS)
                        .child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserBean friend = dataSnapshot.getValue(UserBean.class);
                        viewHolder.messageUser.setText(friend.getUsername());
                        viewHolder.userPhoto.setImageURI(friend.getPhotoUrl());
                        viewHolder.button.setVisibility(View.GONE);
                        viewHolder.checkBox.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }
}
