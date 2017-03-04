package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ActualizarFragment extends Fragment {
    EditText editTextNuevoNombreActualizar;
    EditText editTextNuevaContraseñaActualizar;
    Button buttonActualizarActualizar;
    String nombreID;
    String nuevoNombreString;
    String nuevaContraseñaString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualizar, container, false);
        Bundle bundle = this.getArguments();
        nombreID = bundle.getString("nombre");
        editTextNuevoNombreActualizar = (EditText) view.findViewById(R.id.editTextNuevoNombreActualizar);
        editTextNuevaContraseñaActualizar = (EditText)view.findViewById(R.id.editTextNuevaContraseñaActualizar);
        buttonActualizarActualizar = (Button) view.findViewById(R.id.buttonActualizarActualizar);
        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        buttonActualizarActualizar.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                nuevoNombreString = editTextNuevoNombreActualizar.getText().toString();
                nuevaContraseñaString = editTextNuevaContraseñaActualizar.getText().toString();
            }
        });
    }
}
