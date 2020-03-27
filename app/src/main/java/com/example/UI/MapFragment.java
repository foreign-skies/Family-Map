package com.example.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.UI.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public MapFragment() {
        // Required empty public constructor
    }

    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

         SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);
         return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    LatLng Maharashtra = new LatLng(19.169257,73.3431601);
    map.addMarker(new MarkerOptions().position(Maharashtra).title("Maharashtra"));
    map.moveCamera(CameraUpdateFactory.newLatLng(Maharashtra));
    }
}
