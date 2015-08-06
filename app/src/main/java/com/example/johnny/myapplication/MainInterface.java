package com.example.johnny.myapplication;

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

public class MainInterface extends AppCompatActivity implements OnJSONResponse{

    Integer index = 0;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String receptionString;

    ListAdapter flyer_names;
    ListView lv;

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();

    OnJSONResponse callBack;

    private final String getDataURL = "http://10.0.2.2:80/GitSQL/getdata.php";

    String names[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        ImgGet imgGet = new ImgGet();

        imgGet.getDataFromServer();

        ListAdapter flyer_names = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(flyer_names);

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

    public void onJSONResponse(JSONArray response) {

        JSONObject obj1, obj2, obj3;
        try {
            obj1 = response.getJSONObject(0);
            names[0] = obj1.getString("name");
            obj2 = response.getJSONObject(1);
            names[1] = obj2.getString("name");
            obj3 = response.getJSONObject(2);
            names[2] = obj3.getString("name");

            Toast.makeText(this, names[2], Toast.LENGTH_LONG).show();
        }catch(JSONException e) {
            Log.e("JSON err", "getJSONObject error", e);
        }

        //flyer_names = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.names);
        //lv.setAdapter(flyer_names);
    }
}
