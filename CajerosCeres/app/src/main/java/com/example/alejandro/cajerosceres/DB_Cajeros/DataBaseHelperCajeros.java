package com.example.alejandro.cajerosceres.DB_Cajeros;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperCajeros extends SQLiteOpenHelper{

    public DataBaseHelperCajeros(Context context) {
        super(context, "DB_Cajeros", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CajeroTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int prevVersion, int newVersion) {
        sqLiteDatabase.execSQL(CajeroTable.DROP_QUERY);
        sqLiteDatabase.execSQL(CajeroTable.CREATE_QUERY);
    }

    public void importarCajero(Cajero c){
        ContentValues values = new ContentValues();
        try (Cursor test = this.getReadableDatabase().rawQuery("select * from cajeros where _id like '"+c.getId()+"'",null)){
            if(test.getCount() == 0){
                values.put(CajeroTable.COLUMNA_ID, c.getId());
                values.put(CajeroTable.COLUMNA_ENTIDADBANCARIA, c.getEntidadBancaria());
                values.put(CajeroTable.COLUMNA_URIFOTOCAJERO, c.getUriFotoCajero());
                values.put(CajeroTable.COLUMNA_LONGITUD, c.getLongitud());
                values.put(CajeroTable.COLUMNA_LATITUD, c.getLatitud());
                values.put(CajeroTable.COLUMNA_DIRECCION, c.getDireccion());
                values.put(CajeroTable.COLUMNA_FAV, c.isFav());

                //insert rows
                this.getWritableDatabase().insert(CajeroTable.TABLA_CAJEROS, null, values);
            }
        }
    }

    public Cursor getCursorCajero() {
        return this.getReadableDatabase().rawQuery(CajeroTable.SELECT_ALL_QUERY, null);
    }

    public Cursor getCajero(Integer idCajero) {
        try ( Cursor test = this.getReadableDatabase().rawQuery("select * from cajeros where _id like '"+idCajero+"'",null)) {
            if (test.getCount() != 0)
                return test;
        }
        return null;
    }

    public Cursor getCajerosEntidadBancaria(String entidadBancariaSeleccion) {
        try ( Cursor test = this.getWritableDatabase().rawQuery("select * from cajeros where entidadBancaria like '"+entidadBancariaSeleccion+"'",null)) {
            if (test.getCount() != 0)
                return test;
        }
        return null;
    }

    public boolean setFavoritoCajero(Integer idCajero, int favorito){
        ContentValues values = new ContentValues();
        try (Cursor test = this.getReadableDatabase().rawQuery("UPDATE cajeros SET fav = '"+ favorito +"' where _id like '"+ idCajero +"'",null)) {
            if (test.getCount() != 0) {
                return true;
            }
        }
        return false;
    }

    public void onRestar() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(CajeroTable.DROP_QUERY);
        sqLiteDatabase.execSQL(CajeroTable.CREATE_QUERY);
    }
}
