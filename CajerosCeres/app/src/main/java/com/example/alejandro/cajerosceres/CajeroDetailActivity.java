package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class CajeroDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Ir al mapa", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intentMapa = new Intent(getApplicationContext(), MapaActivity.class);

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String entidadBancaria = (String) extras.get("entidadBancaria");
                double longitud = (double) extras.get("longitud");
                double latitud = (double) extras.get("latitud");
                String uriFotoCajero = (String) extras.get("uriFotoCajero");

                intentMapa.putExtra("entidadBancaria", entidadBancaria);
                intentMapa.putExtra("longitud", longitud);
                intentMapa.putExtra("latitud", latitud);
                intentMapa.putExtra("uriFotoCajero", uriFotoCajero);
                intentMapa.putExtra("cuantosCajeros", "unCajero");

                startActivity(intentMapa);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(CajeroDetailFragment.ARG_ITEM_ID,getIntent().getStringExtra(CajeroDetailFragment.ARG_ITEM_ID));
            CajeroDetailFragment fragment = new CajeroDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.cajero_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, CajeroListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}