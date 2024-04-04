package com.example.alzheimertracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView locationTextView;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;

    // TextViews for displaying sensor data
    private TextView accelerometerTextView;
    private TextView gyroscopeTextView;
    private TextView magnetometerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize TextViews
        locationTextView = findViewById(R.id.location_text_view);
        accelerometerTextView = findViewById(R.id.accelerometer_text_view);
        gyroscopeTextView = findViewById(R.id.gyroscope_text_view);
        magnetometerTextView = findViewById(R.id.magnetometer_text_view);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }

        // Initialize SensorManager and sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Check if sensors are available and register them
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            accelerometerTextView.setText("Accelerometer not available");
        }

        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            gyroscopeTextView.setText("Gyroscope not available");
        }

        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            magnetometerTextView.setText("Magnetometer not available");
        }
    }

    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(this);
        Location location = gpsTracker.getLocation();

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
        } else {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
        }

        // Register a location listener to update the location dynamically
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Update accelerometer TextView
            accelerometerTextView.setText("Accelerometer:\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Update gyroscope TextView
            gyroscopeTextView.setText("Gyroscope:\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Update magnetometer TextView
            magnetometerTextView.setText("Magnetometer:\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener to prevent battery drain
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener again
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}


//package com.example.alzheimertracker;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.example.alzheimertracker.GPSTracker;
//import com.example.alzheimertracker.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private TextView locationTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        locationTextView = findViewById(R.id.location_text_view);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            getLocation();
//        }
//    }
//
////    private void getLocation() {
////        GPSTracker gpsTracker = new GPSTracker(this);
////        Location location = gpsTracker.getLocation();
////
////        if (location != null) {
////            double latitude = location.getLatitude();
////            double longitude = location.getLongitude();
////            locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
////        } else {
////            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
////        }
////    }
//
//    private void getLocation() {
//        GPSTracker gpsTracker = new GPSTracker(this);
//        Location location = gpsTracker.getLocation();
//
//        if (location != null) {
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
//        } else {
//            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
//        }
//
//        // Register a location listener to update the location dynamically
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(@NonNull Location location) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
//                }
//
//                @Override
//                public void onProviderEnabled(@NonNull String provider) {}
//
//                @Override
//                public void onProviderDisabled(@NonNull String provider) {}
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {}
//            });
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation();
//            } else {
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}
