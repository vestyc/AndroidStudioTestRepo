package com.example.johnny.myapplication;

import com.loopj.android.http.*;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class OtherTest extends Activity {

    Button uploadButton;
    TextView display;

    Bitmap bitmap;
    String outputToSend;
    private final int RESULT_LOAD_IMG = 213;
    private final String webserviceURL = "http://10.0.2.2:81/GitSQL/sendimage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_test);

        //setup widgets
        uploadButton = (Button) findViewById(R.id.testButton2);
        display = (TextView) findViewById(R.id.textView2);
    }

    public void buttonPress(View view) {

        if(R.id.testButton2 == view.getId()) {
            //upload test image into database
            Intent gallery = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, RESULT_LOAD_IMG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {

                //save image URI
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView2);

                // Set the Image in ImageView after decoding the String
                BitmapFactory.Options options = new BitmapFactory.Options();
                //Value to make decoder return smaller image size
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgDecodableString);

                imgView.setImageBitmap(bitmap);

                sendImageToPHP();
            }
        }
        catch (Exception e) {

            Toast.makeText(this, "Error getting image from gallery", Toast.LENGTH_LONG).show();
            Log.e("MyTestingApp", "Error getting image from gallery", e);
        }
    }

    //Encodes a bitmap (separate thread), then sends to PHP webservice
    public void sendImageToPHP() throws IOException {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //compressing image
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byteData = stream.toByteArray();
                //encoding Image to a String
                outputToSend = Base64.encodeToString(byteData, 0);

                return "";
            }

            //send result to PHP webservice
            @Override
            protected void onPostExecute(String result) {

                asyncSending();
            }
        }.execute(null, null, null);
    }

    public void asyncSending() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.webserviceURL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                RequestParams params = new RequestParams();
                params.put("image", outputToSend);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
