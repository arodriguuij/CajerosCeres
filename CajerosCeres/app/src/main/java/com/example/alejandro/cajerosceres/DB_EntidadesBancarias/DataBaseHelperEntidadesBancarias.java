package com.example.alejandro.cajerosceres.DB_EntidadesBancarias;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperEntidadesBancarias extends SQLiteOpenHelper {
    public DataBaseHelperEntidadesBancarias(Context context) {
        super(context, "Retail", null, 1);
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
}
