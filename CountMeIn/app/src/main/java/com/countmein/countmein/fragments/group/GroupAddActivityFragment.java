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
import android.widget.EditText;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.GroupBean;
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.holders.ActivityViewHolder;
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
 * Created by Home on 6/5/2017.
 */

public class GroupAddActivityFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    public static final String  USERGROUP = "usergroup";
    public static final String GROUPINACTIVITY = "groupinactivity";
    public ActivityBean eActivity;
    public int isEdit;
    public View rootView;
    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<GroupBean,ActivityViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    public List<String> selectedgroups;
    List<GroupBean> groupList;

    public GroupAddActivityFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedgroups=new ArrayList<>();
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
        groupList = new ArrayList<>();

        try {
            eActivity = (ActivityBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

        }catch (Exception e){
            e.printStackTrace();
        }


        adapter  = new FirebaseRecyclerAdapter<GroupBean,ActivityViewHolder>(GroupBean.class,
                 R.layout.single_card_view, ActivityViewHolder.class, FirebaseDatabase.getInstance().getReference().child(USERGROUP)
                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            @Override
            protected void populateViewHolder(final ActivityViewHolder viewHolder, final GroupBean model, int position) {

                viewHolder.vName.setText(model.getName().toString());
                viewHolder.vDescription.setText(model.getDescription().toString());
                viewHolder.cv.findViewById(R.id.button_view_attending_activity).setVisibility(View.GONE);
                viewHolder.cv.findViewById(R.id.button_view_myActivity).setVisibility(View.GONE);
                viewHolder.vDate.setVisibility(View.GONE);
                viewHolder.checkBox.setTag(model);

                try {
                    FirebaseDatabase.getInstance().getReference().child(GROUPINACTIVITY)
                            .child(eActivity.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snap : dataSnapshot.getChildren()){
                                IdBean groupID = snap.getValue(IdBean.class);

                                if (isEdit == 1) {
                                    if (groupID.getId().equals(model.getId())) {
                                        viewHolder.checkBox.setChecked(true);
                                        selectedgroups.add(model.getId());
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

                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupBean group =(GroupBean) v.getTag();

                        boolean checked = ((CheckBox) v).isChecked();
                        if(checked){
                            selectedgroups.add(group.getId());

                        }else {
                            if(selectedgroups.size()==1){
                                Toast.makeText(getActivity(), "You need to have at least one group selected!", Toast.LENGTH_LONG).show();
                                viewHolder.checkBox.setChecked(true);
                                Log.d("size",Integer.toString(selectedgroups.size()));
                            }else
                            for(int i=0;i<selectedgroups.size();i++){
                                if(group.getId()==selectedgroups.get(i)){
                                    selectedgroups.remove(i);
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

    public List<String> getSelectedgroups() {
        return selectedgroups;
    }

    public void setSelectedgroups(List<String> selectedgroups) {
        this.selectedgroups = selectedgroups;
    }

}
