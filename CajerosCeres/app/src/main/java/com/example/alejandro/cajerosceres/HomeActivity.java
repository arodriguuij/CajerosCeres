package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.EntidadBancaria;

public class HomeActivity extends AppCompatActivity {
    private Button buttonAcceder;
    private Button buttonStopServicio;
    private DataBaseHelperEntidadesBancarias dbhelper;
    private boolean servicioON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        inicializarComponentes();
        dbhelper = new DataBaseHelperEntidadesBancarias(getBaseContext());
    }

    public void inicializarComponentes() {
        buttonAcceder = (Button) findViewById(R.id.buttonAcceder);
        buttonStopServicio = (Button) findViewById(R.id.buttonStopServicio);

        buttonAcceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuLateral.class);

                //PreferenceManager.setDefaultValues(getBaseContext(), R.xml.ajustes, false);
                //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                //servicioON = sharedPref.getBoolean(PrefFragment.KEY_PREF_MONEDA_LIBRAS, false);
                //if(servicioON)
                    startService(new Intent(HomeActivity.this, ActualizarService.class));
                //else
                //    stopService(new Intent(HomeActivity.this, ActualizarService.class));

                crearTablaComisiones();
                startActivity(intent);
            }
        });
        buttonStopServicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuLateral.class);

                stopService(new Intent(HomeActivity.this, ActualizarService.class));

                //crearTablaComisiones();
                startActivity(intent);
            }
        });

    }


    private void crearTablaComisiones(){
        try (Cursor cur = dbhelper.getCursorEntidadBancaria()){
            if(cur.getCount()==0){
                EntidadBancaria eBancoPopular = new EntidadBancaria(1,"BancoPopular",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eBancaPueyo = new EntidadBancaria(2,"BancaPueyo",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eBankinter = new EntidadBancaria(3,"Bankinter",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eBBVA = new EntidadBancaria(4,"BBVA",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCaixa = new EntidadBancaria(5,"Caixa",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCaixaGeral = new EntidadBancaria(6,"CaixaGeral",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaAlmendralejo = new EntidadBancaria(7,"CajaAlmendralejo",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaBadajoz = new EntidadBancaria(8,"CajaBadajoz",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaDuero = new EntidadBancaria(9,"CajaDuero",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaExtremadura = new EntidadBancaria(10,"CajaExtremadura",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaRural = new EntidadBancaria(11,"CajaRural",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eDeutscheBank = new EntidadBancaria(12,"DeutscheBank",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eLiberban = new EntidadBancaria(13,"Liberban",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria ePopular = new EntidadBancaria(14,"Popular",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eSabadell = new EntidadBancaria(15,"Sabadell",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eSantander = new EntidadBancaria(16,"Santander",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                dbhelper.importarCajero(eBancoPopular);
                dbhelper.importarCajero(eBancaPueyo);
                dbhelper.importarCajero(eBankinter);
                dbhelper.importarCajero(eBBVA);
                dbhelper.importarCajero(eCaixa);
                dbhelper.importarCajero(eCaixaGeral);
                dbhelper.importarCajero(eCajaAlmendralejo);
                dbhelper.importarCajero(eCajaBadajoz);
                dbhelper.importarCajero(eCajaDuero);
                dbhelper.importarCajero(eCajaExtremadura);
                dbhelper.importarCajero(eCajaRural);
                dbhelper.importarCajero(eDeutscheBank);
                dbhelper.importarCajero(eLiberban);
                dbhelper.importarCajero(ePopular);
                dbhelper.importarCajero(eSabadell);
                dbhelper.importarCajero(eSantander);
                cur.close();
            }
        }
        dbhelper.close();}
}
