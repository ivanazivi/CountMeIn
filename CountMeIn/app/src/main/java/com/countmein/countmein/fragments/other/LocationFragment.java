package com.countmein.countmein.fragments.other;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    private MarkerOptions marker;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MapFragment fr= new MapFragment();
        Bundle mapBundle = new Bundle();
        Bundle bundle = this.getArguments();

        try {
            int isEdit = bundle.getInt("isEdit");
            ActivityBean eActivity = (ActivityBean) bundle.getSerializable("data");

            if (isEdit == 1) {
                mapBundle.putDouble("markLat", Double.valueOf(eActivity.getlLat()));
                mapBundle.putDouble("markLng", Double.valueOf(eActivity.getlLng()));
                mapBundle.putInt("isEdit", 1);
                fr.setArguments(mapBundle);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_location_map,  fr);
        fragmentTransaction.commit();

        return inflater.inflate(R.layout.fragment_location, container, false);
    }

}
