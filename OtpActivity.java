package com.aumento.there_level_atm_security.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.BottomActivity;
import com.aumento.there_level_atm_security.MainActivity;
import com.aumento.there_level_atm_security.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OtpActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText edt_number;
    EditText edt_otp;
    Button send_otp;
    Button verify_otp;
    GlobalPreference globalPreference;

    String  token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        globalPreference = new GlobalPreference(getApplicationContext());
        initViews();
        setDefaults();
    }
    public  void initViews(){
        edt_number=findViewById(R.id.et_Phone_no);
        edt_otp=findViewById(R.id.et_otp);
        send_otp=findViewById(R.id.send_number);
        verify_otp=findViewById(R.id.btn_verify);
        send_otp.setOnClickListener(this);
        verify_otp.setOnClickListener(this);
    }
    public void setDefaults(){
        Random rand = new Random();
        token=String.format("%04d", rand.nextInt(10000));
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.send_number){
            checknumber();
        }else if(v.getId()==R.id.btn_verify){
            verify();
        }
    }

    private void checknumber() {

        String ip = globalPreference.RetriveIP();
        final String acc = globalPreference.getAccno();
        final String phno = edt_number.getText().toString();
        String phoneUrl="http://" + ip + "/Aumento/three_level_security/checkphone.php";
        Log.d("TAG", "ph : "+phno+" asdasd"+acc+" UYRL: "+phoneUrl);
        if(!edt_number.getText().toString().equals("")){
            StringRequest stringRequest1=new StringRequest(Request.Method.POST, phoneUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "onResponse: "+response);
                    if(response.contains("sendotp"))
                    {
                        sendOTP();
                    }
                    else
                        Toast.makeText(OtpActivity.this, "Please enter the registered phone number linked to your account", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OtpActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("phno",phno);
                    params.put("accno",acc);
                    return params;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            requestQueue1.add(stringRequest1);
        }else{
            Toast.makeText(this, "Please Fill the Fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void verify(){
        String verify_token=edt_otp.getText().toString();
        if(verify_token.equals(token)){
            Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), BottomActivity.class));
        }else{
            Toast.makeText(this, "Invalid Otp Code", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendOTP(){

        String API_URL="http://cloudviews.in/SMS/gateway.php?mobile="+edt_number.getText().toString()+"&msg="+token;
            StringRequest stringRequest=new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(OtpActivity.this, "Message Has Been Send Sucessfully", Toast.LENGTH_SHORT).show();
                    send_otp.setText("Resend OTP");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OtpActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
    }

}
