package com.eder.padilla.alanproject.ui.main.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.eder.padilla.alanproject.util.Util;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class ConectionStateFragment extends Fragment{

    @BindView(R.id.img_state)
    ImageView mImageState;

    @BindDrawable(R.drawable.ic_wifi_2)
    Drawable mNoInternetAccess;

    @BindDrawable(R.drawable.ic_wifi_access)
    Drawable mWifiAccess;

    @BindView(R.id.next_fragment_network)
    TextView mTvGoback;

    @OnClick(R.id.next_fragment_network)
    public void changeFragment(){
        ((Main2Activity)getActivity()).viewPager.setCurrentItem(0);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_conection_state,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
            if (Util.isOnline(getActivity())){
                mImageState.setImageDrawable(mWifiAccess);
            }else{
                mImageState.setImageDrawable(mNoInternetAccess);
            }
        return view;
    }

    public static ConectionStateFragment newInstance() {
        ConectionStateFragment conectionStateFragment = new ConectionStateFragment();
        return conectionStateFragment;
    }

    @Override
    public void onResume() {
        Util.log("resume");
        if (Util.isOnline(getActivity())){
            mImageState.setImageDrawable(mWifiAccess);
        }else{
            mImageState.setImageDrawable(mNoInternetAccess);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Util.log("Pause");
        super.onPause();
    }

    @Override
    public void onDetach() {
        Util.log("Detach");
        super.onDetach();
    }
}
