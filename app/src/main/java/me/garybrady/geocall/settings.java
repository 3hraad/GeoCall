package me.garybrady.geocall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;*/
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class settings extends AppCompatActivity {

    EditText etPhone;
    Button btSetPhone, btDisable, btStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        btSetPhone = (Button) findViewById(R.id.btSetNumber);
       // btDisable = (Button) findViewById(R.id.btDisable);
        btStatus = (Button) findViewById(R.id.btStatus);

        btSetPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callForwardString = "*21*" + etPhone.getText().toString()+ "#";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL);
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                if (ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

        btDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callForwardString = "#21#";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL);
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                if (ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

        btStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callForwardString = "*#21#";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL);
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                if (ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
    }

}
