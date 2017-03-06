package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;

public class BusquedaFragment extends Fragment {
    private Button buttonMapaCajeros;
    private Button buttonListaCajeros;
    private Button buttonActualizarCajeros;
    Interfaz comunicacion;
    DataBaseHelperCajeros dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        dbHelper = new DataBaseHelperCajeros(getActivity().getBaseContext());
        buttonListaCajeros = (Button) view.findViewById(R.id.buttonListaCajeros);
        buttonMapaCajeros = (Button) view.findViewById(R.id.buttonMapaCajeros);
        buttonActualizarCajeros = (Button) view.findViewById(R.id.buttonActualizarCajeros);

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
        buttonActualizarCajeros.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                dbHelper.onRestar();

            }
        });
    }

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
