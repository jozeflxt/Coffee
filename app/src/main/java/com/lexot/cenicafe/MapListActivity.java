package com.lexot.cenicafe;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeTree;

import java.util.ArrayList;
import java.util.List;

public class MapListActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<CoffeeTree> listTreees = new ArrayList<>();
    private BLL bll;

    private Float[] colors = {0.0F,
            30.0F,
            60.0F,
            120.0F,
            180.0F,
            210.0F,
            240.0F,
            270.0F,
            300.0F,
            330.0F};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bll =  new BLL(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        listTreees = bll.getTreesForMap();

        for (CoffeeTree tree:
             listTreees) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(tree.Lat, tree.Lng)).title(tree.toString())).setIcon(BitmapDescriptorFactory.defaultMarker(colors[tree.BatchId % 10]));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tree.Lat, tree.Lng), 20));
        }

    }
}
