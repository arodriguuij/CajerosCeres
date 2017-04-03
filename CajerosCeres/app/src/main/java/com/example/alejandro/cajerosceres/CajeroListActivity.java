package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.EntidadBancaria;

import java.util.ArrayList;
import java.util.List;

public class CajeroListActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private DataBaseHelperEntidadesBancarias dbhelperEB;
    private List<Cajero> cajerosArray;
    private String moneda;
    private String entidadBancariaUsuario;
    private String entidadBancariaSeleccion;
    private String orden;
    private EntidadBancaria e;
    private Double comision;
    private Toolbar toolbar;
    private double longitudUser;
    private double latitudUser;
    private DataBaseHelperCajeros dbhelper;
    private Location locationA;
    private Location locationB;
    private Location locationAaux;
    private Location locationBaux;
    private float distanceAaux;
    private float distanceBaux;
    private float distance;

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
        cargarConfiguracion();

        View recyclerView = findViewById(R.id.cajero_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    //cargar configuración aplicación Android usando SharedPreferences
    public void cargarConfiguracion(){
        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        moneda = sharedPref.getString(PrefFragment.KEY_PREF_MONEDAS, "Euros");
        entidadBancariaUsuario = sharedPref.getString(PrefFragment.KEY_PREF_ENTIDAD_BANCARIA_USUARIO, "BBVA");

        //Obtenemos el nombre de origen que el usuario indicó
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // Entindad bancaría de los cajeros a mostrar
        entidadBancariaSeleccion = (String) extras.get("entidadBancariaString");
        getEntidadBancariaUsuario();

        latitudUser = (Double) extras.get("latitudUser");
        longitudUser = (Double) extras.get("longitudUser");

        // Tipo de orden de la lista
        orden = (String) extras.getString("orden");

        switch (entidadBancariaSeleccion){
            case "Todas":
                recuperarTodosCajeros();
                break;
            default:
                recuperarCajerosEntidadBancaria();
                break;
        }
        if(orden.equals("distancia")) {
            burbujaDistancia();
        }
        if(orden.equals("comision")) {
            burbujaComision();
        }
        if(orden.equals("favoritos")) {
            favoritos();
        }

        /*
        SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", Context.MODE_PRIVATE);
        entidadBancariaSeleccion = prefs.getString("entidadBancariaUsuarioString", "");
        orden = prefs.getString("orden", "");
        */
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
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

            locationA = new Location("point A");
            locationA.setLatitude(holder.mItem.getLatitud());
            locationA.setLongitude(holder.mItem.getLongitud());

            locationB = new Location("point B");
            locationB.setLatitude(latitudUser);
            locationB.setLongitude(longitudUser);

            distance = locationA.distanceTo(locationB);
            holder.mDistancia.setText(Float.toString(distance)+" m");

            getComisionEntidadBancaria(mValues.get(position).getEntidadBancaria());

            final Bitmap bmpOn = BitmapFactory.decodeResource(getResources(), R.mipmap.star_on);
            final Bitmap bmpOff = BitmapFactory.decodeResource(getResources(), R.mipmap.star_off);

            if(moneda.equals("Libras"))
                holder.mcomisionView.setText(comision+"£");
            else
                if(moneda.equals("Euros"))
                    holder.mcomisionView.setText(comision+"€");

            if(holder.mItem.isFav()==1)
                holder.mImageButtonFav.setImageBitmap(bmpOn);
            else
                holder.mImageButtonFav.setImageBitmap(bmpOff);

            holder.mImageButtonFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dbhelper = new DataBaseHelperCajeros(getBaseContext());
                    if(holder.mItem.isFav()==0){ //no Fav
                        holder.mItem.setFav(1);
                        dbhelper.setFavoritoCajero( holder.mItem.getId(), 1);
                        holder.mImageButtonFav.setImageBitmap(bmpOn);
                        Toast.makeText(getBaseContext(),"Cajero "+holder.mItem.getEntidadBancaria()+
                        " añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        holder.mItem.setFav(0);
                        dbhelper.setFavoritoCajero( holder.mItem.getId(), 0);
                        holder.mImageButtonFav.setImageBitmap(bmpOff);
                        Toast.makeText(getBaseContext(),"Cajero "+holder.mItem.getEntidadBancaria()+
                        " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                    dbhelper.close();

                }
            });

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
            public final TextView mDistancia;
            public final ImageButton mImageButtonFav;
            public Cajero mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mEntidadBancariaView = (TextView) view.findViewById(R.id.nombreEntidad);
                mcomisionView = (TextView) view.findViewById(R.id.comision);
                mDistancia = (TextView) view.findViewById(R.id.textViewDistancia);
                mImageButtonFav = (ImageButton) view.findViewById(R.id.imageButtonFav);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mEntidadBancariaView.getText() + "'";
            }
        }
    }

    public  void burbujaComision(){
        int i, j;
        Cajero aux = new Cajero();
        for(i=0;i<cajerosArray.size()-1;i++)
            for(j=0;j<cajerosArray.size()-1;j++)
                if(getComisionEntidadBancaria(cajerosArray.get(j+1).getEntidadBancaria())<=
                        getComisionEntidadBancaria(cajerosArray.get(j).getEntidadBancaria())){
                    asignarCajero(aux,cajerosArray.get(j+1));
                    asignarCajero(cajerosArray.get(j+1),cajerosArray.get(j));
                    asignarCajero(cajerosArray.get(j),aux);
                }
    }

    public  void burbujaDistancia(){
        locationB = new Location("point B");
        locationB.setLatitude(latitudUser);
        locationB.setLongitude(longitudUser);
        int i, j;
        Cajero aux = new Cajero();
        for(i=0;i<cajerosArray.size()-1;i++)
            for(j=0;j<cajerosArray.size()-1;j++) {
                locationAaux = new Location("cajero j + 1");
                locationAaux.setLatitude(cajerosArray.get(j + 1).getLatitud());
                locationAaux.setLongitude(cajerosArray.get(j + 1).getLongitud());
                distanceAaux = locationAaux.distanceTo(locationB);

                locationBaux = new Location("cajero j");
                locationBaux.setLatitude(cajerosArray.get(j).getLatitud());
                locationBaux.setLongitude(cajerosArray.get(j).getLongitud());
                distanceBaux = locationBaux.distanceTo(locationB);

                if (distanceAaux <= distanceBaux) {
                    asignarCajero(aux, cajerosArray.get(j + 1));
                    asignarCajero(cajerosArray.get(j + 1), cajerosArray.get(j));
                    asignarCajero(cajerosArray.get(j), aux);
                }
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