package com.example.johnny.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Created by User on 7/24/2015.
 */
public class ImgGet{

    TextView tv;

    private final String getDataURL = "http://10.0.2.2:80/GitSQL/getdata.php";
    JSONObject jsonObject;
    public String returnString;
    String index;

    public ImgGet(){

    }

    public String getDataFromServer(String flyer_number) {

        index = flyer_number;
        RequestParams params = new RequestParams();
        params.put("index",index);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this.getDataURL, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart(){

                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            jsonObject = new JSONObject(new String(responseBody, StandardCharsets.UTF_8));
                            returnString = jsonObject.toString();

                            if(jsonObject == null){
                                throw new NullPointerException();
                            }


                        }catch(JSONException e) {
                            Log.e("JSONException", "JSONException was thrown", e);
                            return;
                        }catch(NullPointerException e) {
                            Log.e("Null Pointer thrown", "Null pointer thrown", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("JSONException", "JSONException was thrown");
                        return;
                    }
                });
        return returnString;
    }
}
