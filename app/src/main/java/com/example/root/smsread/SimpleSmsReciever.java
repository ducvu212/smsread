package com.example.root.smsread;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.root.smsread.api.ApiHandle;

// Todo : SMS Reciever Class

public class SimpleSmsReciever extends BroadcastReceiver implements ApiHandle.ApiCallback {
    private static final String TAG = "Message recieved";
    private String content = "";
    private String number = "";

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, Login_Activity.class));
            if ((Object[]) intent.getExtras().get("pdus") != null) {
                for (Object obj : (Object[]) intent.getExtras().get("pdus")) {
                    SmsMessage messages = SmsMessage.createFromPdu((byte[]) obj);
                    this.content = messages.getMessageBody();
                    Log.d("TaggMes", this.content);
                    this.number = messages.getOriginatingAddress();
                }
            }
            String url = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("urlType", "");
            if (number != null) {
                Toast.makeText(context, "SMS Received From :" + this.number + "\n" + this.content, Toast.LENGTH_LONG).show();
                Log.d("TaggMesContent", this.content);
            }
            ApiHandle.SMS(context, this, this.content, url);
            return;
        } else {
            if ((Object[]) intent.getExtras().get("pdus") != null) {
                for (Object obj2 : (Object[]) intent.getExtras().get("pdus")) {
                    SmsMessage messages = SmsMessage.createFromPdu((byte[]) obj2);
                    this.content += messages.getMessageBody();
                    Log.d("TaggMes", this.content);
                    this.number = messages.getOriginatingAddress();
                }
            }
            Login_Activity.url = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("urlType", "");
            if (number != null) {

                Toast.makeText(context, "SMS Received From :" + this.number + "\n" + this.content, Toast.LENGTH_LONG).show();
                Log.d("TaggMesContent", this.content);
            }
            ApiHandle.SMS(context, this, this.content, Login_Activity.url);
        }
    }

    public void onComplete(String str, String str2, Object obj) {
    }

    public void onProgress(String str) {
    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
