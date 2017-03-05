package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BusquedaFragment extends Fragment {
    private Button buttonMapaCajeros;
    private Button buttonListaCajeros;
    Interfaz comunicacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        buttonListaCajeros = (Button) view.findViewById(R.id.buttonListaCajeros);
        buttonMapaCajeros = (Button) view.findViewById(R.id.buttonMapaCajeros);

        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Nos dá la actividad asociada a este fragmento
        comunicacion=(Interfaz) getActivity();
        buttonMapaCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaMapaCajeros();
            }
        });
        buttonListaCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros();
            }
        });
    }


}
