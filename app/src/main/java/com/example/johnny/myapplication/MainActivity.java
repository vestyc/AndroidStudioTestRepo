package com.example.johnny.myapplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;



public class MainActivity extends Activity {

    Button uploadButton;
    Button otherTest;
    TextView display;

    Bitmap bitmap;
    String outputToSend;
    String fileName;
    final Integer index = 0;
    private final int RESULT_LOAD_IMG = 213;
    private static final int REQUEST_IMG_CAPTURE = 214;

    private final String webserviceURL = "http://192.168.0.101:80/GitSQL/sendimage.php";
    private final String getDataURL = "http://192.168.0.101:80/GitSQL/getdata.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup widgets
        otherTest = (Button) findViewById(R.id.gotoAnotherButton);
        uploadButton = (Button) findViewById(R.id.testButton);
        display = (TextView) findViewById(R.id.textView);
    }

    public void buttonPress(View view) {

        if (R.id.testButton == view.getId()) {
            //upload test image into database
            AlertDialog.Builder chooser = new AlertDialog.Builder(this);
            chooser.setTitle("Get Image from Gallery or Camera");
            //chooser.setCancelable(true);
            String[] image_sources = {"Camera", "Gallery"};
            chooser.setItems(image_sources, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 1) {
                        Intent post_flyer = new Intent(Intent.ACTION_GET_CONTENT);
                        post_flyer.setType("image/*");
                        startActivityForResult(post_flyer, RESULT_LOAD_IMG);
                    }

                    if (which == 0) {
                        Intent takePictureIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMG_CAPTURE);
                        }
                    }
                }
            });
            AlertDialog dialog = chooser.create();
            dialog.show();
        }

        else if (R.id.getButton == view.getId()) {
            this.getDataFromServer();

        }

        else if (R.id.gotoAnotherButton == view.getId()) {
            Intent otherActivity = new Intent(MainActivity.this, MainInterface.class);
            startActivity(otherActivity);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {

                //save image URI
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                //Code to get the filename of the image
                File f = new File("" + selectedImage);

                fileName = f.getName();


                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView);

                // Set the Image in ImageView after decoding the String
                BitmapFactory.Options options = new BitmapFactory.Options();
                //Value to make decoder return smaller image size
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgDecodableString);

                imgView.setImageBitmap(bitmap);

                ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, tempStream);
                byte[] tba = tempStream.toByteArray();
                this.outputToSend = Base64.encodeToString(tba, 0);
                differentHTTP();
            } else if (requestCode == REQUEST_IMG_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBit = (Bitmap) extras.get("data");
                ImageView imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageBitmap(imageBit);

                ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
                imageBit.compress(Bitmap.CompressFormat.PNG, 100, tempStream);
                byte[] tba = tempStream.toByteArray();
                this.outputToSend = Base64.encodeToString(tba, 0);
                differentHTTP();
            }
        } catch (Exception e) {

            Toast.makeText(this, "Error getting image from gallery", Toast.LENGTH_LONG).show();
            Log.e("MyTestingApp", "Error getting image from gallery", e);
        }
    }

    //using loopj http
    public void differentHTTP() {

        //setting up http parameters
        RequestParams params = new RequestParams();
        params.put("image", this.outputToSend);
        params.put("name", fileName);

        //create http connection with parameters
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this.webserviceURL, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //success message
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //handle error codes
                    }
                });
    }


    public void getDataFromServer() {

        RequestParams params = new RequestParams();
        params.put("index", index.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this.getDataURL, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {

                        TextView view = (TextView) findViewById(R.id.textView);
                        view.setText("Starting connection...");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        //parse json data
                        JSONArray jsonArray;
                        JSONObject jsonObject1;
                        JSONObject jsonObject2;
                        JSONObject jsonObject3;

                        try {
                            jsonArray = new JSONArray((new String(responseBody, StandardCharsets.UTF_8)));
                            jsonObject1 = jsonArray.getJSONObject(0);
                            jsonObject2 = jsonArray.getJSONObject(1);
                            jsonObject3 = jsonArray.getJSONObject(2);
                            byte[] decodedData = Base64.decode(jsonObject1.getString("data"), Base64.DEFAULT);
                            Bitmap image = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

                            ImageView imView = (ImageView) findViewById(R.id.imageView);
                            imView.setImageBitmap(image);

                            TextView txtView = (TextView) findViewById(R.id.textView);
                            txtView.setText("image name: " + jsonObject1.getString("name"));
                            txtView.append("\ndate added: " + jsonObject1.getInt("date"));

                        } catch (JSONException e) {
                            Log.e("JSONException", "JSONException was thrown", e);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        TextView view = (TextView) findViewById(R.id.textView);
                        view.setText("Failure :c");
                    }
                });
        return;
    }
}