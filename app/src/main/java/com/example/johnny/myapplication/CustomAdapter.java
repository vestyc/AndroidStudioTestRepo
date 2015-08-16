package com.example.johnny.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by kaif on 8/16/2015.
 */
class CustomAdapter extends ArrayAdapter<Bitmap>{


    CustomAdapter(Context context, ArrayList<Bitmap> flyers) {
        super(context, R.layout.flyer_layout, flyers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.flyer_layout, parent, false);

        Bitmap getFlyerImage = getItem(position);
        ImageView imageView = (ImageView) customView.findViewById(R.id.flyer_image);

        imageView.setImageBitmap(getFlyerImage);

        return customView;
    }
}
