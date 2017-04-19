package com.example.alejandro.cajerosceres;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExampleBroadcastReceiver extends BroadcastReceiver {
    MyTask myTask;
    private DataBaseHelperCajeros dbhelper;
    private DataBaseHelperEntidadesBancarias dbhelperEntidad;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí lo que se quiera ejecutar
        System.out.println("*******Temporizador. Actualizar cajeros " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        Toast.makeText(context,"AlamarManager. Actualizar cajeros", Toast.LENGTH_LONG).show();
        dbhelperEntidad = new DataBaseHelperEntidadesBancarias(context);
        dbhelper = new DataBaseHelperCajeros(context);
        crearTablaComisiones();

        myTask = new MyTask();
        myTask.execute();

        dbhelper.close();
        dbhelperEntidad.close();

        //Intent intent2 = new Intent(context, MainActivity.class);
        //context.startActivity(intent2);
    }

    private class MyTask extends AsyncTask<String, String, String> {
        private static final String URL2 = "http://opendata.caceres.es/GetData/GetData?dataset=om:CajeroAutomatico&format=json";
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result){
            System.out.println("*******TFInnnnnnnnnnnnnnnnnnnnnnnnnnnn " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            mClient.close();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet request = new HttpGet(URL2);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
                try {
                    mClient.execute(request, responseHandler);
                    publishProgress();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class JSONResponseHandler implements ResponseHandler<Void> {
        @Override
        public Void handleResponse(HttpResponse response) throws IOException {
            Cajero c;
            String JSONResponse = new BasicResponseHandler().handleResponse(response);

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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void crearTablaComisiones(){
        try (Cursor cur = dbhelperEntidad.getCursorEntidadBancaria()){
            if(cur.getCount()==0){
                EntidadBancaria eBancoPopular = new EntidadBancaria(1,"BancoPopular",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eBancaPueyo = new EntidadBancaria(2,"BancaPueyo",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eBankinter = new EntidadBancaria(3,"Bankinter",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eBBVA = new EntidadBancaria(4,"BBVA",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCaixa = new EntidadBancaria(5,"Caixa",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCaixaGeral = new EntidadBancaria(6,"CaixaGeral",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCajaAlmendralejo = new EntidadBancaria(7,"CajaAlmendralejo",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCajaBadajoz = new EntidadBancaria(8,"CajaBadajoz",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCajaDuero = new EntidadBancaria(9,"CajaDuero",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCajaExtremadura = new EntidadBancaria(10,"CajaExtremadura",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eCajaRural = new EntidadBancaria(11,"CajaRural",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eDeutscheBank = new EntidadBancaria(12,"DeutscheBank",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eLiberban = new EntidadBancaria(13,"Liberban",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria ePopular = new EntidadBancaria(14,"Popular",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eSabadell = new EntidadBancaria(15,"Sabadell",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                EntidadBancaria eSantander = new EntidadBancaria(16,"Santander",3.0,3.0,1.0,1.87,2.0,3.0,3.0,3.0,2.0,3.0,3.0,3.0,2.0,0.0,1.8,1.85);
                dbhelperEntidad.importarCajero(eBancoPopular);
                dbhelperEntidad.importarCajero(eBancaPueyo);
                dbhelperEntidad.importarCajero(eBankinter);
                dbhelperEntidad.importarCajero(eBBVA);
                dbhelperEntidad.importarCajero(eCaixa);
                dbhelperEntidad.importarCajero(eCaixaGeral);
                dbhelperEntidad.importarCajero(eCajaAlmendralejo);
                dbhelperEntidad.importarCajero(eCajaBadajoz);
                dbhelperEntidad.importarCajero(eCajaDuero);
                dbhelperEntidad.importarCajero(eCajaExtremadura);
                dbhelperEntidad.importarCajero(eCajaRural);
                dbhelperEntidad.importarCajero(eDeutscheBank);
                dbhelperEntidad.importarCajero(eLiberban);
                dbhelperEntidad.importarCajero(ePopular);
                dbhelperEntidad.importarCajero(eSabadell);
                dbhelperEntidad.importarCajero(eSantander);
                cur.close();
            }
        }
        dbhelperEntidad.close();
    }
}