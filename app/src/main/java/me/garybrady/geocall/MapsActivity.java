package me.garybrady.geocall;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //private GoogleMap mMap;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    @SuppressWarnings("unused")
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
    GoogleMap mMap;
    Marker marker;
    EditText et;
    Circle circle;
    SeekBar radiusSeekbar;
    Switch radiusSwitch;
    Button go, setAlarm;
    EditText searchedLocation;
    String rec_title = null;
    double rec_lat, rec_lng;
    int rec_id;
    Bundle b;
    TextView radiusOutput;
    private static final float DEFAULTZOOM = 15;
    @SuppressWarnings("unused")
    private static final String LOGTAG = "Maps";
    GoogleApiClient mGoogleApiClient;
    public static final String PREFS_NAME = "MaPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        radiusOutput = (TextView) findViewById(R.id.tvRadius);
    }

/////////////////////////

    private void previousAlarmReceived() {
        rec_id = b.getInt("id");
        rec_title = b.getString("title");
        rec_lat = b.getDouble("lat");
        rec_lng = b.getDouble("lng");
        LatLng latLng = new LatLng(rec_lat, rec_lng);
        mMap.clear();
        radiusSwitch.setVisibility(View.VISIBLE);
        radiusSeekbar.setVisibility(View.VISIBLE);
        MarkerOptions options = new MarkerOptions()
                .title(rec_title)
                .position(latLng);
        marker = mMap.addMarker(options);
        marker.setDraggable(true);
        circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(50)
                .strokeColor(Color.WHITE)
                .strokeWidth(3)
                .fillColor(0x6F323299)

        );
        go.setVisibility(View.INVISIBLE);
        setAlarm.setVisibility(View.VISIBLE);
        searchedLocation.setVisibility(View.INVISIBLE);

    }

    private void displayRadius() {

        if (radiusSeekbar.getProgress() < 50) {
            Toast.makeText(getApplicationContext(), "Minimum Distance is 50m", Toast.LENGTH_LONG).show();
            radiusSeekbar.setProgress(50);
        } else {
            Toast.makeText(getApplicationContext(), "Radius Distance: " + circle.getRadius() + "m", Toast.LENGTH_LONG).show();
        }
        /*
        if (radiusSwitch.isChecked()) {
            if (radiusSeekbar.getProgress() < 50) {
                Toast.makeText(getApplicationContext(), "Use meters instead of kilometers for a smaller radius", Toast.LENGTH_LONG).show();
                radiusSeekbar.setProgress(50);
            } else {
                Toast.makeText(getApplicationContext(), "Radius Distance: " + circle.getRadius() / 1000 + "Km", Toast.LENGTH_LONG).show();
            }
        } else {
            if (radiusSeekbar.getProgress() < 50) {
                Toast.makeText(getApplicationContext(), "Minimum Distance is 50m", Toast.LENGTH_LONG).show();
                radiusSeekbar.setProgress(50);
            } else {
                Toast.makeText(getApplicationContext(), "Radius Distance: " + circle.getRadius() + "m", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private void setRadius(int progress) {
        int tempRadius = 0;
        tempRadius = progress;
        circle.setRadius(tempRadius);
        /*if (radiusSwitch.isChecked()) {
            tempRadius = progress * 4;
            circle.setRadius(tempRadius);
        } else {
            tempRadius = progress;
            circle.setRadius(tempRadius);
        }*/
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);


    }

    /*public void geoLocate(View v) throws IOException {
        if (isNetworkAvailable()) {
            et = (EditText) findViewById(R.id.etLongGeoLocate);
            String location = searchedLocation.getText().toString();
            if (location.length() == 0) {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            hideSoftKeyboard(v);

            Geocoder gc = new Geocoder(this);
            try {
                List<Address> list = gc.getFromLocationName(location, 1);
                Address add = list.get(0);
                String locality = add.getLocality();
                Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

                double lat = add.getLatitude();
                double lng = add.getLongitude();

                gotoLocation(lat, lng, DEFAULTZOOM);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Address cannot be found", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot connect, please check internet connection", Toast.LENGTH_LONG).show();
        }

        //Add a marker to searched location

		*//*if(marker !=null){
			marker.remove();
		}

		MarkerOptions options = new MarkerOptions()
			.title(locality)
			.position(new LatLng(lat,lng));
		marker = mMap.addMarker(options);*//*


    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    */


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /*protected void gotoCurrentLocation() {
        LatLng ll = new LatLng(51.8968917, -8.486315699);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
        mMap.animateCamera(update);
        *//*
        //Location currentLocation = mLocationClient.getLastLocation();
        Location currentLocation = ("51.8968917","-8.486315699");
        if (currentLocation == null) {
            Toast.makeText(this, "Current location isn't available", Toast.LENGTH_SHORT).show();
        }
        else {
            LatLng ll = new LatLng(51.8968917,-8.486315699);
            //LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
            mMap.animateCamera(update);
        }
        *//*
    }*/


/////////////////////////

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
        mMap.setMyLocationEnabled(true);


        ///////////////////////////
/*        go=(Button)findViewById(R.id.bLongGeoLocate);
        go.setVisibility(View.VISIBLE);
*/
        setAlarm=(Button)findViewById(R.id.bSetGeofence);
        setAlarm.setVisibility(View.INVISIBLE);

        /*searchedLocation=(EditText)findViewById(R.id.etLongGeoLocate);
        searchedLocation.setVisibility(View.VISIBLE);*/


        radiusSeekbar=(SeekBar)findViewById(R.id.sbRadius);
        radiusSeekbar.setVisibility(View.INVISIBLE);

        radiusSeekbar.setMax(1000);
        radiusSeekbar.setProgress(50);
        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setRadius(progress);
                radiusOutput.setText(progress + " Meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //displayRadius();
            }
        });


        /*radiusSwitch=(Switch)findViewById(R.id.swRadius);
        radiusSwitch.setVisibility(View.INVISIBLE);
        radiusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setRadius(radiusSeekbar.getProgress());
                displayRadius();
            }
        });*/

        //set space for radius tools
        mMap.setPadding(0,0,0,70);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                //radiusSwitch.setVisibility(View.VISIBLE);
                radiusSeekbar.setVisibility(View.VISIBLE);
                MarkerOptions options = new MarkerOptions()
                        .position(latLng);
                marker = mMap.addMarker(options);
                marker.setDraggable(true);
                circle=mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(50)
                        .strokeColor(Color.WHITE)
                        .strokeWidth(3)
                        .fillColor(0x6F323299)

                );
                setAlarm.setVisibility(View.VISIBLE);
                radiusOutput.setText("50 Meters");
                /*go.setVisibility(View.INVISIBLE);

                searchedLocation.setVisibility(View.INVISIBLE);*/

            }

        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                circle.setCenter(marker.getPosition());
                circle.setStrokeWidth(5);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                circle.setCenter(marker.getPosition());
                circle.setStrokeWidth(10);
                circle.setStrokeColor(Color.BLUE);
                circle.setFillColor(0x6F323299);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                circle.setCenter(marker.getPosition());
                circle.setStrokeWidth(3);
                circle.setStrokeColor(Color.WHITE);
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle pass = new Bundle();
                if(b!=null){
                    pass.putString("title",b.getString("title"));
                }
                pass.putDouble("lat", marker.getPosition().latitude);
                pass.putDouble("lng", marker.getPosition().longitude);
                pass.putDouble("radius", circle.getRadius());
                //Intent i = new Intent(MapsActivity.this,GeofenceConstruct.class);
                Intent i = new Intent(MapsActivity.this,dummyGeo.class);
                i.putExtras(pass);
                startActivity(i);*/
                settings = getSharedPreferences(PREFS_NAME, 0);

                editor = settings.edit();
                editor.putBoolean("GeoExists",true);
                editor.putLong("lat", Double.doubleToRawLongBits(marker.getPosition().latitude));
                editor.putLong("lng", Double.doubleToRawLongBits(marker.getPosition().longitude));
                editor.putLong("radius", Double.doubleToRawLongBits(circle.getRadius()));

                // Commit the edits!
                editor.commit();
                Intent i = new Intent(MapsActivity.this,dummyGeo.class);

                startActivity(i);
                finish();
            }
        });

        b = getIntent().getExtras();
        if (b!=null){
            previousAlarmReceived();
        }


    }
}
