package com.example.root.smsread;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.root.smsread.api.ApiHandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// Todo : SMS Reciever Class

public class SimpleSmsReciever extends BroadcastReceiver implements ApiHandle.ApiCallback {
    private static final String TAG = "Message recieved";
    private String content = "";
    private String number = "";
    private int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            try {
                Intent pushIntent = new Intent(context, SimpleSmsReciever.class);
                context.startService(pushIntent);

                for (Object obj2 : (Object[]) intent.getExtras().get("pdus")) {
                    SmsMessage messages = SmsMessage.createFromPdu((byte[]) obj2);
                    if (!messages.getMessageBody().equals("")) {
                        this.content += messages.getMessageBody();
                        Log.d("TaggMes", this.content);
                    }
                    this.number = messages.getDisplayOriginatingAddress();


                    //Resolving the contact name from the contacts.
//                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
//                    Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
//                    try {
//                        c.moveToFirst();
//                        String  displayName = c.getString(0);
//                        String ContactName = displayName;
//                        Toast.makeText(context, ContactName, Toast.LENGTH_LONG).show();
//                        if (!ContactName.equals("")){
//                            number = ContactName ;
//                        }
//
//                    } catch (NullPointerException e) {
//                        // TODO: handle exception
//                    }finally{
//                        c.close();
//                    }

                }

                if (number.equals("MSB")) {
                    number = "MSB";
                    writeToFile(content, context, "sms.txt", Context.MODE_APPEND);
                    writeToFile(content, context, "count.txt", Context.MODE_PRIVATE);
                    if (readCount(context).equals("3 ")) {
                        readFromFile(context, "sms.txt");
                    }

                } else {

//                    Toast.makeText(context, "SMS Received From :" + this.number + "\n" + this.content, Toast.LENGTH_LONG).show();
                    writeToFile(content, context, "content.txt", Context.MODE_APPEND);
                    Intent smsIntent = new Intent(context, SMS_Receive.class);
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    smsIntent.putExtra("MessageNumber", number);
                    smsIntent.putExtra("Message", content);
                    readFromFile(context, "content.txt");
                }


            } catch (NullPointerException ignored) {
            }

            return;

        } else {

            try {
                Intent pushIntent = new Intent(context, SimpleSmsReciever.class);
                context.startService(pushIntent);

                for (Object obj2 : (Object[]) intent.getExtras().get("pdus")) {
                    SmsMessage messages = SmsMessage.createFromPdu((byte[]) obj2);
                    if (!messages.getMessageBody().equals("")) {
                        this.content += messages.getMessageBody();
                        this.content = this.content.replaceAll("\n", " ") ;
                        Log.d("TaggMes", this.content);
                    }
                    this.number = messages.getDisplayOriginatingAddress();


                    //Resolving the contact name from the contacts.
//                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
//                    Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
//                    try {
//                        c.moveToFirst();
//                        String  displayName = c.getString(0);
//                        String ContactName = displayName;
//                        Toast.makeText(context, ContactName, Toast.LENGTH_LONG).show();
//                        if (!ContactName.equals("")){
//                            number = ContactName ;
//                        }
//
//                    } catch (NullPointerException e) {
//                        // TODO: handle exception
//                    }finally{
//                        c.close();
//                    }

                }

                if (number.equals("MSB")) {
                    number = "MSB";
                    writeToFile(content, context, "sms.txt", Context.MODE_APPEND);
                    writeToFile(content, context, "count.txt", Context.MODE_PRIVATE);
                    if (readCount(context).equals("3 ")) {
                        readFromFile(context, "sms.txt");
                    }

                } else {

//                    Toast.makeText(context, "SMS Received From :" + this.number + "\n" + this.content, Toast.LENGTH_LONG).show();
                    writeToFile(content, context, "content.txt", Context.MODE_APPEND);
                    Intent smsIntent = new Intent(context, SMS_Receive.class);
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    smsIntent.putExtra("MessageNumber", number);
                    smsIntent.putExtra("Message", content);
                    readFromFile(context, "content.txt");
                }


            } catch (NullPointerException ignored) {
            }

        }
    }


    public void onComplete(String str, String str2, Object obj) {
    }

    public void onProgress(String str) {
    }

    private void writeToFile(String data, Context context, String name, int mode) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name, mode));
            if (name.equals("content.txt")) {
                outputStreamWriter.append(data).append("\r\n");
            } else {
                outputStreamWriter.append(data).append(" ");
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void readFromFile(Context context, String name) {

        Log.d("NAMEEE", name);
        try {

            InputStream inputStream = context.openFileInput(name);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    String url = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("urlType", "");
                    String username = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("client_id", "");
                    String password = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0).getString("client_serect", "");

                    ApiHandle.SMS(context, this, receiveString, url, username, password, number);
                    Log.d("ReadFile", receiveString);
                }

                Log.d("CheckCOn", isNetworkAvailable(context) + "");
                if (bufferedReader.readLine() == null && isNetworkAvailable(context)) {
                    File file = new File("/data/data/com.example.root.smsread/files/" + name);
                    boolean delete = file.delete();
                    Log.d("Coooooooooooo", delete + "");

                }
                inputStream.close();

            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }

    private String readCount(Context context) {

        String receive = "";

        try {

            InputStream inputStream = context.openFileInput("count.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {

                    if (receiveString.equals(" 3")) {
                        readFromFile(context, "sms.txt");
                    }
                    Log.d("ReadFileCount", receiveString);
                    receive = receiveString;
                    if (bufferedReader.readLine() == null && isNetworkAvailable(context) && receiveString.equals("3")) {
                        File file = new File("/data/data/com.example.root.smsread/files/" + "count.txt");
                        boolean delete = file.delete();
                        Log.d("Coooooooooooo", delete + "");

                    }
                }

                Log.d("CheckCOn", isNetworkAvailable(context) + "");

                inputStream.close();

            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Log.d("ReadFileCount", receive);

        return receive;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}