package com.aumento.there_level_atm_security;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.utils.GlobalPreference;

import java.util.HashMap;
import java.util.Map;

public class RateActivity extends AppCompatActivity {

    private static final String TAG = "RateActivity";

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;
    GlobalPreference globalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
        mFeedback = (EditText) findViewById(R.id.etFeedback);
        mSendFeedback = (Button) findViewById(R.id.btnSubmit);
        globalPreference = new GlobalPreference(getApplicationContext());

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(RateActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(AppRatingActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                    mSendFeed();
                }
            }


        });
    }

    private void mSendFeed() {

        String ip = globalPreference.RetriveIP();

        String url = "http://" + ip + "/Aumento/three_level_security/rate.php";
        final String ratingScale = String.valueOf(mRatingBar.getRating());
        final String feedback = String.valueOf(mFeedback.getText());
        final String rating = String.valueOf(mRatingScale.getText());

        Log.d(TAG, "mSendFeed: "+ratingScale+ feedback+ rating);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RateActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RateActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("ratingScale",ratingScale);
                params.put("feedback",feedback);
                params.put("rating",rating);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RateActivity.this);
        requestQueue.add(stringRequest);

    }
}

