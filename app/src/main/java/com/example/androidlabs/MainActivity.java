package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefs = null;
    private EditText EmailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("FileName",MODE_PRIVATE);
        String savedEmail = prefs.getString("ReserveName","");
        EmailField = findViewById(R.id.Email_et);
        EmailField.setText(savedEmail);
        Button LoginBtn = findViewById(R.id.Login_button);
        LoginBtn.setOnClickListener(bt ->{//onPause();
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            goToProfile.putExtra("Email",EmailField.getText().toString());
            startActivity(goToProfile);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName",EmailField.getText().toString());
        editor.commit();

    }
}