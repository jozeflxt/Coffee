package com.lexot.cenicafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
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
import com.lexot.cenicafe.Models.CoffeeLatLng;

import java.util.ArrayList;
import java.util.List;

public class BatchMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static String CREATING_PARAM = "creatingParam";
    public static String BATCH_ID_PARAM = "batchIdParam";
    private PolygonOptions rectOptions;
    private Polygon polygon;
    private List<LatLng> coordinates = new ArrayList<>();
    private boolean isCreating;
    private BLL bll;
    private int batchId;
    private View fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_map);
        isCreating = getIntent().getBooleanExtra(CREATING_PARAM,true);
        batchId = getIntent().getIntExtra(BATCH_ID_PARAM,0);
        bll = new BLL(this);
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCreating) {
                    bll.createCoordinates(coordinates, batchId);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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
        rectOptions = new PolygonOptions();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (isCreating) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location location) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
                    mMap.setOnMyLocationChangeListener(null);
                }
            });
        }
        if (isCreating) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                    coordinates.add(latLng);
                    if (coordinates.size() > 2) {
                        fab.setVisibility(View.VISIBLE);
                    }
                    if (polygon == null) {
                        rectOptions.add(latLng);
                        polygon = mMap.addPolygon(rectOptions);
                    }
                    drawRegion();
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    drawRegion();
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    drawRegion();
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    drawRegion();
                }
            });
        } else {
            ArrayList<CoffeeLatLng> coffeeLatLngs = bll.getCoordinates(batchId);
            for (int j=0; j<=coffeeLatLngs.size() - 1; j++) {
                coordinates.add(new LatLng(coffeeLatLngs.get(j).Lat, coffeeLatLngs.get(j).Lng));
            }
            if (coordinates.size() > 0) {
                if (polygon == null) {
                    rectOptions.add(coordinates.get(0));
                    polygon = mMap.addPolygon(rectOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 20));
                    mMap.setOnMyLocationChangeListener(null);
                }
                drawRegion();
            }
        }
    }

    public void drawRegion() {
        polygon.setPoints(coordinates);
    }

}
