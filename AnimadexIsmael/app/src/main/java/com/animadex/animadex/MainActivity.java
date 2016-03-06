package com.animadex.animadex;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView animal = (ImageView)findViewById(R.id.imageView);
        animal.setImageResource(R.drawable.main_light);
        final TextView mTextView = (TextView) findViewById(R.id.summary);
        mTextView.setText("Some Text");
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    }
}


