package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroFragment extends Fragment {
    Button buttonRegistrarseRegistro;
    EditText editTextNombreUsuarioRegistro;
    EditText editTextContraseñaRegistro;
    EditText editTextConfirmarContraseñaRegistro;
    String nombreUsuarioString;
    String contraseñaString;
    String confirmarContraseñaString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);
        editTextNombreUsuarioRegistro = (EditText) view.findViewById(R.id.editTextNombreUsuarioRegistro);
        editTextContraseñaRegistro = (EditText) view.findViewById(R.id.editTextContraseñaRegistro);
        editTextConfirmarContraseñaRegistro = (EditText) view.findViewById(R.id.editTextConfirmarContraseñaRegistro);
        buttonRegistrarseRegistro = (Button) view.findViewById(R.id.buttonRegistrarseRegistro);
        return view;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        buttonRegistrarseRegistro.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                nombreUsuarioString = editTextNombreUsuarioRegistro.getText().toString();
                contraseñaString = editTextContraseñaRegistro.getText().toString();
                confirmarContraseñaString = editTextConfirmarContraseñaRegistro.getText().toString();

                //Si los campos no son nulos:
                if((!nombreUsuarioString.equals("")) && (!contraseñaString.equals(""))){
                    //Contraseña verificada:
                    if(contraseñaString.equals(confirmarContraseñaString)){

                    }else{
                        Context context = getContext();
                        CharSequence text = "Verifica correctamente la contraseña";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }else{
                    Context context = getContext();
                    CharSequence text = "Hay algún campo nulo";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
}
