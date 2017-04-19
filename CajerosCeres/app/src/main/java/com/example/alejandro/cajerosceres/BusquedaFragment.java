package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class BusquedaFragment extends Fragment {
    private Interfaz comunicacion;
    private Spinner entidadBancaria;
    private String entidadBancariaString;
    private ImageButton mImageButtonDistancia;
    private ImageButton mImageButtonMap;
    private ImageButton mImageButtonComision;
    private ImageButton mImageButtonFav;
    private String entidadBancariaUsuario;

    private String[][] entidad = { { "0", "Todas" }, { "1", "BancoPopular" }, { "2", "BancaPueyo" }, { "3", "Bankinter" }, { "4", "BBVA" },
            { "5", "Caixa" }, { "6", "CaixaGeral" }, { "7", "CajaAlmendralejo" }, { "8", "CajaBadajoz" },
            { "9", "CajaDuero" }, { "10", "CajaExtremadura" }, { "11", "CajaRural" }, { "12", "DeutscheBank" },
            { "13", "Liberbank" }, { "14", "Popular" }, { "15", "Sabadell" }, { "16", "Santander" },};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        entidadBancaria = (Spinner) view.findViewById(R.id.spinner);
        mImageButtonDistancia = (ImageButton) view.findViewById(R.id.imageButtonDistancia);
        mImageButtonMap = (ImageButton) view.findViewById(R.id.imageButtonMap);
        mImageButtonComision = (ImageButton) view.findViewById(R.id.imageButtonComision);
        mImageButtonFav = (ImageButton) view.findViewById(R.id.imageButtonFav);

        // Nos dá la actividad asociada a este fragmento
        comunicacion=(Interfaz) getActivity();

        mImageButtonDistancia.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_button_distancia));
        mImageButtonMap.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_button_mapa));
        mImageButtonComision.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_button_comision));
        mImageButtonFav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_button_favorito));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        for (int i = 0; i < entidad.length; i++)
            adapter.add(entidad[i][1]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entidadBancaria.setAdapter(adapter);
        entidadBancaria.setOnItemSelectedListener(selectListener1);

        PreferenceManager.setDefaultValues(getContext(), R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        entidadBancariaUsuario = sharedPref.getString(PrefFragment.KEY_PREF_ENTIDAD_BANCARIA_USUARIO, "BBVA");

        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mImageButtonDistancia.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString,"distancia");
            }
        });

        mImageButtonMap.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaMapaCajeros(entidadBancariaString, entidadBancariaUsuario);
            }
        });
        mImageButtonComision.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString,"comision");
            }
        });
        mImageButtonFav.setOnClickListener(new Button.OnClickListener(){
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