package com.example.alejandro.cajerosceres;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Interfaz, LocationListener {
    private LocationManager handle; //Gestor del servicio de localización
    private String provider;
    private static final String TAG = "LocationActivity";
    private double longitudUser;
    private double latitudUser;
    private static final int REQUEST_COARSE_LOCATION = 999;
    private Location loc;
    private Criteria c;


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

        c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        //Crea el objeto que gestiona las localizaciones
        handle = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        obtenerLocalizacion();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer_header);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new BusquedaFragment()).commit();

    }

    public void obtenerLocalizacion() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
        }
        else {
            /*
            Log.i(TAG, "Permisos necesarios OK!.");

            // Obtiene el mejor proveedor en función del criterio asignado (la mejor precisión posible)
            try {
                provider = handle.getBestProvider(c, true);

                // Se activan las notificaciones de localización con los parámetros:
                // proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
                handle.requestLocationUpdates(provider, 10000, 1, this);
                //Obtenemos la última posición conocida dada por el proveedor
                loc = handle.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                latitudUser = loc.getLatitude();
                longitudUser = loc.getLongitude();
                Log.i(TAG, "latitudUser= " + latitudUser + "longitudUser" + longitudUser);
                setSharedPreferences();

            } catch (Exception e) {
                Log.i(TAG, "Exception while fetch GPS at: " + e.getMessage());
            }
            */
            obtenerLocalizacion2();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // Obtiene el mejor proveedor en función del criterio asignado (la mejor precisión posible)
                    try {
                        provider = handle.getBestProvider(c, true);
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            obtenerLocalizacion();
                        }
                        else {
                            handle.requestLocationUpdates(provider, 10000, 1, this);
                            loc = handle.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                            latitudUser = loc.getLatitude();
                            longitudUser = loc.getLongitude();
                            Log.i(TAG, "latitudUser= " + latitudUser + "longitudUser" + longitudUser);
                            setSharedPreferences();
                        }
                            //Toast.makeText(getBaseContext()," latitud:" + latitudUser + ", longitd:" + longitudUser, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.i(TAG, "Exception while fetch GPS at: " + e.getMessage());
                    }

                } else {
                    //Permission denied
                    obtenerLocalizacion();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void obtenerLocalizacion2(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e(TAG, "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }
        Log.i(TAG, "Permisos necesarios OK!.");

        //Crea el objeto que gestiona las localizaciones
        handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        // Obtiene el mejor proveedor en función del criterio asignado (la mejor precisión posible)
        try {
            provider = handle.getBestProvider(c, true);
            // Se activan las notificaciones de localización con los parámetros:
            // proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
            handle.requestLocationUpdates(provider, 10000, 1, this);
            //Obtenemos la última posición conocida dada por el proveedor
            Location loc = handle.getLastKnownLocation(provider);
            latitudUser=loc.getLatitude();
            longitudUser=loc.getLongitude();
            setSharedPreferences();

        } catch (Exception e) {
            Log.i(TAG, "Exception while fetch GPS at: " + e.getMessage());
        }
    }
    // Guarda la lititud y longitud del usuario
    public void setSharedPreferences(){
        SharedPreferences.Editor editor = getSharedPreferences("preferenciasBusqueda", MODE_PRIVATE).edit();
        editor.putString("latitudUser",String.valueOf(latitudUser));
        editor.putString("longitudUser",String.valueOf(longitudUser));
        editor.commit();
    }

    @Override
    public void onLocationChanged(Location location) {  }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {  }

    @Override
    public void onProviderEnabled(String provider) {  }

    @Override
    public void onProviderDisabled(String provider) {  }

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
                        responderBusquedaListaCajeros("Todas", "favoritos");
                    }

        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void responderBusquedaMapaCajeros(String entidadBancariaString, String entidadBancariaUsuario) {
        Intent Intent = new Intent(getApplicationContext(), MapaActivity.class);
        if(entidadBancariaString.equals("Todas"))
            Intent.putExtra("cuantosCajeros", "todosCajeros");
        else
            Intent.putExtra("cuantosCajeros", entidadBancariaString);
        Intent.putExtra("entidadBancariaUsuario", entidadBancariaUsuario);
        Intent.putExtra("latitudUser", latitudUser);
        Intent.putExtra("longitudUser", longitudUser);
        startActivity(Intent);
    }

    @Override
    public void responderBusquedaListaCajeros(String entidadBancariaString, String orden) {
        Intent Intent = new Intent(getApplicationContext(), CajeroListActivity.class);
        Intent.putExtra("entidadBancariaString", entidadBancariaString);
        Intent.putExtra("orden", orden);
        Intent.putExtra("latitudUser", latitudUser);
        Intent.putExtra("longitudUser", longitudUser);
        guardarConfiguracion(entidadBancariaString, orden);

        startActivity(Intent);
    }

    //guardar configuración aplicación Android usando SharedPreferences
    public void guardarConfiguracion(String entidadBancariaString, String orden) {
        SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("entidadBancariaString", entidadBancariaString);
        editor.putString("orden", orden);
        editor.commit();
    }
}
