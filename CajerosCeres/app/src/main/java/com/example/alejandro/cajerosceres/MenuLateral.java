package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuLateral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Interfaz {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_lateral);
        // Detecta eventos abiertos y cerrados del menu lateral
        //Extiende de la clase ActionBarDrawerToggle al incluir una actionBar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        String id = (String) getIntent().getExtras().getString("id");
        if (id.equals("Login"))
            fragmentManager.beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
        else
            if (id.equals("Registro"))
                fragmentManager.beginTransaction().replace(R.id.content_main, new RegistroFragment()).commit();
            else
                if (id.equals("ContinuarSinAcceder"))
                    fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_lateral);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.Perfil:
                onNavigationItemSelected(item);
                return true;
            case R.id.action_settings:
                getFragmentManager().beginTransaction().replace(android.R.id.content, new AjustesFragment()).addToBackStack(null).commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Buscar) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
        } else
            if (id == R.id.Registrarse) {
                fragmentManager.beginTransaction().replace(R.id.content_main, new RegistroFragment()).commit();
            } else
                if (id == R.id.Login) {
                    fragmentManager.beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
                } else
                    if (id == R.id.Perfil) {
                        fragmentManager.beginTransaction().replace(R.id.content_main, new PerfilFragment()).commit();
                    } else
                        if (id == R.id.Favoritos) {
                            fragmentManager.beginTransaction().replace(R.id.content_main, new FavoritosFragment()).commit();
                        } else
                            if (id == R.id.Ayuda) {
                                fragmentManager.beginTransaction().replace(R.id.content_main, new AyudaFragment()).commit();
                            }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_lateral);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void responderBusquedaMapaCajeros() {
        Intent Intent = new Intent(getApplicationContext(), MapaActivity.class);
        startActivity(Intent);
    }

    @Override
    public void responderBusquedaListaCajeros() {
        Intent Intent = new Intent(getApplicationContext(), CajeroListActivity.class);
        startActivity(Intent);
    }

    @Override
    public void responderRegistro() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
    }

    @Override
    public void responderLogin() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }

    @Override
    public void responderActualizarUsuario() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }

    @Override
    public void responderBorrarUsuario() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }

    @Override
    public void PerfilToFavoritos() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new FavoritosFragment()).commit();
    }

    @Override
    public void PerfilToActualizar() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();
    }
}
