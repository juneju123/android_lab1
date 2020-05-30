package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        CheckBox cb = findViewById(R.id.cb);
        String s = cb.getResources().getString(R.string.toast_message);
        String onOff=cb.isChecked()?" on":" off";
        boolean b = cb.isChecked();
        cb.setOnClickListener(e -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show());
        cb.setOnCheckedChangeListener((c, isChecked) -> {
            Snackbar.make(cb,"The switch is now"+onOff,Snackbar.LENGTH_INDEFINITE).setAction( "undo", click -> cb.setChecked(!b)).show();
        });
        Switch sw = findViewById(R.id.switchBtn);
        //  String s =cb.getResources().getString(R.string.toast_message);
        //String onOff=sw.isChecked()?" on":" off";
        // boolean b = sw.isChecked();
        sw.setOnCheckedChangeListener((c, isChecked) -> {
            Snackbar.make(cb,"The switch is now"+onOff,Snackbar.LENGTH_INDEFINITE).setAction( "undo", click -> sw.setChecked(!b)).show();
        });

    }
}