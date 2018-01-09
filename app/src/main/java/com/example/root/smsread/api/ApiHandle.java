package com.example.root.smsread.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ApiHandle implements ApiConstants, AppConstants {

    private static String respone;


    public interface ApiCallback {
        void onComplete(String str, String str2, Object obj);

        void onProgress(String str);
    }

    private static String defaultJsonSuccess() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiConstants.KEY_SUCCESS, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static RequestParams createRequestParams(List<NameValuePair> nameValuePairs) {
        RequestParams params = new RequestParams();
        if (nameValuePairs != null && nameValuePairs.size() > 0) {
            for (NameValuePair pair : nameValuePairs) {
                String name = "" + pair.getName();
                String value = "" + pair.getValue();
                if (value != null && value.trim().toLowerCase().equals("null")) {
                    value = "";
                }
                if (name.equals(ApiConstants.FILE_PARAM)) {
                    try {
                        params.put(ApiConstants.FILE_PARAM, new File(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    params.put(name, value);
                }
            }
        }
        return params;
    }

    private static void requestPostLogin(final Context context, final String url, String input1, String input2, RequestParams requestParams, final ApiCallback callback, final Object extra) {
        AppUtil.log("=========request: " + url);
        AppUtil.log("=========params: " + requestParams);
        AppUtil.log("=========extra: " + extra);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("client_id", input1);
            jsonParams.put("client_serect", input2);
            StringEntity strentity = new StringEntity(jsonParams.toString(), "UTF-8");
            AsyncHttpClient client = new AsyncHttpClient();
            System.out.println("json param : " + jsonParams);
            client.setConnectTimeout(60);
            client.post(context, url, strentity, RequestParams.APPLICATION_JSON, new TextHttpResponseHandler("UTF-8") {
                public void onSuccess(int arg0, Header[] arg1, String arg2) {
                    AppUtil.log(url + "==============onSuccess: " + arg2);
                    if (callback != null) {
                        callback.onComplete(url, arg2, extra);
                    }
                }

                @SuppressLint("ShowToast")
                public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                    AppUtil.log(url + "==============onFailure: " + arg2);
                    Toast.makeText(context.getApplicationContext(), "Không thể kết nối tới URL. Xin kiểm tra lại!",Toast.LENGTH_SHORT).show(); ;
                    if (callback != null) {
                        callback.onComplete(url, arg2, extra);
                    }
                    respone = "Không thể kết nối tới URL. Xin kiểm tra lại!";
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void requestPostSMS(final Context context, final String url, String input1, RequestParams requestParams, final ApiCallback callback, final Object extra) {
        AppUtil.log("=========request: " + url);
        AppUtil.log("=========params: " + requestParams);
        AppUtil.log("=========extra: " + extra);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put(AppConstants.EXTRA_MESSAGE, input1);
            StringEntity strentity = new StringEntity(jsonParams.toString(), "UTF-8");
            AsyncHttpClient client = new AsyncHttpClient();
            System.out.println("json param SMS : " + jsonParams);
            client.addHeader("username", "admin" );
            client.addHeader("password", "@@Admin1234" );
            client.setConnectTimeout(60);
            client.post(context, url, strentity, RequestParams.APPLICATION_JSON, new TextHttpResponseHandler("UTF-8") {
                public void onSuccess(int arg0, Header[] arg1, String arg2) {
                    AppUtil.log(url + "==============onSuccess: " + arg2);
                    Toast.makeText(context, "Kết nối thành công",Toast.LENGTH_SHORT) ;

                    if (callback != null) {
                        callback.onComplete(url, arg2, extra);
                    }
                }

                public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                    AppUtil.log(url + "==============onFailure: " + arg2);
                    Toast.makeText(context, "Không thể kết nối tới URL. Xin kiểm tra lại!",Toast.LENGTH_SHORT) ;
                    if (callback != null) {
                        callback.onComplete(url, arg2, extra);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void Login(Context context, ApiCallback callback, String entity1, String entity2, String entity3) {
        if (callback != null) {
            callback.onProgress(entity3);
        }
        requestPostLogin(context, entity3, entity1, entity2, createRequestParams(new ArrayList()), callback, null);
    }

    public static void SMS(Context context, ApiCallback callback, String entity1, String entity3) {
        if (callback != null) {
            callback.onProgress(entity3);
        }
        requestPostSMS(context, entity3, entity1, createRequestParams(new ArrayList()), callback, null);
    }
}
