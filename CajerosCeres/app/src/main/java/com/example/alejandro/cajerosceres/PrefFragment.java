package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefFragment extends PreferenceFragment {
    public static final String KEY_PREF_MONEDA_LIBRAS = "check_box_ajustes_libras";
    public static final String KEY_PREF_SERVICIO = "check_box_ajustes_servicio";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ajustes);
    }
}
