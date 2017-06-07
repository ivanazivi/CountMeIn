package com.countmein.countmein.fragments.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.countmein.countmein.R;
import com.countmein.countmein.activities.HomeActivity;
import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.activities.NewGroupActivity;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.GroupBean;
import com.countmein.countmein.holders.ActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RecyclerViewFragment";

    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<GroupBean,ActivityViewHolder> adapter;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ActivityBean> activities;


    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity_.toolbar.setTitle(R.string.my_groups);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_view, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);



        adapter  = new FirebaseRecyclerAdapter<GroupBean,ActivityViewHolder>(GroupBean.class,
                R.layout.single_card_view,ActivityViewHolder.class,  FirebaseDatabase.getInstance().getReference().child("usergroup").child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, GroupBean model, int position) {
                viewHolder.vName.setText(model.getName().toString());
                viewHolder.vDescription.setText(model.getDescription().toString());
                viewHolder.cv.findViewById(R.id.button_view_attending_activity).setVisibility(View.GONE);
                viewHolder.vDate.setVisibility(View.GONE);
                viewHolder.cv.findViewById(R.id.layout_checkbox).setVisibility(View.GONE);



                LinearLayout ln = (LinearLayout) viewHolder.cv.findViewById(R.id.text_container);
                ImageButton btnEdit = (ImageButton) viewHolder.cv.findViewById(R.id.activity_edit);
                ImageButton btnDelete = (ImageButton) viewHolder.cv.findViewById(R.id.activity_delete);
                btnEdit.setTag(model);
                btnDelete.setTag(model);
                ln.setTag(model);

                ln.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                    /*    ActivityBean activity=(ActivityBean) view.getTag();
                        Intent i = new Intent(view.getContext(), SelectedActivity.class);
                        Bundle data= new Bundle();
                        data.putSerializable("data",activity);
                        i.putExtras(data);
                        view.getContext().startActivity(i);*/
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        GroupBean group=(GroupBean) view.getTag();
                        Intent i = new Intent(view.getContext(), NewGroupActivity.class);
                        Bundle data= new Bundle();
                        data.putSerializable("data",group);
                        data.putInt("isEdit", 1);
                        i.putExtras(data);
                        view.getContext().startActivity(i);
                    }

                });

                btnDelete.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        GroupBean group=(GroupBean) view.getTag();
                        FirebaseDatabase.getInstance().getReference().child("usergroup").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(group.getId()).removeValue();
                    }
                });


            }
        };

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);


        return rootView;
    }


}
