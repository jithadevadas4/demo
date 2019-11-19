package com.aumento.there_level_atm_security;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.utils.GlobalPreference;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText phone;
    EditText terminate;
    EditText address;
    EditText dob;
    EditText accno;
    GlobalPreference globalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void initViews() {
        name = findViewById(R.id.et_name);
        address=findViewById(R.id.etaddress);
        dob=findViewById(R.id.etdob);
        accno=findViewById(R.id.etaccno);
        password = findViewById(R.id.et_password);
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.et_phone);
        terminate = findViewById(R.id.et_terminate);
        globalPreference = new GlobalPreference(getApplicationContext());
    }


    public void onClick(View view) {
        String ip = globalPreference.RetriveIP();
        String url = "http://" + ip + "/Aumento/three_level_security/regis.php";
        int passlen=password.getText().toString().length();
        int terminate_len=terminate.getText().toString().length();

        if (name.getText().toString().equals("") || email.getText().toString().equals("")||address.getText().toString().equals("") ||accno.getText().toString().equals("") || dob.getText().toString().equals("") || phone.getText().toString().equals("") || terminate.getText().toString().equals("") || password.getText().toString().equals("")) {

            Toast.makeText(this, "Please fill the fields", Toast.LENGTH_LONG).show();

        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (email.getText().toString().matches(emailPattern)) {

                final String reg = "^[0-9]{16}$";
                if (accno.getText().toString().matches(reg)) {

                    final String regexStr = "^[0-9]{10}$";
                    if (phone.getText().toString().matches(regexStr)) {

                        if (passlen == 4 || terminate_len == 4) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(RegistrationActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject statusObject = jsonObject.getJSONObject("status");
                                        String status = statusObject.getString("success");
                                        if (status.equals("true")) {
                                            Toast.makeText(RegistrationActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RegistrationActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("name", name.getText().toString());
                                    params.put("address", address.getText().toString());
                                    params.put("dob", dob.getText().toString());
                                    params.put("accno", accno.getText().toString());
                                    params.put("pin", password.getText().toString());
                                    params.put("email", email.getText().toString());
                                    params.put("terminate", terminate.getText().toString());
                                    params.put("mobile", phone.getText().toString());
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);

                        } else {
                            Toast.makeText(this, "only 4 digit allowed for password and termiante", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid Account Number", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
