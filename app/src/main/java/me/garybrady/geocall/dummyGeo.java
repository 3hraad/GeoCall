package me.garybrady.geocall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class dummyGeo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_geo);
        Bundle b = getIntent().getExtras();
        double rec_lat=0, rec_lng=0, rec_rad=0;
        TextView output = (TextView) findViewById(R.id.tvOutput);
        if (b!=null){

            rec_lat = b.getDouble("lat");
            rec_lng = b.getDouble("lng");
            rec_rad = b.getDouble("radius");
            //LatLng latLng = new LatLng(rec_lat, rec_lng);
        }
        output.setText("Lat: " + rec_lat + "\nLong: " + rec_lng + "\nRadius: " + rec_rad);
    }
}
