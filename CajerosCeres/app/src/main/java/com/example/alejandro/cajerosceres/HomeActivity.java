package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button buttonAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        buttonAcceder = (Button) findViewById(R.id.buttonAcceder);
        buttonAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuLateral.class);
                intent.putExtra("id", "Acceder");
                startActivity(intent);
            }
        });
    }
}
