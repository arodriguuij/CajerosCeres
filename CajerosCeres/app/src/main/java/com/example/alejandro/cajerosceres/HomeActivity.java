package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegistro;
    private Button buttonContinuarSinAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        inicializarComponentes();
    }

    public void inicializarComponentes() {

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent IntentLogin = new Intent(getApplicationContext(), MenuLateral.class);
                IntentLogin.putExtra("id", "Login");
                startActivity(IntentLogin);
            }
        });
        buttonRegistro = (Button) findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent IntentRegistro = new Intent(getApplicationContext(), MenuLateral.class);
                IntentRegistro.putExtra("id", "Crear");
                startActivity(IntentRegistro);
            }
        });
        buttonContinuarSinAcceder = (Button) findViewById(R.id.buttonContinuarSinAcceder);
        buttonContinuarSinAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuLateral.class);
                intent.putExtra("id", "Buscar");
                startActivity(intent);
            }
        });
    }
}
