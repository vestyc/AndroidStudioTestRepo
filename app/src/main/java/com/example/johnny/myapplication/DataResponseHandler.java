package com.example.johnny.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import java.util.ArrayList;

/**
 * Created by Johnny on 8/13/2015.
 */
public class DataResponseHandler extends AsyncHttpResponseHandler implements OnJSONResponse {

    JSONObject jsonObject;
    JSONArray jsonArray;
    String[] names;
    ArrayList<Bitmap> flyers;
    ArrayAdapter adapter_names;
    CustomAdapter adapter_images;

    /*public DataResponseHandler(String[] names, ArrayAdapter adapter_names) {

        this.names = names;
        this.adapter_names = adapter_names;
    }*/

    public DataResponseHandler(ArrayList<Bitmap> flyers, CustomAdapter adapter_images) {

        this.flyers = flyers;
        this.adapter_images = adapter_images;
    }

    @Override
    public void onStart(){

    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        try {
            jsonArray = new JSONArray(new String(responseBody, "UTF-8"));
            this.onJSONResponse(this.flyers, this.adapter_images);

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

    public void onJSONResponse(ArrayList<Bitmap> flyers, CustomAdapter adapter) {

        JSONObject obj1, obj2, obj3;
        byte[] decodedData;
        Bitmap image;
        try {
            obj1 = jsonArray.getJSONObject(0);
            decodedData = Base64.decode(obj1.getString("data"), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

            //names[0] = obj1.getString("name");
            flyers.add(image);

            obj2 = jsonArray.getJSONObject(1);
            decodedData = Base64.decode(obj2.getString("data"), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

            //names[1] = obj2.getString("name");
            flyers.add(image);

            obj3 = jsonArray.getJSONObject(2);
            decodedData = Base64.decode(obj3.getString("data"), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

            //names[2] = obj3.getString("name");
            flyers.add(image);

            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            Log.e("JSON err", "getJSONObject error", e);
        }
    }
}
