package com.example.hypervergeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String savedExtra = getIntent().getStringExtra("response");
        TextView myText = (TextView) findViewById(R.id.textId);
        myText.setText(savedExtra);
    }


}