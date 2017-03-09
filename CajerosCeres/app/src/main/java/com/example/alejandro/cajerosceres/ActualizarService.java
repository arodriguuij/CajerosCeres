package com.example.alejandro.cajerosceres;

import android.app.Service;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

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
import java.util.List;

public class ActualizarService extends Service {
    MyTask myTask;
    private DataBaseHelperCajeros dbhelper;
    private List<Cajero> cajerosArray;

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //if (myTask==null) {
            myTask.execute();
        //}
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private boolean cent;
        private static final String URL2 = "http://opendata.caceres.es/GetData/GetData?dataset=om:CajeroAutomatico&format=json";
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cent = true;
        }
/*
        @Override
        protected String doInBackground(String... params) {
            while (cent){
                HttpGet request = new HttpGet(URL2);
                CajeroListActivity.JSONResponseHandler responseHandler = new CajeroListActivity.JSONResponseHandler();
                //date = dateFormat.format(new Date());
                try {
                    mClient.execute(request, responseHandler);
                    //publishProgress(date);
                    // Stop 300000 = 5 minutos
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
*/
        @Override
        protected String doInBackground(String... params) {
            HttpGet request = new HttpGet(URL2);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            while (cent) {
                try {
                    mClient.execute(request, responseHandler);
                    publishProgress();
                    Thread.sleep(10000);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), "Cajeros Atualizados", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }

    private class JSONResponseHandler implements ResponseHandler<Void> {
        @Override
        public Void handleResponse(HttpResponse response) throws IOException {
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dbhelper.close();
            return null;
        }
    }
}
