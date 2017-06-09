package com.countmein.countmein.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.countmein.countmein.R;
import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.activities.NewActivityActivity;
import com.countmein.countmein.activities.SelectedActivity;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.IdBean;
import com.countmein.countmein.beans.MockUpActivity;
import com.countmein.countmein.beans.UserBean;
import com.countmein.countmein.holders.ActivityViewHolder;
import com.countmein.countmein.listeners.RecyclerItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Home on 6/6/2017.
 */

public class InvitedFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    public static final String INVITEDACTIVITES = "invitedactivities";
    public static final String USERACTIVITIES = "useractivities";
    public static final String ATTENDINGACTIVITIES = "attendingactivities";
    public static final String WHOISATTENDING = "whoisattending";
    protected RecyclerView mRecyclerView;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ActivityBean> activities;
    private FirebaseRecyclerAdapter<IdBean,ActivityViewHolder > adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        HomeActivity_.toolbar.setTitle(R.string.inviting_activities);

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
        adapter  = new FirebaseRecyclerAdapter<IdBean,ActivityViewHolder >(IdBean.class,
                R.layout.single_card_view,ActivityViewHolder.class, FirebaseDatabase.getInstance().getReference().child(INVITEDACTIVITES)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            ImageButton btnAttending;
            LinearLayout ln;

            @Override
            protected void populateViewHolder(final ActivityViewHolder viewHolder, final IdBean model, int position) {

                FirebaseDatabase.getInstance().getReference().child(USERACTIVITIES)
                        .child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snap: dataSnapshot.getChildren()) {
                            ActivityBean act = snap.getValue(ActivityBean.class);
                            viewHolder.vName.setText(act.getName());
                            viewHolder.vDescription.setText(act.getDescription());
                            viewHolder.vDate.setText(act.getDate());

                            viewHolder.cv.findViewById(R.id.button_view_myActivity).setVisibility(View.GONE);
                            viewHolder.cv.findViewById(R.id.layout_checkbox).setVisibility(View.GONE);
                            viewHolder.cv.findViewById(R.id.activity_quit).setVisibility(View.GONE);

                            btnAttending = (ImageButton) viewHolder.cv.findViewById(R.id.activity_attending);
                            ln = (LinearLayout) viewHolder.cv.findViewById(R.id.text_container);
                            btnAttending.setTag(act);
                            ln.setTag(act);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

                btnAttending.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ActivityBean activity=(ActivityBean) view.getTag();
                        Map<String, String> userWhoAttends = new HashMap<String, String>();
                        userWhoAttends.put("activityId", model.getId());
                        userWhoAttends.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseDatabase.getInstance().getReference().child(ATTENDINGACTIVITIES)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(model.getId()).setValue(model);

                        FirebaseDatabase.getInstance().getReference().child(INVITEDACTIVITES)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(model.getId()).removeValue();

                        FirebaseDatabase.getInstance().getReference().child(WHOISATTENDING)
                                .push().setValue(userWhoAttends);

                        Toast.makeText(getApplicationContext(), "You are attending "+activity.getName(), Toast.LENGTH_SHORT).show();

                    }

                });
            }
        };

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }
}
