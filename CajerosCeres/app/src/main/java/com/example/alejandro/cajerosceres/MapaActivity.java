package com.example.alejandro.cajerosceres;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
// Clave de API 1	5 mar. 2017	Ninguna	AIzaSyAa-XMZqW0X2FGWOeAZbCnnkj2rYF9uunI

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private DataBaseHelperCajeros dbhelper;
    private GoogleMap mapa;
    private double longitud;
    private double latitud;
    private double longitudUser;
    private double latitudUser;
    private String entidadBancaria;
    private String direccion;
    private List<Cajero> listaCajeros;
    private List<Cajero> listaCajerosEntidad;
    private String cuantosCajeros;
    private BitmapDescriptor icon;
    private static final String TAG = "LocationActivity";
    private LocationManager mLocMgr;
    private Bundle extras;
    private MarkerOptions markerOptions;
    private LocationManager handle; //Gestor del servicio de localización
    private String provider;
    private LatLng user;
    private Boolean permisos=false;
    private float distancia;
    private Location loc;

    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 10; // 10 metros
    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 60 * 1; // 1 minuto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        obtenerLocalizacion();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Obtenemos las coordenadas del Cajero
        Intent intent = getIntent();
        extras = intent.getExtras();
        obtenerCajeros();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        markerOptions = new MarkerOptions();
        LatLng ciudadCaceres = new LatLng(39.4752169, -6.372337600000037);
        cuantosCajeros = (String) extras.get("cuantosCajeros");


        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        switch (cuantosCajeros) {
            case "todosCajeros":
                int i = 0;
                while (i < listaCajeros.size()) {
                    getComisionEntidadBancaria(listaCajeros.get(i).getEntidadBancaria());
                    LatLng cajero = new LatLng(listaCajeros.get(i).getLatitud(), listaCajeros.get(i).getLongitud());
                    distancia=CajeroListActivity.calcularDistancia(listaCajeros.get(i).getLatitud(),listaCajeros.get(i).getLongitud(),
                            latitudUser,longitudUser);

                    mapa.addMarker(markerOptions.position(cajero).title(listaCajeros.get(i).getEntidadBancaria())
                            .snippet("Distancia: "+distancia+" m").icon(icon));
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                    i++;
                }
                break;
            case "unCajero":
                getComisionEntidadBancaria(entidadBancaria);
                LatLng cajero = new LatLng(latitud, longitud);
                distancia=CajeroListActivity.calcularDistancia(latitud, longitud, latitudUser, longitudUser);
                loc = new Location("");
                loc.setLatitude(latitud);
                loc.setLongitude(longitud);
                setLocation(loc);
                mapa.addMarker(markerOptions.position(cajero).title(entidadBancaria)
                        .snippet("Distancia: "+distancia+" m --- Direccion: "+direccion).icon(icon));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cajero, 15));
                break;
            default:
                int j = 0;
                while (j < listaCajerosEntidad.size()) {
                    getComisionEntidadBancaria(listaCajerosEntidad.get(j).getEntidadBancaria());
                    LatLng cajeroEntidad = new LatLng(listaCajerosEntidad.get(j).getLatitud(), listaCajerosEntidad.get(j).getLongitud());
                    distancia=CajeroListActivity.calcularDistancia(listaCajeros.get(j).getLatitud(),listaCajeros.get(j).getLongitud(),
                            latitudUser,longitudUser);
                    mapa.addMarker(markerOptions.position(cajeroEntidad).title(listaCajerosEntidad.get(j).getEntidadBancaria())
                            .snippet("Distancia: "+distancia+" m").icon(icon));
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                    j++;
                }
                break;
        }
        if(permisos)
            if(user != null)
                mapa.addMarker(markerOptions.position(user).title("user") .snippet("cajeroooooooooooo").icon(BitmapDescriptorFactory.fromResource(R.mipmap.estoy_aqui)));

        dbhelper.close();
    }

    private void getComisionEntidadBancaria(String entidadBancariaLista) {
        switch (entidadBancariaLista) {
            case "BancoPopular":    icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);              break;
            case "BancaPueyo":      icon = BitmapDescriptorFactory.fromResource(R.mipmap.banca_puello);         break;
            case "Bankinter":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.bankinter);            break;
            case "BBVA":            icon = BitmapDescriptorFactory.fromResource(R.mipmap.bbva);                 break;
            case "Caixa":           icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa);                break;
            case "CaixaGeral":      icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa_geral);          break;
            case "CajaAlmendralejo":icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_almendralejo);    break;
            case "CajaBadajoz":     icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_badajoz);         break;
            case "CajaDuero":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_duero);           break;
            case "CajaExtremadura": icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_extremadura);     break;
            case "CajaRural":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_rural);           break;
            case "DeutscheBank":    icon = BitmapDescriptorFactory.fromResource(R.mipmap.deutschebank);         break;
            case "Liberban":        icon = BitmapDescriptorFactory.fromResource(R.mipmap.liberbank);            break;
            case "Popular":         icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);              break;
            case "Sabadell":        icon = BitmapDescriptorFactory.fromResource(R.mipmap.sabadell);             break;
            case "Santander":       icon = BitmapDescriptorFactory.fromResource(R.mipmap.santander);            break;
        }
    }

    public void obtenerCajeros() {
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

    public void obtenerLocalizacion(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e(TAG, "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }
        permisos=true;
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
                user = new LatLng(loc.getLatitude(),loc.getLongitude());
                //Toast.makeText(getBaseContext()," latitud:" + latitudUser + ", longitd:" + longitudUser, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i(TAG, "Exception while fetch GPS at: " + e.getMessage());
            }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getBaseContext()," lat:" + latitudUser + ", lon:" + longitudUser, Toast.LENGTH_LONG).show();
        //mapa.addMarker(markerOptions.position(user).title("user").icon(BitmapDescriptorFactory.fromResource(R.mipmap.star_on)));
        Log.i(TAG, "Lat " + location.getLatitude() + " Long " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {  }

    @Override
    public void onProviderEnabled(String provider) {  }

    @Override
    public void onProviderDisabled(String provider) {  }

    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    direccion=address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}