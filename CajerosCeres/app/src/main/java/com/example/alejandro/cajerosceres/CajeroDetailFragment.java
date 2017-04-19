package com.example.alejandro.cajerosceres;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CajeroDetailFragment extends Fragment implements Callback{
    public static final String ARG_ITEM_ID = "item_id";
    private Cajero cajero;
    private List<Cajero> listaCajeros;
    private DataBaseHelperCajeros dbhelper;
    private View rootView;
    private ImageView logo;
    private ProgressBar progressBar;
    private ImageButton mImageButtonFav;
    private Location loc;
    private String direccion;

    public CajeroDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaCajeros = new ArrayList<Cajero>();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Obtener los Cajeros de la base de datos y meterlos en una lista
            dbhelper = new DataBaseHelperCajeros(getActivity().getBaseContext());
            try (Cursor cur = dbhelper.getCursorCajero()){
                while(cur.moveToNext()){
                    Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                            ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                    listaCajeros.add( c);
                }
                cur.close();
            }
            dbhelper.close();

            cajero = listaCajeros.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID))-1);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cajero.getEntidadBancaria());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cajero_detail, container, false);

        if (cajero != null) {
            ((TextView) rootView.findViewById(R.id.entidadBancaria)).setText(cajero.getEntidadBancaria());
            logo = (ImageView) rootView.findViewById(R.id.imageView2);
            mImageButtonFav = (ImageButton) rootView.findViewById(R.id.imageButtonFavDetail);

            final Bitmap bmpOn = BitmapFactory.decodeResource(getResources(), R.mipmap.star_on);
            final Bitmap bmpOff = BitmapFactory.decodeResource(getResources(), R.mipmap.star_off);

            if(cajero.isFav()==1)
                mImageButtonFav.setImageBitmap(bmpOn);
            else
                mImageButtonFav.setImageBitmap(bmpOff);

            mImageButtonFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dbhelper = new DataBaseHelperCajeros(getActivity().getBaseContext());

                    if(cajero.isFav()==0){ //no Fav
                        cajero.setFav(1);
                        dbhelper.setFavoritoCajero(cajero.getId(), 1);
                        mImageButtonFav.setImageBitmap(bmpOn);
                        Toast.makeText(getContext(),"Cajero "+cajero.getEntidadBancaria()+
                                " añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        cajero.setFav(0);
                        dbhelper.setFavoritoCajero(cajero.getId(), 0);
                        mImageButtonFav.setImageBitmap(bmpOff);
                        Toast.makeText(getContext(),"Cajero "+cajero.getEntidadBancaria()+
                                " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                    dbhelper.close();
                }
            });

            loc = new Location("");
            loc.setLatitude(cajero.getLatitud());
            loc.setLongitude(cajero.getLongitud());
            setLocation(loc);
            ((TextView) rootView.findViewById(R.id.textViewDireccion)).setText(direccion);

            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
            loadImage();
        }
        return rootView;
    }

    // Obtener la dirección de la calle a partir de la latitud y la longitud
    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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

    // Carga una progressBar mientras la imagen aún no se ha descargado y mostrado
    private synchronized void loadImage() {
        Picasso.with(getContext()).load(cajero.getUriFotoCajero()).
                into(logo, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.VISIBLE);
            }
            @Override
            public void onError() {
                progressBar.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onSuccess(){
        // hide the loader and show the imageview
        progressBar.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(){
        // hide the loader and show the imageview which shows the error icon already
        progressBar.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
    }
}