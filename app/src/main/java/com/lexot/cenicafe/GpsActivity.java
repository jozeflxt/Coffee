package com.lexot.cenicafe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.lexot.cenicafe.Models.BLL;

import java.util.ArrayList;
import java.util.List;

public class GpsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String BATCH_ID_PARAM = "batchIdParam";
    private PolygonOptions rectOptions;
    private GoogleMap mMap;
    private Polygon polygon;
    private BLL bll;
    private int batchId;
    private List<LatLng> coordinates = new ArrayList<>();
    private View fab;
    private View track;
    private int coordinatesNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        batchId = getIntent().getIntExtra(BATCH_ID_PARAM,0);
        bll = new BLL(this);
        
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bll.createCoordinates(coordinates, batchId);
                setResult(RESULT_OK);
                finish();
            }
        });

        track = findViewById(R.id.track);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(coordinates.get(coordinates.size() - 1)).draggable(true));
                marker.setTag(coordinatesNum);
                coordinatesNum = coordinatesNum + 1;
                if (coordinatesNum > 2) {
                    fab.setVisibility(View.VISIBLE);
                }
                drawRegion();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        rectOptions = new PolygonOptions();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                if (coordinates.size() == coordinatesNum + 1) {
                    coordinates.set(coordinatesNum, new LatLng(location.getLatitude(), location.getLongitude()));
                } else {
                    coordinates.add(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                drawRegion();
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                changeLatLng((int)marker.getTag(), marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                changeLatLng((int)marker.getTag(), marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                changeLatLng((int)marker.getTag(), marker.getPosition());
            }
        });

    }


    public void changeLatLng(int index, LatLng latLng) {
        coordinates.set(index, latLng);
        drawRegion();
    }

    public void drawRegion() {
        if (polygon == null) {
            rectOptions.add(coordinates.get(0));
            polygon = mMap.addPolygon(rectOptions);
        }
        polygon.setPoints(coordinates);
    }
}
