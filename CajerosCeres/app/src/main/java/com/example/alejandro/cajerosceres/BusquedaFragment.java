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

import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;

public class BusquedaFragment extends Fragment {
    private Button buttonMapaCajeros;
    private Button buttonListaCajeros;
    private Button buttonActualizarCajeros;
    private Interfaz comunicacion;
    private DataBaseHelperCajeros dbHelper;
    private Spinner entidadBancaria;
    private String entidadBancariaString;
    private String[][] entidad = { { "0", "Todas" }, { "1", "BancoPopular" }, { "2", "BancaPueyo" }, { "3", "Bankinter" }, { "4", "BBVA" },
            { "5", "Caixa" }, { "6", "CaixaGeral" }, { "7", "CajaAlmendralejo" }, { "8", "CajaBadajoz" },
            { "9", "CajaDuero" }, { "10", "CajaExtremadura" }, { "11", "CajaRural" }, { "12", "DeutscheBank" },
            { "13", "Liberbank" }, { "14", "Popular" }, { "15", "Sabadell" }, { "16", "Santander" },};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        buttonListaCajeros = (Button) view.findViewById(R.id.buttonListaCajeros);
        buttonMapaCajeros = (Button) view.findViewById(R.id.buttonMapaCajeros);
        buttonActualizarCajeros = (Button) view.findViewById(R.id.buttonActualizarCajeros);
        entidadBancaria = (Spinner) view.findViewById(R.id.spinner);

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

        // Nos dá la actividad asociada a este fragmento
        comunicacion=(Interfaz) getActivity();
        buttonMapaCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaMapaCajeros(entidadBancariaString);
            }
        });
        buttonListaCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                comunicacion.responderBusquedaListaCajeros(entidadBancariaString);
            }
        });
        buttonActualizarCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                dbHelper = new DataBaseHelperCajeros(getActivity().getBaseContext());
                dbHelper.onRestar();
                dbHelper.close();
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
/*
public class intro extends AppCompatActivity {
    private static final long SPLASH_TIME=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent=new Intent().setClass(intro.this,main.class);
                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,SPLASH_TIME);
    }
}

 */
}
