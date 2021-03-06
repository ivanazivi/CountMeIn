package com.countmein.countmein.services;

/**
 * Created by zivic on 6/6/2017.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class FcmNotificationBuilder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String SERVER_API_KEY = "AAAARXv134M:APA91bHdA-e9B5VYgx9g8Ekg4R8gJUzf1B9d5AFJmqB3CzJmjaG8GV_yqeuJ44Wp4oL2gHSdCWobAK8AehQOf7hdV-SMvXAFU0OTaRag3Z9R0lm3OPslJ9INl_p5T4bpB7RpV4XRPeXF";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATA = "data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_UID = "uid";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    private String mTitle;
    private String mMessage;
    private String mUsername;
    private String mUid;
    private String mFirebaseToken;
    private String mReceiverFirebaseToken;
    private ArrayList<String> mReceiversFirebaseTokens;

    private FcmNotificationBuilder() {

    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder title(String title) {
        mTitle = title;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }

    public FcmNotificationBuilder username(String username) {
        mUsername = username;
        return this;
    }

    public FcmNotificationBuilder uid(String uid) {
        mUid = uid;
        return this;
    }

    public FcmNotificationBuilder firebaseToken(String firebaseToken) {
        mFirebaseToken = firebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiversFirebaseTokens(ArrayList<String> receiversFirebaseTokens) {
        mReceiversFirebaseTokens = receiversFirebaseTokens;
        return this;
    }

    public void send() {
        RequestBody remoteMessage = null;
        try {
            remoteMessage = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(remoteMessage)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();


        JSONArray jsArray = new JSONArray(mReceiversFirebaseTokens);
        jsonObjectBody.put("to", "dAGGHZWUp6Y:APA91bGoL3ObP5h5jbgJQXG-hGEbOIOqxJuumMw9idyF4N5FVO3tKt95LiBSLUxsTXh-SGzCKjUoxf72UbZQpVJDE2rm6186eGbde9JHVWLye__4B-HdYMDv__lBnTgaBlt2dcJ7mvCJ");

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_TITLE, mTitle);
        jsonObjectData.put(KEY_TEXT, mMessage);
        jsonObjectData.put(KEY_USERNAME, mUsername);
        jsonObjectData.put(KEY_UID, mUid);
        jsonObjectData.put(KEY_FCM_TOKEN, mFirebaseToken);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);

        return jsonObjectBody;
    }
}