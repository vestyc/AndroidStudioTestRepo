package com.example.johnny.myapplication;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


/**
 * Created by kaif on 8/6/2015.
 */
public interface OnJSONResponse {

        public void onJSONResponse(ArrayList<Bitmap> flyers, CustomAdapter adapter);

}
