package com.example.root.smsread;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.root.smsread.api.ApiHandle;

import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener, ApiHandle.ApiCallback {

    private Spinner spnType;
    private EditText edtUrl, edtId, edtSerect;
    private List<String> listType;
    private String type;
    public static String url;
    public static String TaiKhoan, MatKhau, urlType;
    private Button btnOk, btnEdt;
    private int Pos;
    private final int MY_PERMISSION = 123;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("com.example.root.smsread.count", MODE_PRIVATE) ;
        SharedPreferences.Editor editorr = preferences.edit() ;
        preferences.getInt("count", 0);

        if (preferences.getInt("count", 0) == 1) {

            new AlertDialog.Builder(this)
                    .setTitle("Xin cấp quyền cho ứng dụng")
                    .setMessage("Bạn hãy cấp quyền truy cập SMS và Bộ Nhớ để ứng dụng có thể hoạt động. " +
                            "Hãy nhấn Đồng ý (Allow) để cấp quyền ")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).create().show();
            editorr.putInt("count", 2 ) ;
            editorr.apply();
//
        }
        checkPer();

        preferences.getInt("count", 0);
        findViewByIds();
        inits();
        setEvents();
        checkData();
    }

    private void checkPer() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Login_Activity.this, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS

            }, MY_PERMISSION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Login_Activity.this, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS

            }, MY_PERMISSION);
        }


            new AlertDialog.Builder(this)
                    .setTitle("Xin cấp quyền cho ứng dụng")
                    .setMessage("Bạn hãy cấp quyền truy cập SMS và Bộ Nhớ để ứng dụng có thể hoạt động. " +
                            "Hãy nhấn Cancel nếu đã cấp quyền ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    Login_Activity.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 101);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {


                        onBackPressed();
                        return;
                    } if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {


                        onBackPressed();
                        return;
                    }
                    else {
                        dialog.dismiss();
                    }

                }
            }).create().show();


    }


    private void setEvents() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listType);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnType.setAdapter(adapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Pos == i) {
                    type = spnType.getItemAtPosition(i).toString();
                } else {
                    Toast.makeText(getBaseContext(), spnType.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    type = spnType.getSelectedItem().toString();
                    Pos = i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void inits() {
        listType = new ArrayList<>();
        listType.add("http://");
        listType.add("https://");

    }

    private void findViewByIds() {
        spnType = findViewById(R.id.spn_type);
        edtId = findViewById(R.id.edt_id);
        edtSerect = findViewById(R.id.edt_serect);
        edtUrl = findViewById(R.id.edt_url);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnEdt = findViewById(R.id.btn_edt);
        btnEdt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_ok:
                Login();
                break;

            case R.id.btn_edt:
                btnOk.setVisibility(View.VISIBLE);
                edtUrl.setEnabled(true);
                edtUrl.setFocusableInTouchMode(true);
                edtId.setFocusableInTouchMode(true);
                edtId.setEnabled(true);
                edtSerect.setFocusableInTouchMode(true);
                edtSerect.setEnabled(true);

                break;

            default:

        }
    }

    public void Login() {
        TaiKhoan = this.edtId.getText().toString();
        MatKhau = this.edtSerect.getText().toString();
        url = this.edtUrl.getText().toString();
        urlType = spnType.getItemAtPosition(Pos) + this.edtUrl.getText().toString();
        SharedPreferences sp = getSharedPreferences("com.example.root.smsread", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("url", url);
        editor.putString("urlType", urlType);
        editor.putString("client_id", TaiKhoan);
        editor.putString("client_serect", MatKhau);
        editor.putInt("type", Pos);


        editor.apply();

        if (url.matches("")) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập URL", Toast.LENGTH_SHORT).show();
        } else if (TaiKhoan.matches("")) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Client ID", Toast.LENGTH_SHORT).show();
        } else if (MatKhau.matches("")) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Client Serect", Toast.LENGTH_SHORT).show();
        } else {
            url = url.trim();
            url = url.replaceAll("\\s+", " ");
            Log.d("AAAAAAAAAAAA", this.type + url);
            ApiHandle.Login(getApplicationContext(), this, TaiKhoan, MatKhau, this.type + url.trim());
            Log.d("Loginnnn", TaiKhoan + "\n" + MatKhau + "\n" + this.type + url);
            btnOk.setVisibility(View.GONE);
            edtUrl.setEnabled(false);
            edtUrl.setFocusable(false);
            edtId.setFocusable(false);
            edtId.setEnabled(false);
            edtSerect.setFocusable(false);
            edtSerect.setEnabled(false);
        }
    }


    private void checkData() {
        SharedPreferences sp = getSharedPreferences("com.example.root.smsread", Activity.MODE_PRIVATE);
        String url = sp.getString("url", "");
        String client_id = sp.getString("client_id", "");
        String client_serect = sp.getString("client_serect", "");
        int typePos = sp.getInt("type", 0);

        Log.d("CHECKDATA", spnType.getItemAtPosition(typePos) + url + "\n" + client_id + "\n" + client_serect + "\n");

        if (!url.equals("") && !client_id.equals("") && !client_serect.equals("")) {

            edtUrl.setText(url);
            edtId.setText(client_id);
            edtSerect.setText(client_serect);
            spnType.setSelection(typePos);

            btnOk.setVisibility(View.GONE);
            edtUrl.setEnabled(false);
            edtUrl.setFocusable(false);
            edtId.setFocusable(false);
            edtId.setEnabled(false);
            edtSerect.setFocusable(false);
            edtSerect.setEnabled(false);
        }
    }

    public void onProgress(String api) {
        showLoadingDialog();
    }

    public void onComplete(String api, String result, Object extra) {
        dismissLoadingDialog();

    }

    public void showLoadingDialog() {
        try {
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissLoadingDialog() {
        try {
            findViewById(R.id.loading).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
