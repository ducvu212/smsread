package com.example.root.smsread;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.root.smsread.api.ApiHandle;

import static com.example.root.smsread.Login_Activity.url;

// Todo : Activity Class

public class SMS_Receive extends AppCompatActivity implements ApiHandle.ApiCallback {


  /*  Button msg;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__receive);
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS
//            refreshSmsInbox();

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(SMS_Receive.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        // Todo : Receive Message And Number In Intent

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String address = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            TextView addressField = (TextView) findViewById(R.id.address);
            TextView messageField = (TextView) findViewById(R.id.message);

            // Todo : Set Number And Message In TextView

            addressField.setText("Message From : " +address);
            messageField.setText("Messsage : "+message);
            ApiHandle.SMS(getApplicationContext(), this, message, url);
            Log.d("MESSSSSS", message) ;
        }
       /* msg = (Button)findViewById(R.id.btn_msg);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onComplete(String str, String str2, Object obj) {

    }

    @Override
    public void onProgress(String str) {

    }
}
