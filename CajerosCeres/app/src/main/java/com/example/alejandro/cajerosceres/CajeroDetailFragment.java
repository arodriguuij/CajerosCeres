package com.example.alejandro.cajerosceres;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.cajerosceres.dummy.DummyContent;

public class CajeroDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private DummyContent.DummyItem mItem;

    public CajeroDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = DummyContent.ITEMS.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID))-1);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("CajerosCeres");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cajero_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.cajero_detail)).setText(mItem.getEntidadBancaria());
        }

        return rootView;
    }
}
