package com.aumento.there_level_atm_security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText accNoET;
    EditText pinET;
    Button submit;
    TextView text_register;
    GlobalPreference globalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    public void initViews() {
        accNoET = findViewById(R.id.acc_noEditText);
        pinET = findViewById(R.id.pinEditText);
        submit = findViewById(R.id.btn_login);
        text_register = findViewById(R.id.txt1);
        globalPreference = new GlobalPreference(getApplicationContext());
        submit.setOnClickListener(this);
        text_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            login();
        } else if(v.getId()==R.id.txt1){
            startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
        }
    }

    public void login() {
        String ip = globalPreference.RetriveIP();
        String url = "http://" + ip + "/Aumento/three_level_security/key.php";

        if (accNoET.getText().toString().equals("") || pinET.equals("")) {
            Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else {

            final String regexStr = "^[0-9]{16}$";
            if (accNoET.getText().toString().matches(regexStr)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject Object=jsonObject.getJSONObject("status");
                        String status=Object.getString("success");
                        if(status.equals("true")){
                            globalPreference.SaveAccountNo(accNoET.getText().toString());
                            startActivity(new Intent(getApplicationContext(),FingerPrintActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Invalid Username and Password"+response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //jsonParsing(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }


            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accno", accNoET.getText().toString());
                    params.put("pin", pinET.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
            else

            {
                Toast.makeText(this, "Invalid Account Number", Toast.LENGTH_SHORT).show();
            }

    }
    }
}
