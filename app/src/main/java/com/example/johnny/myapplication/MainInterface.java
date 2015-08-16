package com.example.johnny.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainInterface extends AppCompatActivity {

    Integer index = 0;

    private final String getDataURL = "http://10.0.2.2:80/GitSQL/getdata.php";

    ArrayList<Bitmap> flyers = new ArrayList<Bitmap>();

    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_img);

    ListAdapter flyer_names;
    ListView lv;

    String names[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        //initial listview setup with junk data
        this.flyer_names = new CustomAdapter(this, flyers);
        this.lv = (ListView) findViewById(R.id.listView);
        this.lv.setAdapter(flyer_names);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //grabbing data from server, updating list view
        RequestParams params = new RequestParams();
        params.put("index", index);
        DataResponseHandler responseHandler = new DataResponseHandler(flyers, (CustomAdapter) flyer_names);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this.getDataURL, params, responseHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
