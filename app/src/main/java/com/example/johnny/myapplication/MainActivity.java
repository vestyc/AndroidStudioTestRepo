package com.example.johnny.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup button
        uploadButton = (Button) findViewById(R.id.testButton);
    }

    public void buttonPress(View view) {

        //upload test image into database
    }
}
