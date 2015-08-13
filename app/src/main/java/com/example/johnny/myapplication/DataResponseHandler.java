package com.example.johnny.myapplication;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Johnny on 8/13/2015.
 */
public class DataResponseHandler extends AsyncHttpResponseHandler implements OnJSONResponse {

    JSONObject jsonObject;
    JSONArray jsonArray;
    String[] names;
    ArrayAdapter adapter;

    public DataResponseHandler(String[] names, ArrayAdapter adapter) {

        this.names = names;
        this.adapter = adapter;
    }

    @Override
    public void onStart(){

    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        try {
            jsonArray = new JSONArray(new String(responseBody, "UTF-8"));
            this.onJSONResponse(this.names, this.adapter);

        }catch(JSONException e) {
            Log.e("JSONException", "JSONException was thrown", e);
            return;
        }catch(NullPointerException e) {
            Log.e("Null Pointer thrown", "Null pointer thrown", e);
        }catch(UnsupportedEncodingException e) {
            Log.e("Charset Error", "Charset not supported", e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e("JSONException", "JSONException was thrown");
        return;
    }

    public void onJSONResponse(String[] names, ArrayAdapter adapter) {

        JSONObject obj1, obj2, obj3;
        try {
            obj1 = jsonArray.getJSONObject(0);
            names[0] = obj1.getString("name");
            obj2 = jsonArray.getJSONObject(1);
            names[1] = obj2.getString("name");
            //obj3 = jsonArray.getJSONObject(2);
            //names[2] = obj3.getString("name");

            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            Log.e("JSON err", "getJSONObject error", e);
        }
    }
}
