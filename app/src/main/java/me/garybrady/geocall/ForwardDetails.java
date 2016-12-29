package me.garybrady.geocall;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.internal.overlay.zzo;

public class ForwardDetails extends AppCompatActivity {
    EditText PhoneNum;
    Button SaveDetails;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MaPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_details);
        settings = getSharedPreferences(PREFS_NAME, 0);

        PhoneNum = (EditText) findViewById(R.id.etPhoneForward);
        SaveDetails = (Button) findViewById(R.id.btSaveForwardDetails);


        String PN = settings.getString("PhoneNumber","");
        if(PN.equals("")){
            PhoneNum.setHint("Phone Number please");
        }else{
            PhoneNum.setText(PN);
        }

        SaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = settings.edit();
                editor.putString("PhoneNumber", PhoneNum.getText().toString());
                // Commit the edits!
                editor.commit();
                finish();
            }
        });
    }
}
