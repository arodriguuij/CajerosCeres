package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    private DataBaseHelperEntidadesBancarias dataBaseHelperEntidadesBancarias;
    private EntidadBancaria entidadBancariaUsuarioE;
    private DataBaseHelperCajeros dbhelper;
    private List<Cajero> cajerosArray;
    private String moneda; // Tipo de moneda (Libras o Euros)
    private String entidadBancariaUsuario;
    private String entidadBancariaSeleccion;
    private String orden; // Orden de búsqeuda de los cajeros
    private String distanciaMaxima; // Distancia máxima desde la ubicación del usuario al cajero
    private int distanciaMaximaInt;
    private Double comision;
    private Toolbar toolbar;
    private double longitudUser;
    private double latitudUser;
    private float distancia;
    private float distanciaAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_list);

        toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity_list);
        toolbar.setTitle("Lista de cajeros");
        setSupportActionBar(toolbar);

        cargarConfiguracion();

        View recyclerView = findViewById(R.id.cajero_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    protected void onStop() {
        // Guardado de los datos de la entidad bancaria seleccionada y el orden de la lista de cajeros
        SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("entidadBancariaSeleccion", entidadBancariaSeleccion);
        editor.putString("orden", orden);
        editor.putString("latitudUser",String.valueOf(latitudUser));
        editor.putString("longitudUser",String.valueOf(longitudUser));
        editor.commit();
        super.onStop();
    }

    // Cargar configuración aplicación Android usando SharedPreferences
    public void cargarConfiguracion() {
        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        distanciaMaxima = sharedPref.getString(PrefFragment.KEY_PREF_DISTANCIA, "Infinito");
        setDistanciaMaxima(distanciaMaxima);
        moneda = sharedPref.getString(PrefFragment.KEY_PREF_MONEDAS, "Euros");
        entidadBancariaUsuario = sharedPref.getString(PrefFragment.KEY_PREF_ENTIDAD_BANCARIA_USUARIO, "BBVA");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        try {
            entidadBancariaSeleccion = (String) extras.get("entidadBancariaString");
        }catch (Exception e){ };

        // Obtención de la entidad bancaria seleccionada y el orden de la lista de cajeros
        if(entidadBancariaSeleccion == null){
            SharedPreferences prefs = getSharedPreferences("preferenciasBusqueda", Context.MODE_PRIVATE);
            entidadBancariaSeleccion = prefs.getString("entidadBancariaSeleccion", "");
            orden = prefs.getString("orden", "");
            latitudUser = Double.parseDouble(prefs.getString("latitudUser", ""));
            longitudUser = Double.parseDouble(prefs.getString("longitudUser", ""));
        }
        else{
            latitudUser = (Double) extras.get("latitudUser");
            longitudUser = (Double) extras.get("longitudUser");
            // Tipo de orden de la lista
            orden = (String) extras.getString("orden");
        }

        entidadBancariaUsuarioE=getEntidadBancariaUsuario();

        switch (entidadBancariaSeleccion) {
            case "Todas":
                recuperarTodosCajeros();
                break;
            default:
                recuperarCajerosEntidadBancaria();
                break;
        }
        if (orden.equals("distancia")) {
            burbujaDistancia();
        }
        if (orden.equals("comision")) {
            burbujaComision();
        }
        if (orden.equals("favoritos")) {
            favoritos();
        }
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
            holder.mEntidadBancariaView.setText("Cajero " + mValues.get(position).getEntidadBancaria());
            holder.mEntidadBancariaView.setTextColor(Color.parseColor("#49678D"));

            distancia = calcularDistancia(holder.mItem.getLatitud(), holder.mItem.getLongitud(), latitudUser, longitudUser);

            // Dos decimales
            double roundOff = Math.round(distancia/1000 * 100.0) / 100.0;

            holder.mDistancia.setText(String.valueOf(roundOff) + " Km");
            if (distancia > 1000)
                holder.mDistancia.setTextColor(Color.parseColor("#FE0000")); // Rojo
            else if (distancia > 500)
                holder.mDistancia.setTextColor(Color.parseColor("#E5BE01")); // Amarillo
            else
                holder.mDistancia.setTextColor(Color.parseColor("#57A639")); // verde

            comision=getComisionEntidadBancaria(mValues.get(position).getEntidadBancaria(),entidadBancariaUsuarioE);

            final Bitmap bmpOn = BitmapFactory.decodeResource(getResources(), R.mipmap.star_on);
            final Bitmap bmpOff = BitmapFactory.decodeResource(getResources(), R.mipmap.star_off);

            if (moneda.equals("Libras"))
                holder.mcomisionView.setText(comision + "£");
            else
                if (moneda.equals("Euros"))
                    holder.mcomisionView.setText(comision + "€");

            if (holder.mItem.isFav() == 1)
                holder.mImageButtonFav.setImageBitmap(bmpOn);
            else
                holder.mImageButtonFav.setImageBitmap(bmpOff);

            holder.mImageButtonFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dbhelper = new DataBaseHelperCajeros(getBaseContext());
                    if (holder.mItem.isFav() == 0) { //no Fav
                        holder.mItem.setFav(1);
                        dbhelper.setFavoritoCajero(holder.mItem.getId(), 1);
                        holder.mImageButtonFav.setImageBitmap(bmpOn);
                        Toast.makeText(getBaseContext(), "Cajero " + holder.mItem.getEntidadBancaria() +
                                " añadido a favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.mItem.setFav(0);
                        dbhelper.setFavoritoCajero(holder.mItem.getId(), 0);
                        holder.mImageButtonFav.setImageBitmap(bmpOff);
                        Toast.makeText(getBaseContext(), "Cajero " + holder.mItem.getEntidadBancaria() +
                                " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                    dbhelper.close();

                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CajeroDetailActivity.class);
                    intent.putExtra(CajeroDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId() + 1));
                    intent.putExtra("id", holder.mItem.getId());
                    intent.putExtra("entidadBancaria", holder.mItem.getEntidadBancaria());
                    intent.putExtra("uriFotoCajero", holder.mItem.getUriFotoCajero());
                    intent.putExtra("longitud", holder.mItem.getLongitud());
                    intent.putExtra("latitud", holder.mItem.getLatitud());
                    intent.putExtra("fav", holder.mItem.isFav());
                    intent.putExtra("entidadBancariaSeleccion", entidadBancariaSeleccion);
                    intent.putExtra("entidadBancariaUsuario", entidadBancariaUsuario);
                    intent.putExtra("orden", orden);

                    context.startActivity(intent);
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

    public void burbujaComision() {
        int i, j;
        Cajero aux = new Cajero();
        for (i = 0; i < cajerosArray.size() - 1; i++)
            for (j = 0; j < cajerosArray.size() - 1; j++)
                if (getComisionEntidadBancaria(cajerosArray.get(j + 1).getEntidadBancaria(),entidadBancariaUsuarioE) <=
                        getComisionEntidadBancaria(cajerosArray.get(j).getEntidadBancaria(),entidadBancariaUsuarioE)) {
                    asignarCajero(aux, cajerosArray.get(j + 1));
                    asignarCajero(cajerosArray.get(j + 1), cajerosArray.get(j));
                    asignarCajero(cajerosArray.get(j), aux);
                }
    }

    public void burbujaDistancia() {
        int i, j;
        Cajero aux = new Cajero();
        for (i = 0; i < cajerosArray.size() - 1; i++)
            for (j = 0; j < cajerosArray.size() - 1; j++) {
                distancia = calcularDistancia(cajerosArray.get(j + 1).getLatitud(), cajerosArray.get(j + 1).getLongitud(),
                        latitudUser, longitudUser);

                distanciaAux = calcularDistancia(cajerosArray.get(j).getLatitud(), cajerosArray.get(j).getLongitud(),
                        latitudUser, longitudUser);

                if (distancia < distanciaAux) {
                    asignarCajero(aux, cajerosArray.get(j + 1));
                    asignarCajero(cajerosArray.get(j + 1), cajerosArray.get(j));
                    asignarCajero(cajerosArray.get(j), aux);
                }
            }
    }

    public void favoritos() {
        int i=0;
        List<Cajero> cajerosArrayAux = new ArrayList<Cajero>();
        while(i < cajerosArray.size()){
            if(cajerosArray.get(i).isFav()==1){
                cajerosArrayAux.add(cajerosArray.get(i));
            }
            i++;
        }
        cajerosArray = cajerosArrayAux;
    }

    public void asignarCajero(Cajero aux, Cajero aux2) {
        aux.setId(aux2.getId());
        aux.setEntidadBancaria(aux2.getEntidadBancaria());
        aux.setUriFotoCajero(aux2.getUriFotoCajero());
        aux.setDireccion(aux2.getDireccion());
        aux.setLatitud(aux2.getLatitud());
        aux.setLongitud(aux2.getLongitud());
        aux.setFav(aux2.isFav());
    }

    private void recuperarTodosCajeros() {
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        cajerosArray = new ArrayList<Cajero>();
        try (Cursor cur = dbhelper.getCursorCajero()) {
            while (cur.moveToNext()) {
                Cajero c = new Cajero(cur.getInt(0), cur.getString(1), cur.getString(2)
                        , cur.getDouble(3), cur.getDouble(4), cur.getString(5), cur.getInt(6));

                distancia = calcularDistancia(c.getLatitud(), c.getLongitud(), latitudUser, longitudUser);

                if (distanciaMaximaInt > distancia)
                    cajerosArray.add(c);
            }
            cur.close();
        }
        dbhelper.close();
    }

    private void recuperarCajerosEntidadBancaria() {
        dbhelper = new DataBaseHelperCajeros(getBaseContext());
        cajerosArray = new ArrayList<Cajero>();
        try (Cursor cur = dbhelper.getCursorCajero()) {
            while (cur.moveToNext()) {
                if (cur.getString(1).equals(entidadBancariaSeleccion)) {
                    Cajero c = new Cajero(cur.getInt(0), cur.getString(1), cur.getString(2)
                            , cur.getDouble(3), cur.getDouble(4), cur.getString(5), cur.getInt(6));

                    distancia = calcularDistancia(c.getLatitud(), c.getLongitud(), latitudUser, longitudUser);

                    if (distanciaMaximaInt > distancia)
                        cajerosArray.add(c);
                }
            }
            cur.close();
        }
        dbhelper.close();
    }

    public static double getComisionEntidadBancaria(String entidadBancariaLista, EntidadBancaria e) {
        Double comisionAux=0.0;
        switch (entidadBancariaLista) {
            case "BancoPopular":    comisionAux = e.getComisionBancaPueyo();       break;
            case "BancaPueyo":      comisionAux = e.getComisionBancoPopular();     break;
            case "Bankinter":       comisionAux = e.getComisionBankinter();        break;
            case "BBVA":            comisionAux = e.getComisionBBVA();             break;
            case "Caixa":           comisionAux = e.getComisionCaixa();            break;
            case "CaixaGeral":      comisionAux = e.getComisionCaixaGeral();       break;
            case "CajaAlmendralejo":comisionAux = e.getComisionCajaAlmendralejo(); break;
            case "CajaBadajoz":     comisionAux = e.getComisionCajaBadajoz();      break;
            case "CajaDuero":       comisionAux = e.getComisionCajaDuero();        break;
            case "CajaExtremadura": comisionAux = e.getComisionCajaExtremadura();  break;
            case "CajaRural":       comisionAux = e.getComisionCajaRural();        break;
            case "DeutscheBank":    comisionAux = e.getComisionDeutscheBank();     break;
            case "Liberbank":       comisionAux = e.getComisionLiberbank();        break;
            case "Popular":         comisionAux = e.getComisionPopular();          break;
            case "Sabadell":        comisionAux = e.getComisionSabadell();         break;
            case "Santander":       comisionAux = e.getComisionSantander();        break;
        }
        return comisionAux;
    }

    private EntidadBancaria getEntidadBancariaUsuario() {
        dataBaseHelperEntidadesBancarias = new DataBaseHelperEntidadesBancarias(getBaseContext());
        EntidadBancaria e = null;
        try (Cursor cur = dataBaseHelperEntidadesBancarias.getCursorEntidadBancaria()) {
            while(cur.moveToNext()) {
                if(cur.getString(1).equals(entidadBancariaUsuario))
                    e = new EntidadBancaria(cur.getInt(0), cur.getString(1), cur.getDouble(2)
                            , cur.getDouble(3), cur.getDouble(4), cur.getDouble(5), cur.getDouble(6)
                            , cur.getDouble(7), cur.getDouble(8), cur.getDouble(9), cur.getDouble(10)
                            , cur.getDouble(11), cur.getDouble(12), cur.getDouble(13), cur.getDouble(14)
                            , cur.getDouble(15), cur.getDouble(16), cur.getDouble(17));
            }
            cur.close();
        }
        dataBaseHelperEntidadesBancarias.close();
        return e;
    }

    private void setDistanciaMaxima(String distanciaMaxima) {
        switch (distanciaMaxima) {
            case "100":
                distanciaMaximaInt = 100;
                break;
            case "500":
                distanciaMaximaInt = 500;
                break;
            case "1000":
                distanciaMaximaInt = 1000;
                break;
            case "Infinito":
                distanciaMaximaInt = 999999999;
                break;
        }
    }

    public static float calcularDistancia(double latitudA, double longitudA, double latitudB, double longitudB) {
        Location locationA = new Location("A");
        locationA.setLatitude(latitudA);
        locationA.setLongitude(longitudA);
        Location locationB = new Location("B");
        locationB.setLatitude(latitudB);
        locationB.setLongitude(longitudB);

        return locationA.distanceTo(locationB);
    }
}