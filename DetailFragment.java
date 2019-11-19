package com.aumento.there_level_atm_security.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.R;
import com.aumento.there_level_atm_security.utils.GlobalPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class DetailFragment extends Fragment {

    View view;
    TextView name;
    TextView address;
    TextView email;
    TextView accno;
    TextView phone;
    TextView balance;

    GlobalPreference globalPreference;
    public static final String TAG = DetailFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details, container, false);
        name=(TextView)view.findViewById(R.id.name);
        address=(TextView)view.findViewById(R.id.address);
        email=(TextView)view.findViewById(R.id.email);
        phone=(TextView)view.findViewById(R.id.phone);
        accno=(TextView)view.findViewById(R.id.acc_no);
        balance=(TextView)view.findViewById(R.id.bal);
        globalPreference=new GlobalPreference(getActivity());

        select();
        getbal();
        return view;

    }


    public  void getbal(){

        final String accno=globalPreference.getAccno();
        String ip=globalPreference.RetriveIP();

        String url = "http://" + ip + "/Aumento/three_level_security/getBal.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);
                balance.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accno",accno);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }




    public  void select(){

        final String accno=globalPreference.getAccno();
        String ip=globalPreference.RetriveIP();

        String url = "http://" + ip + "/Aumento/three_level_security/getDetails.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);


                    JsonParsing(response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accno",accno);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);




    }



    public  void JsonParsing(String response){

        try {
            JSONArray jsonArray=new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String acc1=jsonObject.getString("account_no");
                String name1=jsonObject.getString("name");
                String address1=jsonObject.getString("address");
                String email1=jsonObject.getString("email_id");
                String phone1=jsonObject.getString("phone_no");
                name.setText(name1);
                accno.setText(acc1);
                address.setText(address1);
                email.setText(email1);
                phone.setText(phone1);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
