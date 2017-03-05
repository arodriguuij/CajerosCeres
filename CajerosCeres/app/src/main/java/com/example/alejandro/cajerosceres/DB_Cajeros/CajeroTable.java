package com.example.alejandro.cajerosceres.DB_Cajeros;

import android.provider.BaseColumns;

public class CajeroTable implements BaseColumns{

    // Tabla
    public final static String TABLA_CAJEROS = "cajeros";

    // Atributos
    final static String COLUMNA_ID = "_id";
    final static String COLUMNA_ENTIDADBANCARIA = "entidadBancaria";
    final static String COLUMNA_URIFOTOCAJERO = "uriFotoCajero";
    final static String COLUMNA_LONGITUD = "longitud";
    final static String COLUMNA_LATITUD = "latitud";
    final static String COLUMNA_DIRECCION = "direccion";
    final static String COLUMNA_FAV = "fav";
    final static String[] columns = {COLUMNA_ID, COLUMNA_ENTIDADBANCARIA, COLUMNA_URIFOTOCAJERO, COLUMNA_LONGITUD,
            COLUMNA_LATITUD, COLUMNA_DIRECCION, COLUMNA_FAV};

    public static final String CREATE_QUERY = "create table " + TABLA_CAJEROS + " (" +
            COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_ENTIDADBANCARIA + " TEXT, " +
            COLUMNA_URIFOTOCAJERO + " TEXT, " +
            COLUMNA_LONGITUD + " TEXT, " +
            COLUMNA_LATITUD + " TEXT, " +
            COLUMNA_DIRECCION + " TEXT, " +
            COLUMNA_FAV + " TEXT) " ;

    public static final String DROP_QUERY = "drop table " + TABLA_CAJEROS;
    public static final String SELECT_ALL_QUERY = "select * from " + TABLA_CAJEROS;
}
