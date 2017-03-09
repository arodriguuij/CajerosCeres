package com.example.alejandro.cajerosceres;

import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;

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
    public DataBaseHelperCajeros dbhelper;
    private List<Cajero> cajerosArray;
    private List<Cajero> listaCajeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cajero_list);
        new HttpGetTask().execute();
        if (findViewById(R.id.cajero_detail_container) != null) {
            mTwoPane = true;
        }
        listaCajeros = new ArrayList<Cajero>();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Obtenemos el nombre de origen que el usuario indicó
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String entidadBancariaSeleccion = (String) extras.get("entidadBancariaString");

        switch (entidadBancariaSeleccion){
            case "Todas":
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(cajerosArray));
                break;
            default:
                /*dbhelper = new DataBaseHelperCajeros(getBaseContext());
                try (Cursor cur = dbhelper.getCajerosEntidadBancaria(entidadBancariaSeleccion)){
                    if(cur.getCount()!=0) {
                        cur.moveToFirst();
                        while (cur.moveToNext()) {
                            Cajero c = new Cajero(cur.getInt(0), cur.getString(1), cur.getString(2)
                                    , cur.getDouble(3), cur.getDouble(4), cur.getString(5), cur.getInt(6));
                            listaCajeros.add(c);
                        }
                    }else
                        Snackbar.make(recyclerView, "DB vacia", Snackbar.LENGTH_LONG).show();
                    cur.close();
                }
                dbhelper.close();*/
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
            //holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getEntidadBancaria());

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
            public final TextView mIdView;
            public final TextView mContentView;
            public Cajero mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
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

}
