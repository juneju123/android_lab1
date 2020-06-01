package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        CheckBox cb = findViewById(R.id.cb);
        Button btn = findViewById(R.id.button);
        String s = cb.getResources().getString(R.string.toast_message);


        btn.setOnClickListener(e -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show());
        cb.setOnCheckedChangeListener((c, b) -> {
            String onOff= b?cb.getResources().getString(R.string.on):cb.getResources().getString(R.string.off);
            Snackbar.make(cb,cb.getResources().getString(R.string.snackbar_msg)+onOff,Snackbar.LENGTH_LONG).setAction( cb.getResources().getString(R.string.undo), click -> cb.setChecked(!b)).show();
        });
        Switch sw = findViewById(R.id.switchBtn);
        sw.setOnCheckedChangeListener((c, b) -> {
           String onOff= b?sw.getResources().getString(R.string.on):sw.getResources().getString(R.string.off);
            Snackbar.make(cb,cb.getResources().getString(R.string.snackbar_msg)+onOff,Snackbar.LENGTH_LONG).setAction( cb.getResources().getString(R.string.undo), click -> sw.setChecked(!b)).show();
        });


    }
}