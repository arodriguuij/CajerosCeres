package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

// Clave de API 1	5 mar. 2017	Ninguna	AIzaSyAa-XMZqW0X2FGWOeAZbCnnkj2rYF9uunI

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    private LocationManager mLocMgr;
    private double latitudYo;
    private double longitudYo;
/*
    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 10; // 10 metros
    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 60 * 1; // 1 minuto
*/
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

		/* Use the LocationManager class to obtain GPS locations */
/*        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocListener.setMapaActivity(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            System.out.println("---------------------------------------- No se tienen permisos necesarios!, se requieren.----------------------------------------");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }else {
            // 10 metros, 100*60*1 1 minuto
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 100*60*1, mlocListener);
        }*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng ciudadCaceres = new LatLng(39.4752169, -6.372337600000037);
        //LatLng yo = new LatLng(latitudYo, longitudYo);
        //mapa.addMarker(new MarkerOptions().position(yo).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

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

    public class MyLocationListener implements android.location.LocationListener {
        MapaActivity mapaActivity;

        public MapaActivity getMainActivity() {
            return mapaActivity;
        }

        public void setMapaActivity(MapaActivity mapaActivity) {
            this.mapaActivity = mapaActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas debido a la deteccin de un cambio de ubicacion
            latitudYo=loc.getLatitude();
            longitudYo=loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este metodo se ejecuta cada vez que se detecta un cambio en el status del proveedor de localizacion (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Tempralmente no disponible pero se espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }
}

/*
        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            System.out.println("---------------------------------------- No se tienen permisos necesarios!, se requieren.----------------------------------------");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }else{
            System.out.println("---------------------------------------- Permisos necesarios OK!. ----------------------------------------");
            mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES,
                    (android.location.LocationListener) locListener, Looper.getMainLooper());
        }
*/
/*
    public LocationListener locListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            System.out.println("---------------------------------------- Lat " +
                    location.getLatitude() + " Long " + location.getLongitude()+"" +
                    " ----------------------------------------");
            latitudYo=location.getLatitude();
            longitudYo=location.getLongitude();
        }
    };

*/