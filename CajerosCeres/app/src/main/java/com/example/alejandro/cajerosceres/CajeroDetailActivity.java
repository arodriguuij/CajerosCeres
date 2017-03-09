package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;

public class CajeroDetailActivity extends AppCompatActivity {
    private DataBaseHelperCajeros dbhelper;
    private Interfaz comunicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton map = (FloatingActionButton) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMapa = new Intent(getApplicationContext(), MapaActivity.class);

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String entidadBancaria = (String) extras.get("entidadBancaria");
                double longitud = (double) extras.get("longitud");
                double latitud = (double) extras.get("latitud");
                String uriFotoCajero = (String) extras.get("uriFotoCajero");
                int fav = (int) extras.get("fav");

                intentMapa.putExtra("entidadBancaria", entidadBancaria);
                intentMapa.putExtra("longitud", longitud);
                intentMapa.putExtra("latitud", latitud);
                intentMapa.putExtra("uriFotoCajero", uriFotoCajero);
                intentMapa.putExtra("cuantosCajeros", "unCajero");
                intentMapa.putExtra("fav", fav);

                startActivity(intentMapa);
            }
        });
/*
        FloatingActionButton fav = (FloatingActionButton) findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "¿Añadir a favoritos?", Snackbar.LENGTH_LONG)
                        .setAction("Añadir", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent2 = new Intent(getApplicationContext(), CajeroListActivity.class);
                                Intent intent = getIntent();

                                Bundle extras = intent.getExtras();
                                Integer id = (Integer) extras.get("id");

                                dbhelper = new DataBaseHelperCajeros(getApplicationContext());
                                dbhelper.setFavoritoCajero(id, 1);
                                dbhelper.close();

                                intent2.putExtra("viaje_origen", (String) extras.get("viaje_origen"));
                                intent2.putExtra("viaje_destino", (String) extras.get("viaje_destino"));
                                navigateUpTo(intent2);


                                Intent intent = getIntent();
                                Bundle extras = intent.getExtras();
                                String entidadBancariaSeleccion= (String) extras.get("entidadBancariaSeleccion");
                                String entidadBancariaUsuario= (String) extras.get("entidadBancariaUsuario");
                                String orden= (String) extras.get("orden");


                                Intent Intent = new Intent(getApplicationContext(), CajeroListActivity.class);
                                Intent.putExtra("entidadBancariaUsuarioString", entidadBancariaSeleccion);
                                Intent.putExtra("entidadBancariaString", entidadBancariaUsuario);
                                Intent.putExtra("orden", orden);
                                startActivity(Intent);
                            }
                        }).show();
            }
        });
*/
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
