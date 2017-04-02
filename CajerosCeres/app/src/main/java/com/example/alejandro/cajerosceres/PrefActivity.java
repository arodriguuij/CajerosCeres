package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PrefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_activity);

        getFragmentManager().beginTransaction().replace(R.id.content_preference, new PrefFragment()).commit();
    }
}
