package me.garybrady.geocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by C175148 on 30/Mar/2016.
 */
public class SMSListener extends BroadcastReceiver {

    public static final String PREFS_NAME = "MaPrefs";
    String msgBody;

    //settings;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        boolean forwardCalls = settings.getBoolean("forwardCalls", false);
        if (forwardCalls) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            //noinspection deprecation
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            msgBody = msgs[i].getMessageBody();
                        }
                        Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    }
}
