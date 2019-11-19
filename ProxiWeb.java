package com.aumento.there_level_atm_security.fragment;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import com.aumento.there_level_atm_security.R;
import com.aumento.there_level_atm_security.utils.GlobalPreference;

import org.jetbrains.annotations.Nullable;


public class ProxiWeb extends Fragment implements SensorEventListener {

    WebView web;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    public static final String TAG="ProxiWeb";
    GlobalPreference globalPreference;
     String ip="";
    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_proxy, container, false);
        web=(WebView)view.findViewById(R.id.webview);
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        globalPreference=new GlobalPreference(getActivity());
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        ip=globalPreference.RetriveIP();
        web.setWebViewClient(new MyBrowser());
        web.getSettings().setJavaScriptEnabled(true);
        return view;

    }

    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x=sensorEvent.values[0];
        Log.d(TAG, "onSensorChanged: "+x);
        String acc = globalPreference.getAccno();
        String url1="http://" + ip + "/Aumento/three_level_security/web/page1.php?accno="+acc;
        String url2="http://" + ip + "/Aumento/three_level_security/web/page2.php?accno="+acc;
         if(x==0.0){
             web.loadUrl(url1);
         }
         else{
             web.loadUrl(url2);
         }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}