package com.example.alejandro.cajerosceres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BusquedaFragment extends Fragment {
    private Button buttonMapaCajeros;
    private Button buttonListaCajeros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment, container, false);
        buttonMapaCajeros = (Button)getActivity().findViewById(R.id.buttonMapaCajeros);
        buttonListaCajeros = (Button)getActivity().findViewById(R.id.buttonListaCajeros);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
