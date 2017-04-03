package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class BusquedaFragment extends Fragment {
    private Button buttonMapa;
    private Button buttonDistancia;
    private Button buttonComision;
    private Button buttonFavoritos;
    private Interfaz comunicacion;
    private Spinner entidadBancaria;
    private String entidadBancariaString;
    private String[][] entidad = { { "0", "Todas" }, { "1", "BancoPopular" }, { "2", "BancaPueyo" }, { "3", "Bankinter" }, { "4", "BBVA" },
            { "5", "Caixa" }, { "6", "CaixaGeral" }, { "7", "CajaAlmendralejo" }, { "8", "CajaBadajoz" },
            { "9", "CajaDuero" }, { "10", "CajaExtremadura" }, { "11", "CajaRural" }, { "12", "DeutscheBank" },
            { "13", "Liberbank" }, { "14", "Popular" }, { "15", "Sabadell" }, { "16", "Santander" },};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        buttonDistancia = (Button) view.findViewById(R.id.buttonDistancia);
        buttonMapa = (Button) view.findViewById(R.id.buttonMapaCajeros);
        buttonComision = (Button) view.findViewById(R.id.buttonComision);
        buttonFavoritos = (Button) view.findViewById(R.id.buttonFavoritos);
        entidadBancaria = (Spinner) view.findViewById(R.id.spinner);
        // Nos dá la actividad asociada a este fragmento
        comunicacion=(Interfaz) getActivity();


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        for (int i = 0; i < entidad.length; i++)
            adapter.add(entidad[i][1]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entidadBancaria.setAdapter(adapter);
        entidadBancaria.setOnItemSelectedListener(selectListener1);
        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonMapa.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaMapaCajeros(entidadBancariaString);
            }
        });
        buttonDistancia.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString,"distancia");
            }
        });
        buttonComision.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString,"comision");
            }
        });
        buttonFavoritos.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString,"favoritos");
            }
        });
    }

    private AdapterView.OnItemSelectedListener selectListener1 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView parent, View v, int position, long id) {
            int pos = entidadBancaria.getSelectedItemPosition();
            entidadBancariaString = entidad[pos][1];
        }
        public void onNothingSelected(AdapterView arg0) {}
    };
}
