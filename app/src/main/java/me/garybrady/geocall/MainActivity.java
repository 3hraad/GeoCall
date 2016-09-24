package me.garybrady.geocall;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;*/
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    EditText etPhone;
    Button btToggle, btDisable, btStatus, fab, btGeo;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MaPrefs";
    ImageView ivToggle;
    String callForwardString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
        fab = (Button) findViewById(R.id.btFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMap = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(iMap);
            }
        });


        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        btToggle = (Button) findViewById(R.id.btSetNumber);
        //btDisable = (Button) findViewById(R.id.btDisable);
        btStatus = (Button) findViewById(R.id.btStatus);
        btGeo = (Button) findViewById(R.id.btGeo);
        //ivToggle = (ImageView) findViewById(R.id.ivToggle);
        //ivToggle.setColorFilter(Color.rgb(76,175,80));



        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean forwardCalls = settings.getBoolean("forwardCalls", false);
        if (forwardCalls) {
            btToggle.setText("On");
        } else {
            btToggle.setText("Off");
        }

/*        ivToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleClicked(ivToggle);
            }
        });*/
        btToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btToggle.getText().toString().toUpperCase().equals("OFF")) {
                    activateGeo();
                } else if (btToggle.getText().toString().toUpperCase().equals("ON")) {
                    deactivateGeo();
                } else {
                    Toast.makeText(MainActivity.this, btToggle.getText().toString().toUpperCase(), Toast.LENGTH_LONG).show();
                }

            }
        });

        btStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callForwardString = "*#21#";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL);
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intentCallForward);

            }
        });

        btGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGeo = new Intent(MainActivity.this, dummyGeo.class);
                startActivity(iGeo);
            }
        });
    }


    private void toggleClicked(View v) {
        int colourStart = Color.rgb(76, 175, 80);
        int colourEnd = Color.rgb(255, 82, 82);

        ValueAnimator colourAnim = ObjectAnimator.ofInt(v, "backgroundColor", colourStart, colourEnd);
        colourAnim.setDuration(500);
        colourAnim.setEvaluator(new ArgbEvaluator());
        colourAnim.setRepeatCount(0);
        //colourAnim.setRepeatMode(ValueAnimator.);
        colourAnim.start();

        //RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation anim = new RotateAnimation(-45.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.75f);
        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(0); //Repeat animation indefinitely
        anim.setDuration(500); //Put desired duration per anim cycle here, in milliseconds

        //Start animation
        v.startAnimation(anim);

    }

    private void deactivateGeo() {
        btToggle.setText("Off");
        editor = settings.edit();
        editor.putBoolean("forwardCalls", false);
        // Commit the edits!
        editor.commit();

        String callForwardString = "#21#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL);
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intentCallForward);
    }

    private void activateGeo() {
        btToggle.setText("On");
        editor = settings.edit();
        editor.putBoolean("forwardCalls", true);

        // Commit the edits!
        editor.commit();
        String callForwardString = "*21*" + etPhone.getText().toString() + "#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL);
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intentCallForward);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent iSettings = new Intent(MainActivity.this, settings.class);
            startActivity(iSettings);
        }

        return super.onOptionsItemSelected(item);
    }
    */

}
