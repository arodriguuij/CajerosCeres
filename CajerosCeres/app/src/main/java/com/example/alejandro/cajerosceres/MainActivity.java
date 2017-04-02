package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Interfaz {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity);
        toolbar.setTitle("CajerosCeres");
        setSupportActionBar(toolbar);

        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawerLayout);
        // Detecta eventos abiertos y cerrados del menu lateral
        // Extiende de la clase ActionBarDrawerToggle al incluir una actionBar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer_header);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; esto agrega items a la action bar si está presente.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    /* App bar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), PrefActivity.class);
                startActivity(intent);
                return true;
            case R.id.Ayuda:
                onNavigationItemSelected(item);
                return true;
            case R.id.Buscar:
                onNavigationItemSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Barra lateral */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.Buscar)
            fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
        else
            if (id == R.id.Ayuda)
                fragmentManager.beginTransaction().replace(R.id.content_main, new AyudaFragment()).commit();
                else
                    if (id == R.id.Favoritos) {
                        responderBusquedaListaCajeros("Todas", "BancoPopular", "favoritos");
                    }

        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void responderBusquedaMapaCajeros(String entidadBancariaString, String entidadBancariaUsuarioString) {
        Intent Intent = new Intent(getApplicationContext(), MapaActivity.class);
        Intent.putExtra("entidadBancariaUsuarioString", entidadBancariaUsuarioString);
        if(entidadBancariaString.equals("Todas"))
            Intent.putExtra("cuantosCajeros", "todosCajeros");
        else {
            Intent.putExtra("cuantosCajeros", entidadBancariaString);
        }
        startActivity(Intent);
    }

    @Override
    public void responderBusquedaListaCajeros(String entidadBancariaString, String entidadBancariaUsuarioString, String orden) {
        Intent Intent = new Intent(getApplicationContext(), CajeroListActivity.class);
        Intent.putExtra("entidadBancariaUsuarioString", entidadBancariaUsuarioString);
        Intent.putExtra("entidadBancariaString", entidadBancariaString);
        Intent.putExtra("orden", orden);

        guardarConfiguracion(entidadBancariaUsuarioString, entidadBancariaString, orden);
        /*
        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        libras = sharedPref.getBoolean(PrefFragment.KEY_PREF_MONEDA_LIBRAS, false);
        */
                startActivity(Intent);
    }

    //guardar configuración aplicación Android usando SharedPreferences
    public void guardarConfiguracion(String entidadBancariaUsuarioString, String entidadBancariaString, String orden) {
        SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("entidadBancariaUsuarioString",entidadBancariaUsuarioString);
        editor.putString("entidadBancariaString", entidadBancariaString);
        editor.putString("orden", orden);
        editor.commit();
    }

}
