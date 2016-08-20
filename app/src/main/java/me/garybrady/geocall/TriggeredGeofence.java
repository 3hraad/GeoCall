package me.garybrady.geocall;
 /*
 * Activity that is called from reciveTransitionIntentService
 * */
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import java.util.Collections;
import java.util.List;

/**
 * Created by Gary on 06/03/14.
 */
public class TriggeredGeofence extends Activity {
    MediaPlayer mp;
    Vibrator vibrator;
    int geoIDs[];
    String array[];
    String received;
    Button bStop;
    int realAlarms=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test);

        Bundle bun = getIntent().getExtras();
//        geoIDs =  bun.getIntArray("id");
        received=bun.getString("id");

    }
}


