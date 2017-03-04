package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FavoritosFragment extends Fragment {
    Button buttonAccederLogin;
    EditText editTextNombreUsuario;
    EditText editTextContraseña;
    String nombreUsuarioString;
    String contraseñaString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        editTextNombreUsuario = (EditText) view.findViewById(R.id.editTextNombreUsuario);
        editTextContraseña = (EditText) view.findViewById(R.id.editTextContraseña);
        buttonAccederLogin = (Button) view.findViewById(R.id.buttonAccederLogin);
        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonAccederLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                nombreUsuarioString = editTextNombreUsuario.getText().toString();
                contraseñaString = editTextContraseña.getText().toString();
            }
        });
    }
}