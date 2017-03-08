package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

// Clave de API 1	5 mar. 2017	Ninguna	AIzaSyAa-XMZqW0X2FGWOeAZbCnnkj2rYF9uunI

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DataBaseHelperCajeros dbhelper;
    private GoogleMap mapa;
    private double longitud;
    private double latitud;
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
        switch (cuantosCajeros){
            case "todosCajeros":
                listaCajeros = new ArrayList<Cajero>();
                try (Cursor cur = dbhelper.getCursorCajero()){
                    while(cur.moveToNext()){
                        Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                                ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
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
                try (Cursor cur = dbhelper.getCursorCajero()){
                    while(cur.moveToNext()){
                        Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                                ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                        listaCajeros.add(c);
                    }
                    cur.close();
                }
                int i=0;
                while(i<listaCajeros.size()){
                    if(listaCajeros.get(i).getEntidadBancaria().equals(cuantosCajeros)){
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
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        switch (cuantosCajeros){
            case "todosCajeros":
                int i=0;
                while(i<listaCajeros.size()){
                    getComisionEntidadBancaria(listaCajeros.get(i).getEntidadBancaria());
                    LatLng cajero = new LatLng(listaCajeros.get(i).getLatitud(), listaCajeros.get(i).getLongitud());
                    mapa.addMarker(markerOptions.position(cajero).title(listaCajeros.get(i).getEntidadBancaria())
                            .snippet(listaCajeros.get(i).getUriFotoCajero()).icon(icon));
                    i++;
                }
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                break;
            case "unCajero":
                getComisionEntidadBancaria(entidadBancaria);
                LatLng cajero = new LatLng(latitud, longitud);
                mapa.addMarker(markerOptions.position(cajero).title(entidadBancaria).snippet(uriFotoCajero).icon(icon));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cajero, 15));
                break;
            default:
                int j=0;
                while(j<listaCajerosEntidad.size()){
                    getComisionEntidadBancaria(listaCajerosEntidad.get(j).getEntidadBancaria());
                    LatLng cajeroEntidad = new LatLng(listaCajerosEntidad.get(j).getLatitud(), listaCajerosEntidad.get(j).getLongitud());
                        mapa.addMarker(markerOptions.position(cajeroEntidad).title(listaCajerosEntidad.get(j).getEntidadBancaria())
                                .snippet(listaCajerosEntidad.get(j).getUriFotoCajero()).icon(icon));
                    j++;
                }
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadCaceres, 13));
                break;
        }
        dbhelper.close();

        //Drawable circleDrawable = getResources().getDrawable(R.drawable.circle_shape);
        //BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        //mapa.addMarker(markerOptions.position(cajero).title(entidadBancaria).snippet(uriFotoCajero).icon(markerIcon));
        //mapa.addMarker(markerOptions.position(cajero).title(entidadBancaria).snippet(uriFotoCajero));

        //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cajero, 15));
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth()/6, drawable.getIntrinsicHeight()/6, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth()/6, drawable.getIntrinsicHeight()/6);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getComisionEntidadBancaria(String entidadBancariaLista){
        switch(entidadBancariaLista){
            case "BancoPopular":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);
                break;
            case "BancaPueyo":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.banca_puello);
                break;
            case "Bankinter":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.bankinter);
                break;
            case "BBVA":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.bbva);
                break;
            case "Caixa":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa);
                break;
            case "CaixaGeral":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caixa_geral);
                break;
            case "CajaAlmendralejo":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_almendralejo);
                break;
            case "CajaBadajoz":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_badajoz);
                break;
            case "CajaDuero":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_duero);
                break;
            case "CajaExtremadura":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_extremadura);
                break;
            case "CajaRural":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.caja_rural);
                break;
            case "DeutscheBank":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.deutschebank);
                break;
            case "Liberban":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.liberbank);
                break;
            case "Popular":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.popular);
                break;
            case "Sabadell":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.sabadell);
                break;
            case "Santander":
                icon = BitmapDescriptorFactory.fromResource(R.mipmap.santander);
                break;
        }
    }

}