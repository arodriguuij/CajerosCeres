package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    // Método que se acciona al tocar la pantalla
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        // Controla que solo se cuente un evento en la pantalla (una sola dirección de pulsación)
        if(action == MotionEvent.ACTION_DOWN) {

            // Ejecución del BroadcastReceiver que ejecuta la obtención de datos mediante JSON del opendata de Cáceres
            // y la creación de una alarma que obtiene estos cada 24 horas
            Intent intentBroadcastReceiver = new Intent(getApplicationContext(), BroadcastReceiverAuto.class);
            sendBroadcast(intentBroadcastReceiver);

            // Intent que llama a la actividad principal de la aplicación
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }
        return super.onTouchEvent(event);
    }
}
