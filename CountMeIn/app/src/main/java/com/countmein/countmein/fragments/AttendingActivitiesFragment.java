package com.countmein.countmein.fragments;

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
import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.activities.SelectedActivity;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.beans.User_ActivityBean;
import com.countmein.countmein.beans.WhoAttendsBean;
import com.countmein.countmein.beans.MockUpActivity;
import com.countmein.countmein.holders.ActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class AttendingActivitiesFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    public static final String ATTENDINGACTIVITIES = "attendingactivities";
    public static final String USERACTIVITIES = "useractivities";

    protected RecyclerView mRecyclerView;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ActivityBean> activities;
    private FirebaseRecyclerAdapter<User_ActivityBean,ActivityViewHolder > adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        HomeActivity_.toolbar.setTitle(R.string.attending_activities);

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
        adapter  = new FirebaseRecyclerAdapter<User_ActivityBean,ActivityViewHolder >(User_ActivityBean.class,
                R.layout.single_card_view,ActivityViewHolder.class, FirebaseDatabase.getInstance().getReference().child(ATTENDINGACTIVITIES)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            ImageButton btnQuit;
            LinearLayout ln;

            @Override
            protected void populateViewHolder(final ActivityViewHolder viewHolder, final User_ActivityBean model, int position) {

                FirebaseDatabase.getInstance().getReference().child(USERACTIVITIES)
                        .child(model.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snap: dataSnapshot.getChildren()) {
                            ActivityBean act = snap.getValue(ActivityBean.class);
                            viewHolder.vName.setText(act.getName());
                            viewHolder.vDescription.setText(act.getDescription());
                            viewHolder.vDate.setText(act.getDate());

                            viewHolder.cv.findViewById(R.id.button_view_myActivity).setVisibility(View.GONE);
                            viewHolder.cv.findViewById(R.id.layout_checkbox).setVisibility(View.GONE);
                            viewHolder.cv.findViewById(R.id.activity_attending).setVisibility(View.GONE);

                            btnQuit = (ImageButton) viewHolder.cv.findViewById(R.id.activity_quit);
                            ln = (LinearLayout) viewHolder.cv.findViewById(R.id.text_container);
                            btnQuit.setTag(act);
                            ln.setTag(act);

                            ln.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View view){
                                    ActivityBean activity=(ActivityBean) view.getTag();
                                    Intent i = new Intent(view.getContext(), SelectedActivity.class);
                                    Bundle data= new Bundle();
                                    data.putSerializable("data",activity);
                                    i.putExtras(data);
                                    view.getContext().startActivity(i);
                                }
                            });

                            btnQuit.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {
                    /*    FirebaseDatabase.getInstance().getReference().child(ATTENDINGACTIVITIES)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(model.getId()).removeValue();
*/
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("whoisattending").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                WhoAttendsBean appid = snapshot.getValue(WhoAttendsBean.class);
                                                if (appid.activityId.equals(model.getActivityId())) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("whoisattending")
                                                            .child(snapshot.getKey()).removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);

        return rootView;
    }
}
