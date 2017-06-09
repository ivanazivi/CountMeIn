package com.countmein.countmein.fragments.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.GroupBean;
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.holders.PeopleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 6/5/2017.
 */

public class GroupFriendFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String USERFRIENDS = "userfriends";
    public static final String  FRIENDSINGROUP = "friendsingroup";
    private static final String USERS = "users";

    public GroupBean eGroup;
    public int isEdit;
    public  View rootView;
    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<IdBean,PeopleViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    List<UserBean> selectedusers;
    List<UserBean> friends;
    List<CheckBox> checkBoxes;

    public  GroupFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedusers = new ArrayList<>();
        //HomeActivity.toolbar.setTitle("Search people");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_view, container, false);
        rootView.setTag(TAG);
        Bundle bundle = this.getArguments();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        isEdit=0;
        friends = new ArrayList<UserBean>();
        checkBoxes = new ArrayList<>();

        try {
            eGroup = (GroupBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

        }catch (Exception e){
            e.printStackTrace();
        }

        adapter  = new FirebaseRecyclerAdapter<IdBean,PeopleViewHolder>(IdBean.class,
                R.layout.people_card_view,PeopleViewHolder.class, FirebaseDatabase.getInstance().getReference().child(USERFRIENDS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            @Override
            protected void populateViewHolder(final PeopleViewHolder viewHolder, IdBean model, int position) {
                FirebaseDatabase.getInstance().getReference().child(USERS)
                        .child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final UserBean friend = dataSnapshot.getValue(UserBean.class);
                        friends.add(friend);
                        viewHolder.messageUser.setText(friend.getUsername());
                        viewHolder.userPhoto.setImageURI(friend.getPhotoUrl());
                        viewHolder.button.setVisibility(View.GONE);
                        CheckBox bx = viewHolder.checkBox;
                        bx.setTag(friend);
                        checkBoxes.add(bx);


                        try {
                            FirebaseDatabase.getInstance().getReference().child(FRIENDSINGROUP)
                                    .child(eGroup.getId()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    IdBean friendInGroup = new IdBean();
                                    String friend_id;
                                    String model_id;

                                    for(DataSnapshot snap: dataSnapshot.getChildren()) {
                                        friendInGroup = snap.getValue(IdBean.class);

                                        if (isEdit == 1) {
                                            for (int k = 0; k < friends.size(); k++) {
                                                try {
                                                    friend_id = friendInGroup.getId();
                                                    model_id = friends.get(k).getId();
                                                    if (friend_id.equals(model_id) && !selectedusers.contains(friends.get(k))) {
                                                        checkBoxes.get(k).setChecked(true);
                                                        selectedusers.add(friends.get(k));
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserBean userBean =(UserBean) v.getTag();

                        boolean checked = ((CheckBox) v).isChecked();
                        if(checked){
                            Log.d("clickListener", "checked");

                            selectedusers.add(userBean);
                        }else {
                            if(selectedusers.size()==1){
                                Toast.makeText(getActivity(), "You need to have at least one friend selected!", Toast.LENGTH_LONG).show();
                                viewHolder.checkBox.setChecked(true);
                                Log.d("size",Integer.toString(selectedusers.size()));
                            }else{

                                for(int i=0;i<selectedusers.size();i++){
                                    if(userBean.getId().equals(selectedusers.get(i).getId())){
                                        Log.d("clickListener","removed");
                                        selectedusers.remove(i);
                                    }
                                }
                            }

                        }

                    }
                });
            }
        };

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    public List<UserBean> getSelectedusers() {
        return selectedusers;
    }

    public void setSelectedusers(List<UserBean> selectedusers) {
        this.selectedusers = selectedusers;
    }

}
