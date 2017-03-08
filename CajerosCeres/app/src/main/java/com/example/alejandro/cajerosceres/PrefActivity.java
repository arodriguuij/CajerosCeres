package com.example.alejandro.cajerosceres;

import android.app.Activity;
import android.os.Bundle;

public class PrefActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mostramos el contenido de la pantalla de preferencias.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }
}
