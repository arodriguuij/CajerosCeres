package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.EntidadBancaria;

import java.util.ArrayList;
import java.util.List;

public class CajeroListActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private DataBaseHelperCajeros dbhelper;
    private DataBaseHelperEntidadesBancarias dbhelperEB;
    private List<Cajero> cajerosArray;
    private Boolean libras=false;
    private String entidadBancariaUsuario;
    private String entidadBancariaSeleccion;
    private String orden;
    private EntidadBancaria e;
    private Double comision;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_list);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity_list);
        toolbar.setTitle("Lista de cajeros");
        setSupportActionBar(toolbar);

        /*if (findViewById(R.id.cajero_detail_container) != null) {
            mTwoPane = true;
        }*/
        View recyclerView = findViewById(R.id.cajero_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        libras = sharedPref.getBoolean(PrefFragment.KEY_PREF_MONEDA_LIBRAS, false);

        cargarConfiguracion();
    }


    //cargar configuración aplicación Android usando SharedPreferences
    public void cargarConfiguracion(){
        SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", Context.MODE_PRIVATE);
        entidadBancariaSeleccion = prefs.getString("entidadBancariaUsuarioString", "");
        entidadBancariaUsuario = prefs.getString("entidadBancariaString", "");
        orden = prefs.getString("orden", "");
/*
        imageButtonFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Obtenemos el nombre de origen que el usuario indicó
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // Entindad bancaría de los cajeros a mostrar
        entidadBancariaSeleccion = (String) extras.get("entidadBancariaString");
        // Entindad bancaría del usuario para constrastar las comisiones
        entidadBancariaUsuario = (String) extras.get("entidadBancariaUsuarioString");
        // Tipo de orden de la lista
        orden = (String) extras.getString("orden");

        getEntidadBancariaUsuario();

        switch (entidadBancariaSeleccion){
            case "Todas":
                recuperarTodosCajeros();
                break;
            default:
                recuperarCajerosEntidadBancaria();
                break;
        }
        if(orden.equals("ranking")) {
            burbuja();
        }
        if(orden.equals("favoritos")) {
            favoritos();
        }
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(cajerosArray));
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<Cajero> mValues;
        public SimpleItemRecyclerViewAdapter(List<Cajero> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cajero_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            // Entidad bancaria lista
            holder.mEntidadBancariaView.setText("Cajero "+mValues.get(position).getEntidadBancaria());

            getEntidadBancariaUsuario();
            getComisionEntidadBancaria(mValues.get(position).getEntidadBancaria());

            if(libras)
                holder.mcomisionView.setText(comision+"£");
            else
                holder.mcomisionView.setText(comision+"€");
/*
            if(holder.mItem.isFav()==1)
                holder.mBtnBotonMasImagen.setImageAlpha(R.mipmap.star_on);
            else
                holder.mBtnBotonMasImagen.setImageAlpha(R.mipmap.star_off);

            holder.mBtnToggle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(holder.mItem.isFav()==0){ //no Fav
                        holder.mItem.setFav(1);
                        holder.mBtnToggle.setChecked(true);
                        Toast.makeText(getBaseContext(),"Cajero "+holder.mItem.getEntidadBancaria()+" añadido a favoritos", Toast.LENGTH_LONG).show();
                    }
                    else{
                        holder.mItem.setFav(0);
                        holder.mBtnToggle.setChecked(false);
                        Toast.makeText(getBaseContext(),"Cajero "+holder.mItem.getEntidadBancaria()+" eliminado de favoritos", Toast.LENGTH_LONG).show();
                    }
                }
            });
*/

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CajeroDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()+1));
                        CajeroDetailFragment fragment = new CajeroDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.cajero_detail_container, fragment).commit();
                    } else {*/
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CajeroDetailActivity.class);
                        intent.putExtra(CajeroDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()+1));
                        intent.putExtra("id",holder.mItem.getId());
                        intent.putExtra("entidadBancaria",holder.mItem.getEntidadBancaria());
                        intent.putExtra("uriFotoCajero",holder.mItem.getUriFotoCajero());
                        intent.putExtra("longitud",holder.mItem.getLongitud());
                        intent.putExtra("latitud",holder.mItem.getLatitud());
                        intent.putExtra("fav",holder.mItem.isFav());
                        intent.putExtra("entidadBancariaSeleccion",entidadBancariaSeleccion);
                        intent.putExtra("entidadBancariaUsuario",entidadBancariaUsuario);
                        intent.putExtra("orden",orden);
                        context.startActivity(intent);
                    //}
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mEntidadBancariaView;
            public final TextView mcomisionView;
            //public final ImageButton mImageButtonFav;
            public Cajero mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mEntidadBancariaView = (TextView) view.findViewById(R.id.nombreEntidad);
                mcomisionView = (TextView) view.findViewById(R.id.comision);
                //mImageButtonFav = (ImageButton) findViewById(R.id.imageButtonFav);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mEntidadBancariaView.getText() + "'";
            }
        }
    }

    public  void burbuja(){
        int i, j;
        Cajero aux = new Cajero();
        for(i=0;i<cajerosArray.size()-1;i++)
            for(j=0;j<cajerosArray.size()-1;j++)
                if(getComisionEntidadBancaria(cajerosArray.get(j+1).getEntidadBancaria())<=getComisionEntidadBancaria(cajerosArray.get(j).getEntidadBancaria())){
                    asignarCajero(aux,cajerosArray.get(j+1));
                    asignarCajero(cajerosArray.get(j+1),cajerosArray.get(j));
                    asignarCajero(cajerosArray.get(j),aux);
                }
    }

    public void favoritos(){
        cajerosArray = new ArrayList<Cajero>();
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        try (Cursor cur = dbhelper.getCursorCajero()){
            while(cur.moveToNext()){
                Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                        ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                if(c.isFav()==1)
                    cajerosArray.add(c);
            }
            cur.close();
        }
        dbhelper.close();
    }

    public void asignarCajero(Cajero aux,Cajero aux2){
        aux.setId(aux2.getId());
        aux.setEntidadBancaria(aux2.getEntidadBancaria());
        aux.setUriFotoCajero(aux2.getUriFotoCajero());
        aux.setDireccion(aux2.getDireccion());
        aux.setLatitud(aux2.getLatitud());
        aux.setLongitud(aux2.getLongitud());
        aux.setFav(aux2.isFav());
    }

    private void recuperarTodosCajeros(){
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        cajerosArray = new ArrayList<Cajero>();
        try (Cursor cur = dbhelper.getCursorCajero()){
            while(cur.moveToNext()){
                Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                        ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                cajerosArray.add(c);
            }
            cur.close();
        }
        dbhelper.close();
    }

    private void recuperarCajerosEntidadBancaria(){
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        cajerosArray = new ArrayList<Cajero>();
        try (Cursor cur = dbhelper.getCursorCajero()){
            while(cur.moveToNext()){
                if(cur.getString(1).equals(entidadBancariaSeleccion)){
                    Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                            ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                    cajerosArray.add(c);
                }
            }
            cur.close();
        }
        dbhelper.close();
    }

    private double getComisionEntidadBancaria(String entidadBancariaLista){
        switch(entidadBancariaLista){
            case "BancoPopular":        comision=e.getComisionBancaPueyo();         break;
            case "BancaPueyo":          comision=e.getComisionBancoPopular();       break;
            case "Bankinter":           comision=e.getComisionBankinter();          break;
            case "BBVA":                comision=e.getComisionBBVA();               break;
            case "Caixa":               comision=e.getComisionCaixa();              break;
            case "CaixaGeral":          comision=e.getComisionCaixaGeral();         break;
            case "CajaAlmendralejo":    comision=e.getComisionCajaAlmendralejo();   break;
            case "CajaBadajoz":         comision=e.getComisionCajaBadajoz();        break;
            case "CajaDuero":           comision=e.getComisionCajaDuero();          break;
            case "CajaExtremadura":     comision=e.getComisionCajaExtremadura();    break;
            case "CajaRural":           comision=e.getComisionCajaRural();          break;
            case "DeutscheBank":        comision=e.getComisionDeutscheBank();       break;
            case "Liberban":            comision=e.getComisionLiberbank();          break;
            case "Popular":             comision=e.getComisionPopular();            break;
            case "Sabadell":            comision=e.getComisionSabadell();           break;
            case "Santander":           comision=e.getComisionSantander();          break;
        }
        return comision;
    }

    private EntidadBancaria getEntidadBancariaUsuario(){
        dbhelperEB = new DataBaseHelperEntidadesBancarias(getBaseContext());
        try (Cursor cur = dbhelperEB.getCursorEntidadBancaria()){
            while(cur.moveToNext()){
                if(cur.getString(1).equals(entidadBancariaUsuario))
                    e = new EntidadBancaria(cur.getInt(0),cur.getString(1),cur.getDouble(2)
                            ,cur.getDouble(3),cur.getDouble(4),cur.getDouble(5),cur.getDouble(6),cur.getDouble(7)
                            ,cur.getDouble(8),cur.getDouble(9),cur.getDouble(10),cur.getDouble(11),cur.getDouble(12),cur.getDouble(13)
                            ,cur.getDouble(14),cur.getDouble(15),cur.getDouble(16),cur.getDouble(17));
            }
            cur.close();
        }
        dbhelperEB.close();
        return e;
    }
}