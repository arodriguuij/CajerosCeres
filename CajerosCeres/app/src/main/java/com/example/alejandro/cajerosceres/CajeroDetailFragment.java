package com.example.alejandro.cajerosceres;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandro.cajerosceres.DB_Cajeros.Cajero;
import com.example.alejandro.cajerosceres.DB_Cajeros.DataBaseHelperCajeros;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CajeroDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Cajero cajero;
    private List<Cajero> listaCajeros;
    private DataBaseHelperCajeros dbhelper;
    private MyTask myTask;
    private View rootView;
    private ImageView logo;
    private ProgressBar progressBar;
    private ImageButton mImageButtonFav;


    public CajeroDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaCajeros = new ArrayList<Cajero>();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Obtener los Cajeros de la base de datos y meterlos en una lista
            dbhelper = new DataBaseHelperCajeros(getActivity().getBaseContext());
            try (Cursor cur = dbhelper.getCursorCajero()){
                while(cur.moveToNext()){
                    Cajero c = new Cajero(cur.getInt(0),cur.getString(1),cur.getString(2)
                            ,cur.getDouble(3),cur.getDouble(4),cur.getString(5),cur.getInt(6));
                    listaCajeros.add( c);
                }
                cur.close();
            }
            dbhelper.close();

            cajero = listaCajeros.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID))-1);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cajero.getEntidadBancaria());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cajero_detail, container, false);

        if (cajero != null) {
            ((TextView) rootView.findViewById(R.id.entidadBancaria)).setText(cajero.getEntidadBancaria());
            logo = (ImageView) rootView.findViewById(R.id.imageView2);
            mImageButtonFav = (ImageButton) rootView.findViewById(R.id.imageButtonFavDetail);

            final Bitmap bmpOn = BitmapFactory.decodeResource(getResources(), R.mipmap.star_on);
            final Bitmap bmpOff = BitmapFactory.decodeResource(getResources(), R.mipmap.star_off);

            if(cajero.isFav()==1)
                mImageButtonFav.setImageBitmap(bmpOn);
            else
                mImageButtonFav.setImageBitmap(bmpOff);

            mImageButtonFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dbhelper = new DataBaseHelperCajeros(getActivity().getBaseContext());

                    if(cajero.isFav()==0){ //no Fav
                        cajero.setFav(1);
                        dbhelper.setFavoritoCajero(cajero.getId(), 1);
                        mImageButtonFav.setImageBitmap(bmpOn);
                        Toast.makeText(getContext(),"Cajero "+cajero.getEntidadBancaria()+
                                " añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        cajero.setFav(0);
                        dbhelper.setFavoritoCajero(cajero.getId(), 0);
                        mImageButtonFav.setImageBitmap(bmpOff);
                        Toast.makeText(getContext(),"Cajero "+cajero.getEntidadBancaria()+
                                " eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                    dbhelper.close();
                }
            });


            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
            Picasso.with(getContext()).load(cajero.getUriFotoCajero()).into(logo);

            myTask = new MyTask();
            myTask.execute();
        }
        return rootView;
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}