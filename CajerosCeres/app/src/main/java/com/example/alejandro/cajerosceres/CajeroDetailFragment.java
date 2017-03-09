package com.example.alejandro.cajerosceres;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;

import java.util.ArrayList;
import java.util.List;

public class CajeroDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Cajero cajero;
    private List<Cajero> listaCajeros;
    private DataBaseHelperCajeros dbhelper;

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
        View rootView = inflater.inflate(R.layout.cajero_detail, container, false);

        FloatingActionButton favButton = (FloatingActionButton) rootView.findViewById(R.id.favButton);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbhelper = new DataBaseHelperCajeros(getActivity().getBaseContext());
                if(cajero.isFav()==0) {
                    dbhelper.setFavoritoCajero(cajero.getId(), 1);
                    Toast.makeText(getActivity(),"Añadido a favoritos!",Toast.LENGTH_SHORT).show();
                }
                else {
                    dbhelper.setFavoritoCajero(cajero.getId(), 0);
                    Toast.makeText(getActivity(),"Eliminado de favoritos!",Toast.LENGTH_SHORT).show();
                }
                dbhelper.close();
            }
        });

        if (cajero != null) {
            ((TextView) rootView.findViewById(R.id.entidadBancaria)).setText(cajero.getEntidadBancaria());
            ((TextView) rootView.findViewById(R.id.uriFotoCajero)).setText(cajero.getUriFotoCajero());
            ((TextView) rootView.findViewById(R.id.longitud)).setText(String.valueOf(cajero.getLongitud()));
            ((TextView) rootView.findViewById(R.id.latitud)).setText(String.valueOf(cajero.getLatitud()));
            if(cajero.isFav()==0)
                ((TextView) rootView.findViewById(R.id.fav)).setText("No");
            else
                ((TextView) rootView.findViewById(R.id.fav)).setText("Sí");


            //ImageView logo= (ImageView) rootView.findViewById(R.id.imageView2);
            //logo.setImageResource(R.drawable.ic_logo);
            //Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(R.id.imageView2);
        }
        return rootView;
    }
}