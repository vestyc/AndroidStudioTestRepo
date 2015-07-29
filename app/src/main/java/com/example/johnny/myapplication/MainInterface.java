package com.example.johnny.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MainInterface extends AppCompatActivity {

    Integer index = 0;
    JSONObject jsonObject;
    String receptionString;

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();

    private final String getDataURL = "http://10.0.2.2:80/GitSQL/getdata.php";

    String names[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        ImgGet imgGet = new ImgGet();

        while(index<9){
            params.put("index", index.toString());
            client.post(this.getDataURL, params,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            //parse json data
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(new String(responseBody, StandardCharsets.UTF_8));
                                byte[] decodedData = Base64.decode(jsonObject.getString("data"), Base64.DEFAULT);
                                Bitmap image = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

                                if(jsonObject == null){
                                    names[index] = "null";
                                }

                                else{
                                    names[index] = jsonObject.getString("name");
                                }

                            }catch(JSONException e) {
                                Log.e("JSONException", "JSONException was thrown", e);
                                return;
                            }

                            CallBack
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            names[index] = "Failure";
                        }
                    });

            index++;
        }

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
}
