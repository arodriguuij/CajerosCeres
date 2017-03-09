package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.util.Log;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

// Clave de API 1	5 mar. 2017	Ninguna	AIzaSyAa-XMZqW0X2FGWOeAZbCnnkj2rYF9uunI

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSION_LOCATION = 1337;
    private DataBaseHelperCajeros dbhelper;
    private GoogleMap mapa;
    private double longitud;
    private double latitud;
    private Marker marcador;
    private double longitudUsuario;
    private double latitudUsuario;
    private String entidadBancaria;
    private String uriFotoCajero;
    private List<Cajero> listaCajeros;
    private List<Cajero> listaCajerosEntidad;
    private String cuantosCajeros;
    private BitmapDescriptor icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //Obtenemos las coordenadas del Cajero
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        dbhelper = new DataBaseHelperCajeros(getBaseContext());

        cuantosCajeros = (String) extras.get("cuantosCajeros");
        switch (cuantosCajeros) {
            case "todosCajeros":
                listaCajeros = new ArrayList<Cajero>();
                try (Cursor cur = dbhelper.getCursorCajero()) {
                    while (cur.moveToNext()) {
                        Cajero c = new Cajero(cur.getInt(0), cur.getString(1), cur.getString(2)
                                , cur.getDouble(3), cur.getDouble(4), cur.getString(5), cur.getInt(6));
                        listaCajeros.add(c);
                    }
                    cur.close();
                }
                break;
            case "unCajero":
                longitud = (double) extras.get("longitud");
                latitud = (double) extras.get("latitud");
                entidadBancaria = (String) extras.get("entidadBancaria");
                uriFotoCajero = (String) extras.get("uriFotoCajero");
                break;
            default:
                listaCajeros = new ArrayList<Cajero>();
                listaCajerosEntidad = new ArrayList<Cajero>();
                try (Cursor cur = dbhelper.getCursorCajero()) {
                    while (cur.moveToNext()) {
                        Cajero c = new Cajero(cur.getInt(0), cur.getString(1), cur.getString(2)
                                , cur.getDouble(3), cur.getDouble(4), cur.getString(5), cur.getInt(6));
                        listaCajeros.add(c);
                    }
                    cur.close();
                }
                int i = 0;
                while (i < listaCajeros.size()) {
                    if (listaCajeros.get(i).getEntidadBancaria().equals(cuantosCajeros)) {
                        listaCajerosEntidad.add(listaCajeros.get(i));
                    }
                    i++;
                }
                break;
        }
        dbhelper.close();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng ciudadCaceres = new LatLng(39.4752169, -6.372337600000037);

        //miUbicacion();

        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        switch (cuantosCajeros) {
            case "todosCajeros":
                int i = 0;
                while (i < listaCajeros.size()) {
                    getComisionEntidadBancaria(listaCajeros.get(i).getEntidadBancaria());
                    LatLng cajero = new LatLng(listaCajeros.get(i).getLatitud(), listaCajeros.get(i).getLongitud());
                    mapa.addMarker(markerOptions.position(cajero).title(listaCajeros.get(i).getEntidadBancaria())
                            .snippet(listaCajeros.get(i).getUriFotoCajero()).icon(icon));
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                    i++;
                }
                break;
            case "unCajero":
                getComisionEntidadBancaria(entidadBancaria);
                LatLng cajero = new LatLng(latitud, longitud);
                mapa.addMarker(markerOptions.position(cajero).title(entidadBancaria).snippet(uriFotoCajero).icon(icon));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cajero, 15));
                break;
            default:
                int j = 0;
                while (j < listaCajerosEntidad.size()) {
                    getComisionEntidadBancaria(listaCajerosEntidad.get(j).getEntidadBancaria());
                    LatLng cajeroEntidad = new LatLng(listaCajerosEntidad.get(j).getLatitud(), listaCajerosEntidad.get(j).getLongitud());
                    mapa.addMarker(markerOptions.position(cajeroEntidad).title(listaCajerosEntidad.get(j).getEntidadBancaria())
                            .snippet(listaCajerosEntidad.get(j).getUriFotoCajero()).icon(icon));
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                    j++;
                }
                break;
        }
        dbhelper.close();

    }



    private void agregarMarcador(double lat, double lng) {
        LatLng ubicacionUsuario = new LatLng(latitudUsuario, longitudUsuario);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 14));
        marcador = mapa.addMarker(new MarkerOptions().position(ubicacionUsuario).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitudUsuario = location.getLatitude();
            longitudUsuario = location.getLongitude();
            agregarMarcador(latitudUsuario, longitudUsuario);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }
    };
/*
    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
            mapa.setMyLocationEnabled(true);
            //mapa.getUiSettings().setMyLocationButtonEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
            // 15000 = cada 15 segundos
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, (android.location.LocationListener) locationListener);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    public boolean checkLocationPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == TAG_CODE_PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permiso concedido


            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
            }
        }
    }

*/
    private void miUbicacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_LOCATION);
        } else {
            //   gps functions.
            mapa.setMyLocationEnabled(true);
            //mapa.getUiSettings().setMyLocationButtonEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
            // 15000 = cada 15 segundos
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, (android.location.LocationListener) locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  gps functionality
        }
    }


    private void getComisionEntidadBancaria(String entidadBancariaLista) {
        switch (entidadBancariaLista) {
            case "BancoPopular":    icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);break;
            case "BancaPueyo":      icon = BitmapDescriptorFactory.fromResource(R.mipmap.banca_puello);break;
            case "Bankinter":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.bankinter);break;
            case "BBVA":            icon = BitmapDescriptorFactory.fromResource(R.mipmap.bbva);break;
            case "Caixa":           icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa);break;
            case "CaixaGeral":      icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa_geral);break;
            case "CajaAlmendralejo":icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_almendralejo);break;
            case "CajaBadajoz":     icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_badajoz);break;
            case "CajaDuero":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_duero);break;
            case "CajaExtremadura": icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_extremadura);break;
            case "CajaRural":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_rural);break;
            case "DeutscheBank":    icon = BitmapDescriptorFactory.fromResource(R.mipmap.deutschebank);break;
            case "Liberban":        icon = BitmapDescriptorFactory.fromResource(R.mipmap.liberbank);break;
            case "Popular":         icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);break;
            case "Sabadell":        icon = BitmapDescriptorFactory.fromResource(R.mipmap.sabadell);break;
            case "Santander":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.santander);break;
        }
    }
}