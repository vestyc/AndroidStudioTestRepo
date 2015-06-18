package com.example.johnny.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button uploadButton;
    TextView display;

    Bitmap bitmap;
    String outputToSend;
    private final int RESULT_LOAD_IMG = 213;
    private final String webserviceURL = "http://10.0.0.2/GitSQL/sendimage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup widgets
        uploadButton = (Button) findViewById(R.id.testButton);
        display = (TextView) findViewById(R.id.textView);
    }

    public void buttonPress(View view) {

        if(R.id.testButton == view.getId()) {
            //upload test image into database
            Intent gallery = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, RESULT_LOAD_IMG);
        }
        else if(R.id.gotoAnotherButton == view.getId()) {
            Intent otherActivity = new Intent(MainActivity.this, OtherTest.class);
            startActivity(otherActivity);
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
                ImageView imgView = (ImageView) findViewById(R.id.imageView);

                // Set the Image in ImageView after decoding the String
                BitmapFactory.Options options = new BitmapFactory.Options();
                //Value to make decoder return smaller image size
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgDecodableString);

                imgView.setImageBitmap(bitmap);

                /************Writing image to local android memory************/
               // File file = new File(this.getFilesDir(), "testimage.png");

                sendImageToPHP();
            }
        }
        catch (Exception e) {

            Toast.makeText(this, "Error getting image from gallery", Toast.LENGTH_LONG).show();
            Log.e("MyTestingApp", "Error getting image from gallery", e);
        }
    }

    //Encodes a bitmap (separate thread), then sends to PHP webservice
    public void sendImageToPHP() throws IOException{

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                /*

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //compressing image
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteData = stream.toByteArray();
                //encoding Image to a String
                outputToSend = Base64.encodeToString(byteData,Base64.DEFAULT);



                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(webserviceURL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    //urlConnection.connect();

                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    //setup headers
                    //urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Content-Type", "image/png");
                    urlConnection.setRequestProperty("charset", "text/plain; charset=utf-8");

                    //setup image data
                    String parametersString = "image=" + outputToSend;
                    //String parametersString = "image=" + "herp derp test string hehe";
                    byte[] postData = parametersString.getBytes(StandardCharsets.UTF_8);
                    //int postDataLength = postData.length;
                    //urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    out.write(postData, 0, postData.length);

                   /* InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    byte[] inputBuff = new byte[300];
                    in.read(inputBuff);
                }
                catch(IndexOutOfBoundsException e) {
                    Log.e("BuffErr", "Index out of bounds", e);
                }
                catch(MalformedURLException e) {
                    Log.e("URL Error", "Trouble creating URL", e);
                }
                catch(ProtocolException e) {
                    Log.e("Protocol Exception", "Request Method broke.", e);
                }
                catch(NullPointerException e) {
                    Log.e("Null Pointer", "Null Pointer Exception", e);
                }
                catch(IOException e) {
                    Log.e("Connection Error", "Something wrong in sendImageToPHP", e);
                }
                finally {
                    urlConnection.disconnect();
                }*/
                return null;
            }

            //send result to PHP webservice
            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG, "Response from server: " + result);

                // showing the server response in an alert dialog
                showAlert(result);

                super.onPostExecute(result);

            }

            @SuppressWarnings("deprecation")
            private String uploadFile() {
                String responseString = null;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(webserviceURL);

                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new RecoverySystem.ProgressListener() {

                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });

                    File sourceFile = new File(filePath);

                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));

                    // Extra parameters if you want to pass to server
                    entity.addPart("website",
                            new StringBody("www.androidhive.info"));
                    entity.addPart("email", new StringBody("abc@gmail.com"));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }
        }.execute(null, null, null);
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
