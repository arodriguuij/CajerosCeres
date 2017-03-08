package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.EntidadBancaria;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CajeroListActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private DataBaseHelperCajeros dbhelper;
    private DataBaseHelperEntidadesBancarias dbhelperEB;
    private List<Cajero> cajerosArray;
    private List<Cajero> listaCajeros;
    private Boolean ingles=false;
    private Boolean libras=false;
    private String entidadBancariaUsuario;
    private String entidadBancariaSeleccion;
    private EntidadBancaria e;
    private Double comision;
    private String aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_list);
        new HttpGetTask().execute();
        if (findViewById(R.id.cajero_detail_container) != null) {
            mTwoPane = true;
        }
        listaCajeros = new ArrayList<Cajero>();

        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        libras = sharedPref.getBoolean(PrefFragment.KEY_PREF_MONEDA_LIBRAS, false);
        ingles = sharedPref.getBoolean(PrefFragment.KEY_PREF_IDIOMA_INGLES, false);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Obtenemos el nombre de origen que el usuario indicó
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        entidadBancariaSeleccion = (String) extras.get("entidadBancariaString");
        entidadBancariaUsuario = (String) extras.get("entidadBancariaUsuarioString");

        switch (entidadBancariaSeleccion){
            case "Todas":
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(cajerosArray));
                break;
            default:
                int i=0;
                while(i<cajerosArray.size()){
                    if(cajerosArray.get(i).getEntidadBancaria().equals(entidadBancariaSeleccion)){
                        listaCajeros.add(cajerosArray.get(i));
                    }
                    i++;
                }
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(listaCajeros));
                break;
        }
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
            holder.mEntidadBancariaView.setText(mValues.get(position).getEntidadBancaria());

            getEntidadBancariaUsuario();
            getComisionEntidadBancaria(mValues.get(position).getEntidadBancaria());

            if(libras)
                holder.mcomisionView.setText(comision+"£");
            else
                holder.mcomisionView.setText(comision+"€");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CajeroDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()+1));
                        CajeroDetailFragment fragment = new CajeroDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.cajero_detail_container, fragment).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CajeroDetailActivity.class);
                        intent.putExtra(CajeroDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()+1));
                        intent.putExtra("entidadBancaria",holder.mItem.getEntidadBancaria());
                        intent.putExtra("uriFotoCajero",holder.mItem.getUriFotoCajero());
                        intent.putExtra("longitud",holder.mItem.getLongitud());
                        intent.putExtra("latitud",holder.mItem.getLatitud());
                        intent.putExtra("fav",holder.mItem.isFav());
                        context.startActivity(intent);
                    }
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
            public Cajero mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mEntidadBancariaView = (TextView) view.findViewById(R.id.nombreEntidad);
                mcomisionView = (TextView) view.findViewById(R.id.comision);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mEntidadBancariaView.getText() + "'";
            }
        }
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<Cajero>> {

        private static final String URL2 = "http://opendata.caceres.es/GetData/GetData?dataset=om:CajeroAutomatico&format=json";
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<Cajero> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL2);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Cajero> result) {
            if (null != mClient)
                mClient.close();
            View recyclerView = findViewById(R.id.cajero_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<Cajero>> {
        @Override
        public List<Cajero> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            cajerosArray = new ArrayList<Cajero>();
            Cajero c;
            String JSONResponse = new BasicResponseHandler().handleResponse(response);
            dbhelper = new DataBaseHelperCajeros(getBaseContext());

            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                JSONObject result = responseObject.getJSONObject("results");
                JSONArray bindings = result.getJSONArray("bindings");

                for (int iBinding = 0; iBinding < bindings.length(); iBinding++) {
                    JSONObject cajero = (JSONObject) bindings.get(iBinding);

                    c=new Cajero();
                    c.setId(iBinding);
                    c.setFav(0);

                    JSONObject om_entidadBancaria = cajero.getJSONObject("om_entidadBancaria");
                    c.setEntidadBancaria(om_entidadBancaria.getString("value"));

                    JSONObject om_tieneEnlaceSIG = cajero.getJSONObject("om_tieneEnlaceSIG");
                    c.setUriFotoCajero(om_tieneEnlaceSIG.getString("value"));

                    JSONObject geo_long = cajero.getJSONObject("geo_long");
                    c.setLongitud(geo_long.getDouble("value"));

                    JSONObject geo_lat = cajero.getJSONObject("geo_lat");
                    c.setLatitud(geo_lat.getDouble("value"));

                    JSONObject om_situadoEnVia = cajero.getJSONObject("om_situadoEnVia");
                    c.setDireccion(om_situadoEnVia.getString("value"));

                    dbhelper.importarCajero(c);
                    cajerosArray.add(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dbhelper.close();
            return cajerosArray;
        }
    }

    private double getComisionEntidadBancaria(String entidadBancariaLista){
        switch(entidadBancariaLista){
            case "BancoPopular":
                comision=e.getComisionBancaPueyo();
                break;
            case "BancaPueyo":
                comision=e.getComisionBancoPopular();
                break;
            case "Bankinter":
                comision=e.getComisionBankinter();
                break;
            case "BBVA":
                comision=e.getComisionBBVA();
                break;
            case "Caixa":
                comision=e.getComisionCaixa();
                break;
            case "CaixaGeral":
                comision=e.getComisionCaixaGeral();
                break;
            case "CajaAlmendralejo":
                comision=e.getComisionCajaAlmendralejo();
                break;
            case "CajaBadajoz":
                comision=e.getComisionCajaBadajoz();
                break;
            case "CajaDuero":
                comision=e.getComisionCajaDuero();
                break;
            case "CajaExtremadura":
                comision=e.getComisionCajaExtremadura();
                break;
            case "CajaRural":
                comision=e.getComisionCajaRural();
                break;
            case "DeutscheBank":
                comision=e.getComisionDeutscheBank();
                break;
            case "Liberban":
                comision=e.getComisionLiberbank();
                break;
            case "Popular":
                comision=e.getComisionPopular();
                break;
            case "Sabadell":
                comision=e.getComisionSabadell();
                break;
            case "Santander":
                comision=e.getComisionSantander();
                break;
        }
        return comision;
    }

    private EntidadBancaria getEntidadBancariaUsuario(){
        dbhelperEB = new DataBaseHelperEntidadesBancarias(getBaseContext());
        try (Cursor cur = dbhelperEB.getCursorEntidadBancaria()){
            while(cur.moveToNext()){
                aux=cur.getString(1);
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
