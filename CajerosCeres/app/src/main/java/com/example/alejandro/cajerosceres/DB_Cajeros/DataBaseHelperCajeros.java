package com.example.alejandro.cajerosceres.DB_Cajeros;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperCajeros extends SQLiteOpenHelper{

    public DataBaseHelperCajeros(Context context) {
        super(context, "Retail", null, 1);
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
}
