package com.example.alejandro.cajerosceres.DB_EntidadesBancarias;

import android.provider.BaseColumns;

public class EntidadBancariaTable implements BaseColumns{
    // Tabla
    public final static String TABLA_CAJEROS = "cajeros";

    // Atributos
    final static String COLUMNA_ID = "_id";
    final static String COLUMNA_ENTIDADBANCARIA = "entidadBancaria";
    final static String COLUMNA_comisionBancoPopular = "comisionBancoPopular";
    final static String COLUMNA_comisionBancaPueyo = "comisionBancaPueyo";
    final static String COLUMNA_comisionBankinter = "comisionBankinter";
    final static String COLUMNA_comisionBBVA = "comisionBBVA";
    final static String COLUMNA_comisionCaixa = "comisionCaixa";
    final static String COLUMNA_comisionCaixaGeral = "comisionCaixaGeral";
    final static String COLUMNA_comisionCajaAlmendralejo = "comisionCajaAlmendralejo";
    final static String COLUMNA_comisionCajaBadajoz = "comisionCajaBadajoz";
    final static String COLUMNA_comisionCajaDuero = "comisionCajaDuero";
    final static String COLUMNA_comisionCajaExtremadura = "comisionCajaExtremadura";
    final static String COLUMNA_comisionCajaRural = "comisionCajaRural";
    final static String COLUMNA_comisionDeutscheBank = "comisionDeutscheBank";
    final static String COLUMNA_comisionLiberbank = "comisionLiberbank";
    final static String COLUMNA_comisionPopular = "comisionPopular";
    final static String COLUMNA_comisionSabadell = "comisionSabadell";
    final static String COLUMNA_comisionSantander = "comisionSantander";

    final static String[] columns = {COLUMNA_ID, COLUMNA_ENTIDADBANCARIA, COLUMNA_comisionBancoPopular,
            COLUMNA_comisionBancaPueyo, COLUMNA_comisionBankinter, COLUMNA_comisionBBVA, COLUMNA_comisionCaixa,
            COLUMNA_comisionCaixaGeral, COLUMNA_comisionCajaAlmendralejo, COLUMNA_comisionCajaBadajoz,
            COLUMNA_comisionCajaDuero, COLUMNA_comisionCajaExtremadura, COLUMNA_comisionCajaRural,
            COLUMNA_comisionDeutscheBank, COLUMNA_comisionLiberbank, COLUMNA_comisionPopular,
            COLUMNA_comisionPopular, COLUMNA_comisionSabadell, COLUMNA_comisionSantander};

    public static final String CREATE_QUERY = "create table " + TABLA_CAJEROS + " (" +
            COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_ENTIDADBANCARIA + " TEXT, " +
            COLUMNA_comisionBancoPopular + " TEXT, " +
            COLUMNA_comisionBancaPueyo + " TEXT, " +
            COLUMNA_comisionBankinter + " TEXT, " +
            COLUMNA_comisionBBVA + " TEXT, " +
            COLUMNA_comisionCaixa + " TEXT, " +
            COLUMNA_comisionCaixaGeral + " TEXT, " +
            COLUMNA_comisionCajaAlmendralejo + " TEXT, " +
            COLUMNA_comisionCajaBadajoz + " TEXT, " +
            COLUMNA_comisionCajaDuero + " TEXT, " +
            COLUMNA_comisionCajaExtremadura + " TEXT, " +
            COLUMNA_comisionCajaRural + " TEXT, " +
            COLUMNA_comisionDeutscheBank + " TEXT, " +
            COLUMNA_comisionLiberbank + " TEXT, " +
            COLUMNA_comisionPopular + " TEXT, " +
            COLUMNA_comisionSabadell + " TEXT, " +
            COLUMNA_comisionSantander + " TEXT) " ;

    public static final String DROP_QUERY = "drop table " + TABLA_CAJEROS;
    public static final String SElECT_ALL_QUERY = "select * from " + TABLA_CAJEROS;
}
