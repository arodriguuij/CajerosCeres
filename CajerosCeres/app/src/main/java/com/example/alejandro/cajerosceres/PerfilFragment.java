package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PerfilFragment extends Fragment{
    TextView textViewPerfilNombre;
    TextView textViewPerfilPassword;
    String nombreUsuarioString;
    Button buttonModificarDatosPerfil;
    Button buttonFavoritosPerfil;
    Button buttonBorrarPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        Bundle bundle = this.getArguments();
        String namePaquete = bundle.getString("nombre");
        String passwordPaquete = bundle.getString("password");

        textViewPerfilNombre = (TextView) view.findViewById(R.id.textViewPerfilNombre);
        textViewPerfilNombre.setText(namePaquete);
        textViewPerfilPassword = (TextView)view.findViewById(R.id.textViewPerfilPassword);
        textViewPerfilPassword.setText(passwordPaquete);
        buttonModificarDatosPerfil = (Button) view.findViewById(R.id.buttonModificarDatosPerfil);
        buttonFavoritosPerfil = (Button) view.findViewById(R.id.buttonFavoritosPerfil);
        buttonBorrarPerfil = (Button) view.findViewById(R.id.buttonBorrarPerfil);

        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        nombreUsuarioString = textViewPerfilNombre.getText().toString();

        buttonModificarDatosPerfil.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
            }
        });

        buttonFavoritosPerfil.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
            }
        });

        buttonBorrarPerfil.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
            }
        });
    }
}

