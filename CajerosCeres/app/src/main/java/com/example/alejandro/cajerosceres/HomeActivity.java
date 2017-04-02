package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;

public class HomeActivity extends AppCompatActivity {
    private Button buttonAcceder;
    private Button buttonStopServicio;
    private DataBaseHelperEntidadesBancarias dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        buttonAcceder = (Button) findViewById(R.id.buttonAcceder);
        buttonStopServicio = (Button) findViewById(R.id.buttonStopServicio);

        buttonAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
            }
        });
        buttonStopServicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Cancelación
                /*
                AlarmManager am = (AlarmManager) HomeActivity.this.getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), ExampleBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pendingIntent);
                */
            }
        });
    }
}
