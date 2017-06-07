package com.countmein.countmein.fragments.other;



import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


import com.countmein.countmein.R;
import com.countmein.countmein.activities.HomeActivity;
import com.countmein.countmein.activities.HomeActivity_;
import com.countmein.countmein.activities.NewActivityActivity;
import com.countmein.countmein.beans.ChatMessageBean;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.androidannotations.annotations.EBean;

import static com.countmein.countmein.R.id.map;


/**
 * A simple {@link Fragment} subclass.
 */

@EBean
public class MapFragment extends Fragment implements OnMapReadyCallback {


    public static GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    public static MarkerOptions marker;
    public static Marker mMarker;
    public SupportMapFragment  mMapFragment;
    private LatLng setActivityMarkLocation;
    private int isSelectedActivity;
    private int isEdit;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mMapFragment.getMapAsync(this);
        marker=null;
        Bundle mapBundle = this.getArguments();

        try {
            setActivityMarkLocation = new LatLng(Double.valueOf(mapBundle.getDouble("markLat")), Double.valueOf(mapBundle.getDouble("markLng")));
            isSelectedActivity = mapBundle.getInt("isSelectedAcitvity");
            isEdit = mapBundle.getInt("isEdit");
        }catch (Exception e){
            isSelectedActivity = 0;
        }

        /*    ((WorkaroundMapFragment) mMapFragment).setListener(new WorkaroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {

                }
            });*/

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);

                    return;
                }

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            float zoomLevel = 16;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HomeActivity_.hLat,HomeActivity_.hLog),zoomLevel));
   /*         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HomeActivity.hLat,HomeActivity.hLog),zoomLevel));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(HomeActivity.hLat, HomeActivity.hLog))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); */
        } else {
            // Show rationale and request permission.
        }
        if(isSelectedActivity == 1 || isEdit == 1) {
            marker = new MarkerOptions();
            mMarker = mMap.addMarker(marker.position(setActivityMarkLocation).draggable(true));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                       @Override
                                       public void onMapClick(LatLng latLng) {

                                           FirebaseMessaging.getInstance().subscribeToTopic("blaaaa");

                                           if(isSelectedActivity != 1) {
                                               if (marker != null) { //if marker exists (not null or whatever)
                                                   mMarker.setPosition(latLng);
                                               } else {
                                                   marker = new MarkerOptions();
                                                   mMarker = mMap.addMarker(marker.position(latLng).draggable(true));
                                               }
                                           }
                                       }
                                   }
        );
        
    }

}
