package com.countmein.countmein.fragments.group;

import android.nfc.Tag;
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
import com.countmein.countmein.beans.User;
import com.countmein.countmein.holders.PeopleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 6/5/2017.
 */

public class GroupFriendFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";


    public GroupBean eGroup;
    public int isEdit;

    public  View rootView;

    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<User,PeopleViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public List<User> getSelectedusers() {
        return selectedusers;
    }

    public void setSelectedusers(List<User> selectedusers) {
        this.selectedusers = selectedusers;
    }

    List<User> selectedusers;

    public  GroupFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedusers=new ArrayList<>();
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
        try {
            eGroup = (GroupBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

        }catch (Exception e){
            e.printStackTrace();
        }

        adapter  = new FirebaseRecyclerAdapter<User,PeopleViewHolder>(User.class,
                R.layout.people_card_view,PeopleViewHolder.class, FirebaseDatabase.getInstance().getReference().child("userfriends").child(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            @Override
            protected void populateViewHolder(final PeopleViewHolder viewHolder, User model, int position) {
                viewHolder.messageUser.setText(model.getUsername());
                viewHolder.userPhoto.setImageURI(model.getPhotoUrl());
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.checkBox.setTag(model);
                if (isEdit == 1) {
                    for(int i=0;i<eGroup.getFriends().size();i++){
                        String friend_id = eGroup.getFriends().get(i).getId();
                        String model_id = model.getId();
                        Log.d("friend_did",friend_id);
                        Log.d("model_id",model_id);
                        if(friend_id.equals(model_id)){
                            viewHolder.checkBox.setChecked(true);
                            selectedusers.add(model);

                        }

                    }

                }
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user=(User) v.getTag();

                        boolean checked = ((CheckBox) v).isChecked();
                        if(checked){
                            Log.d("clickListener", "checked");

                            selectedusers.add(user);
                        }else {
                            if(selectedusers.size()==1){
                                Toast.makeText(getActivity(), "You need to have at least one friend selected!", Toast.LENGTH_LONG).show();
                                viewHolder.checkBox.setChecked(true);
                                Log.d("size",Integer.toString(selectedusers.size()));
                            }else{

                                for(int i=0;i<selectedusers.size();i++){
                                    if(user.getId().equals(selectedusers.get(i).getId())){
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

}
