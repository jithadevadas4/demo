package com.aumento.there_level_atm_security;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.Adapters.SingleAdapter;
import com.aumento.there_level_atm_security.pogo.single;
import com.aumento.there_level_atm_security.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSinglePin extends AppCompatActivity {


    public static final String TAG = ViewSinglePin.class.getSimpleName();
    private RecyclerView RvItem;
    private RecyclerView.LayoutManager mLayoutManager;
    SingleAdapter adapter;
    GlobalPreference globalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_pin);

        globalPreference = new GlobalPreference(getApplicationContext());


        select();
    }

    public void select() {

        final String accno1=globalPreference.getAccno();
        String ip = globalPreference.RetriveIP();

        Log.d("TEST", "single pin retrive: "+accno1+" ip= "+ip);
        String url = "http://" + ip + "/Aumento/three_level_security/getPin.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                showlist(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accno", accno1);
                params.put("type", "fixed");
                return params;


            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    public void showlist(String s) {
        try {
            ArrayList<single> list = new ArrayList<>();
            JSONArray jarray = new JSONArray(s);
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jsonObject = jarray.getJSONObject(i);
                String ac = jsonObject.getString("acc_no");

                String pin = jsonObject.getString("pin");

                String dates = jsonObject.getString("pindate");

                String amt = jsonObject.getString("amt");


                single item = new single();

                item.setAccno(ac);
                item.setAmount(amt);
                item.setDate(dates);
                item.setPins("****");


                list.add(item);

            }


            RvItem = (RecyclerView) findViewById(R.id.rv_sitem);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            RvItem.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RvItem.setLayoutManager(mLayoutManager);


            adapter = new SingleAdapter(getApplicationContext(), list);

            RvItem.setAdapter(adapter);


        } catch (Exception e) {


        }


    }


}