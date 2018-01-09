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

import static com.example.root.smsread.Login_Activity.url;

// Todo : SMS Reciever Class

public class SimpleSmsReciever extends BroadcastReceiver implements ApiHandle.ApiCallback {

    private static final String TAG = "Message recieved";
    private String content = "";
    private String number = "" ;

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, Login_Activity.class));
            for (int i = 0; i < ((Object[]) intent.getExtras().get("pdus")).length; i++) {
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) intent.getExtras().get("pdus"))[i]);
                content = messages.getMessageBody();
                Log.d("TaggMes", content);
                number = messages.getOriginatingAddress() ;
            }
            String url = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("urlType", "");
//            Intent smsIntent = new Intent(context, SMS_Receive.class);
//            smsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
//            smsIntent.putExtra("Message", );
            Toast.makeText(context, "SMS Received From :" + number + "\n" + content, Toast.LENGTH_LONG).show();
//            Log.d("Testtttt", url);
            Log.d("TaggMesContent", content);
            ApiHandle.SMS(context, this, content, url);
            return;
        } else {
            for (int i = 0; i < ((Object[]) intent.getExtras().get("pdus")).length; i++) {
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) intent.getExtras().get("pdus"))[i]);
                content = content + messages.getMessageBody();
                Log.d("TaggMes", content);
                number = messages.getOriginatingAddress() ;
            }
            url = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("urlType", "");
//            Intent smsIntent = new Intent(context, SMS_Receive.class);
//            smsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
//            smsIntent.putExtra("Message", messages.getMessageBody());
//            Toast.makeText(context, "SMS Received From :" + messages.getOriginatingAddress() + "\n" + messages.getMessageBody(), Toast.LENGTH_LONG).show();
//            Log.d("Testtttt", url);
            Toast.makeText(context, "SMS Received From :" + number + "\n" + content, Toast.LENGTH_LONG).show();

            Log.d("TaggMesContent", content);
            ApiHandle.SMS(context, this, content, url);
        }

//        if (!isNetworkAvailable(context)) {
//            SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) intent.getExtras().get("pdus"))[0]);
//            Log.d("TAGGGGGGG", "false") ;
//            SharedPreferences spt = context.getSharedPreferences("com.example.root.smsread.content", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor edt = spt.edit();
//            edt.clear() ;
//            edt.putString("content", messages.getMessageBody()) ;
//            edt.apply();
//        } else {
//            SharedPreferences spt = context.getSharedPreferences("com.example.root.smsread.content", Activity.MODE_PRIVATE);
//
//            String content = spt.getString("content", "");
//            if (!content.equals("")) {
//                ApiHandle.SMS(context, this, content, url);
//            }
//
//
//        }
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