package com.countmein.countmein.fragments.group;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.GroupBean;

/**
 * Created by Home on 6/5/2017.
 */

public class NewGroupDetailsFragment extends android.support.v4.app.Fragment {

    public GroupBean eGroup;
    public int isEdit;
    public EditText gName;
    public  EditText gDesc;
    public  View rootView;

    public NewGroupDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_new_group_details, container, false);
        gName=(EditText) rootView.findViewById(R.id.groupName);
        gDesc=(EditText) rootView.findViewById(R.id.groupDesc);
        Bundle bundle = this.getArguments();

        try {
            eGroup = (GroupBean) bundle.getSerializable("data");
            isEdit = bundle.getInt("isEdit");

            if (isEdit == 1) {
                ((EditText) rootView.findViewById(R.id.groupName)).setText(eGroup.getName());
                ((EditText) rootView.findViewById(R.id.groupDesc)).setText(eGroup.getDescription());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }



}
