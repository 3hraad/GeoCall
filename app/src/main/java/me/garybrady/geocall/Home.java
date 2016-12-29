package me.garybrady.geocall;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Home extends AppCompatActivity {
    CheckBox PermGrant, PhoneSet, LocationSet;
    SeekBar EnableGeo;
    TextView tvOn, tvOff, tvStatus;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Bundle pass;
    public static final String PREFS_NAME = "MaPrefs";
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {


        //Checking Phone number
        PhoneSet = (CheckBox) findViewById(R.id.cbPhoneNum);
        PhoneSet.setChecked(checkPhoneNumber());
        PhoneSet.setClickable(false);
        PhoneSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ForwardDetails.class);
                startActivity(i);
            }
        });


        //Checking Permissions
        PermGrant = (CheckBox) findViewById(R.id.cbPermGranted);
        //PermissionsGranted = checkPermissionsGranted();
        PermGrant.setChecked(checkPermissionsGranted());
        PermGrant.setClickable(false);
        PermGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        //Checking Location is set
        LocationSet = (CheckBox) findViewById(R.id.cbLocationSet);
        LocationSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, MapsActivity.class);
                startActivity(i);
            }
        });

        //Enable GeoCall
        pass = new Bundle();
        EnableGeo = (SeekBar) findViewById(R.id.sbEnableGeoCall);
        tvOff = (TextView) findViewById(R.id.tvOff);
        tvOn = (TextView) findViewById(R.id.tvOn);
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        if (mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false)){
            tvOn.setClickable(false);
            tvOff.setClickable(true);

            EnableGeo.setProgress(78);
        }else{
            tvOff.setClickable(false);
            tvOn.setClickable(true);
            EnableGeo.setProgress(22);
        }
        EnableGeo.setEnabled(false);
        /*if (checkCriteria()){
            EnableGeo.setProgress(78);
        }else{
            EnableGeo.setProgress(22);
        }*/

        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.putBoolean("create", false);
                Intent i = new Intent(Home.this,dummyGeo.class);
                i.putExtras(pass);
                startActivity(i);
            }
        });

        tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.putBoolean("create", true);
                Intent i = new Intent(Home.this,dummyGeo.class);
                i.putExtras(pass);
                startActivity(i);
            }
        });

        //test output
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        if (mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false)){
            tvStatus.setText("true");
        }else{
            tvStatus.setText("false");
        }

    }

    private boolean checkPhoneNumber() {
        settings = getSharedPreferences(PREFS_NAME, 0);


        String PN = settings.getString("PhoneNumber","");
        if(PN.equals("")){
            PhoneSet.setText("No Phone Number Set");
            return false;
        }else{
            PhoneSet.setText("Phone Number to Forward to: " + PN);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private Boolean checkPermissionsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return false;

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            return false;

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;

        }
        return true;
    }

    private Boolean checkCriteria(){
        if (PhoneSet.isChecked() && PermGrant.isChecked() && LocationSet.isChecked()){
            return true;
        } else {
            return false;
        }
    }

}
