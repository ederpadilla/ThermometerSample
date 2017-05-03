package com.eder.padilla.alanproject.ui.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eder.padilla.alanproject.R;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class GrapfhFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_graph,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        return view;
    }

    public static GrapfhFragment newInstance() {
        GrapfhFragment grapfhFragment = new GrapfhFragment();
        return grapfhFragment;
    }
}
