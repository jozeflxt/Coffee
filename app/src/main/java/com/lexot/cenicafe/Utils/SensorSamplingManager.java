package com.lexot.cenicafe.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.lexot.cenicafe.MainActivity;

import java.util.List;

public class SensorSamplingManager implements SensorEventListener {

    private float[] mGravity;
    private float[] mGeomagnetic;

    private String accs = ";;;";
    private String linAccs = ";;;";
    private String gyrs = ";;;";
    private String magn = ";;;";
    private String rots = ";;;";
    private String alti = ";";
    private String gpss = ";;";

    Integer satellites = 0;
    Integer satellitesInFix = 0;

    private Sensor senGyroscope;
    private Sensor senLinAccelerometer;
    private Sensor senMagnetometer;
    private Sensor senRotation;
    private Sensor senAltitude;

    private long lastUpdate = 0;
    private String dataSensores = "";


    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Context context;

    Handler handler = new Handler();
    private boolean runningCode = false;
    // Define the code block to be executed
    Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            long curTime = System.currentTimeMillis();
            float Rm[] = new float[9];
            float Im[] = new float[9];
            String orient = ";;;";
            if(mGravity != null && mGeomagnetic != null) {
                boolean success = android.hardware.SensorManager.getRotationMatrix(Rm, Im, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    android.hardware.SensorManager.getOrientation(Rm, orientation);
                    orient = ((Float) orientation[0]).toString() + ";";
                    orient += ((Float) orientation[1]).toString() + ";";
                    orient += ((Float) orientation[2]).toString() + ";";
                }
            }
            dataSensores += ((Long) (curTime - lastUpdate)).toString() + ";" + accs +linAccs+ gyrs + rots +orient+ magn+alti + gpss + "\n";
            handler.postDelayed(runnableCode, 10);
        }
    };

    public SensorSamplingManager(SensorManager sensorManager, LocationManager locationManager) {
        this.senSensorManager = sensorManager;
        this.locationManager = locationManager;
        setupSensors();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyrs = ((Float) sensorEvent.values[0]).toString() + ";" + ((Float) sensorEvent.values[1]).toString() + ";" + ((Float) sensorEvent.values[2]).toString() + ";";
        }
        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = sensorEvent.values;
            magn = ((Float) sensorEvent.values[0]).toString() + ";" + ((Float) sensorEvent.values[1]).toString() + ";" + ((Float) sensorEvent.values[2]).toString() + ";";
        }
        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            rots = ((Float) sensorEvent.values[0]).toString() + ";" + ((Float) sensorEvent.values[1]).toString() + ";" + ((Float) sensorEvent.values[2]).toString() + ";";
        }
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            linAccs = ((Float) sensorEvent.values[0]).toString() + ";" + ((Float) sensorEvent.values[1]).toString() + ";" + ((Float) sensorEvent.values[2]).toString() + ";";
        }
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = sensorEvent.values;
            accs = ((Float) sensorEvent.values[0]).toString() + ";" + ((Float) sensorEvent.values[1]).toString() + ";" + ((Float) sensorEvent.values[2]).toString() + ";";
        }
        if (mySensor.getType() == Sensor.TYPE_PRESSURE) {
            alti = ((Float) sensorEvent.values[0]).toString() + ";";
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @SuppressLint("MissingPermission")
    public void setupSensors() {
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senLinAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senMagnetometer = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senRotation = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        senAltitude = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        senSensorManager.registerListener(this, senRotation, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManager.registerListener(this, senLinAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManager.registerListener(this, senMagnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManager.registerListener(this, senAltitude, SensorManager.SENSOR_DELAY_FASTEST);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            gpss = location.getLatitude() + ";" + location.getLongitude() + ";";
        }
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                gpss = location.getLatitude() + ";" + location.getLongitude() + ";";
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int i) {
                satellites = 0;
                satellitesInFix = 0;
                for (GpsSatellite sat : locationManager.getGpsStatus(null).getSatellites()) {
                    if (sat.usedInFix()) {
                        satellitesInFix++;
                    }
                    satellites++;
                }
            }
        });
    }

    public void stopSampling() {
        handler.removeCallbacks(runnableCode);
        locationManager.removeUpdates(locationListener);
        senSensorManager.unregisterListener(this);
        runningCode = false;
    }

    @SuppressLint("MissingPermission")
    public void startSampling() {
        if (!runningCode) {
            lastUpdate = System.currentTimeMillis();
            handler.post(runnableCode);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);
            runningCode = true;
        }
    }

    public String getSensorInfo(List<String> focusModes) {
        String message = "Lista de sensores:\n\n";
        List<Sensor> sensors = senSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < sensors.size(); i++) {
            message += "Nombre: " + sensors.get(i).getName() + "\n";
            message += "Resolución: " + sensors.get(i).getResolution() + "\n";
            message += "Vendor: " + sensors.get(i).getVendor() + "\n";
            message += "Tipo: " + sensors.get(i).getType() + "\n\n\n";
        }
        if (focusModes != null) {
            message += "FocusModes: \n\n";
            for (int i = 0; i < focusModes.size(); i++) {
                message += focusModes.get(i) + "\n";
            }
        }
        message += "\n\n\nNúmero de satelites \n";
        message += satellites.toString()+"\n\n";
        message += "Número de satelites usados en la última geolocalización\n";
        message += satellitesInFix.toString()+"\n\n";
        return message;
    }

    public String getSensorData() {
        return dataSensores;
    }

}
