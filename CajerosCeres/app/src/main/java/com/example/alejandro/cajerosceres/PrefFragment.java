package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefFragment extends PreferenceFragment {
    public static final String KEY_PREF_DISTANCIA = "PREF_LIST_DISTANCIA";
    public static final String KEY_PREF_MONEDAS = "PREF_LIST_MONEDAS";
    public static final String KEY_PREF_ENTIDAD_BANCARIA_USUARIO = "PREF_LIST_ENTIDAD";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ajustes);
    }
}
