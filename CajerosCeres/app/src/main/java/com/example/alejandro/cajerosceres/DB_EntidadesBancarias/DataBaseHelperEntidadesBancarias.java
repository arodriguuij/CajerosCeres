package com.example.alejandro.cajerosceres.DB_EntidadesBancarias;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperEntidadesBancarias extends SQLiteOpenHelper {
    public DataBaseHelperEntidadesBancarias(Context context) {
        super(context, "DB_Comisiones", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EntidadBancariaTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int prevVersion, int newVersion) {
        sqLiteDatabase.execSQL(EntidadBancariaTable.DROP_QUERY);
        sqLiteDatabase.execSQL(EntidadBancariaTable.CREATE_QUERY);
    }

    public Cursor getCursorEntidadBancaria() {
        return this.getReadableDatabase().rawQuery(EntidadBancariaTable.SELECT_ALL_QUERY, null);
    }


    public void importarCajero(EntidadBancaria e){
        ContentValues values = new ContentValues();
        try (Cursor test = this.getReadableDatabase().rawQuery("select * from entidadBancaria where _id like '"+e.getId()+"'",null)){
            if(test.getCount() == 0){
                values.put(EntidadBancariaTable.COLUMNA_ID, e.getId());
                values.put(EntidadBancariaTable.COLUMNA_ENTIDADBANCARIA, e.getEntidadBancaria());
                values.put(EntidadBancariaTable.COLUMNA_comisionBancoPopular, e.getComisionBancoPopular());
                values.put(EntidadBancariaTable.COLUMNA_comisionBancaPueyo, e.getComisionBancaPueyo());
                values.put(EntidadBancariaTable.COLUMNA_comisionBankinter, e.getComisionBankinter());
                values.put(EntidadBancariaTable.COLUMNA_comisionBBVA, e.getComisionBBVA());
                values.put(EntidadBancariaTable.COLUMNA_comisionCaixa, e.getComisionCaixa());
                values.put(EntidadBancariaTable.COLUMNA_comisionCaixaGeral, e.getComisionCaixaGeral());
                values.put(EntidadBancariaTable.COLUMNA_comisionCajaAlmendralejo, e.getComisionCajaAlmendralejo());
                values.put(EntidadBancariaTable.COLUMNA_comisionCajaBadajoz, e.getComisionCajaBadajoz());
                values.put(EntidadBancariaTable.COLUMNA_comisionCajaDuero, e.getComisionCajaDuero());
                values.put(EntidadBancariaTable.COLUMNA_comisionCajaExtremadura, e.getComisionCajaExtremadura());
                values.put(EntidadBancariaTable.COLUMNA_comisionCajaRural, e.getComisionCajaRural());
                values.put(EntidadBancariaTable.COLUMNA_comisionDeutscheBank, e.getComisionDeutscheBank());
                values.put(EntidadBancariaTable.COLUMNA_comisionLiberbank, e.getComisionLiberbank());
                values.put(EntidadBancariaTable.COLUMNA_comisionPopular, e.getComisionPopular());
                values.put(EntidadBancariaTable.COLUMNA_comisionSabadell, e.getComisionSabadell());
                values.put(EntidadBancariaTable.COLUMNA_comisionSantander, e.getComisionSantander());
                //insert rows
                this.getWritableDatabase().insert(EntidadBancariaTable.TABLA_ENTIDADBANCARIA, null, values);
            }
        }
    }

    public Cursor getEntidadBancaria(String entidadBancaria) {
        try ( Cursor test = this.getWritableDatabase().rawQuery("select * from entidadBancaria where entidadBancaria like '"+entidadBancaria+"'",null)) {
            if (test.getCount() != 0)
                return test;
        }
        return null;
    }
}
