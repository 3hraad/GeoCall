package me.garybrady.geocall;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 33,
            MY_PERMISSIONS_REQUEST_RECEIVE_SMS=34,
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=35,
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=36;
    EditText etPhone, etEmail;
    Button SaveSettings;
    Switch swCallForward, swTextForward, swGeo;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MaPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        swCallForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swCallForward.isChecked()) {
                    ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    Toast.makeText(SettingsActivity.this, "Permissions can only be revoked through Android App Settings", Toast.LENGTH_SHORT).show();
                    startInstalledAppDetailsActivity(SettingsActivity.this);
                }
            }
        });

        swTextForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swTextForward.isChecked()) {

                    ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
                } else {
                    Toast.makeText(SettingsActivity.this, "Permissions can only be revoked through Android App Settings", Toast.LENGTH_SHORT).show();
                    startInstalledAppDetailsActivity(SettingsActivity.this);
                }
            }
        });

        swGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swGeo.isChecked()) {
                    //ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.ACCESS_LO},MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else {
                    Toast.makeText(SettingsActivity.this, "Permissions can only be revoked through Android App Settings", Toast.LENGTH_SHORT).show();
                    startInstalledAppDetailsActivity(SettingsActivity.this);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        swGeo= (Switch) findViewById(R.id.swGeo);
        swCallForward = (Switch) findViewById(R.id.swCall);
        swTextForward = (Switch) findViewById(R.id.swText);
        SaveSettings = (Button) findViewById(R.id.btSaveSettings);
        settings = getSharedPreferences(PREFS_NAME, 0);
        String PN = settings.getString("PhoneNumber","");
        if(PN.equals("")){
            etPhone.setHint("Phone Number please");
        }else{
            etPhone.setText(PN);
        }

        SaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = settings.edit();
                editor.putString("PhoneNumber", etPhone.getText().toString());
                // Commit the edits!
                editor.commit();
                finish();
            }
        });
        initSwitches();

    }

    private void initSwitches() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

            swCallForward.setChecked(true);

        } else {

            swCallForward.setChecked(false);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){

            swTextForward.setChecked(true);

        } else {

            swTextForward.setChecked(false);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){

            swGeo.setChecked(true);

        } else {

            swGeo.setChecked(false);
        }
    }


    public boolean checkCallPermission(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,android.Manifest.permission.CALL_PHONE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SettingsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

                // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        return true;
    }


    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SettingsActivity.this, "Deniend", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
