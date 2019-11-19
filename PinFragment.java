package com.aumento.there_level_atm_security.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.there_level_atm_security.R;
import com.aumento.there_level_atm_security.ViewSinglePin;
import com.aumento.there_level_atm_security.utils.GlobalPreference;
import java.util.HashMap;
import java.util.Map;


public class PinFragment extends Fragment {

    View view;
    EditText accno;
    EditText amount;
    EditText pin;
   // EditText date;
    Button submit;
    Button view_pin;

    DatePickerDialog datePickerDialog;
    public static final String TAG = "PinFragment";
    GlobalPreference globalPreference;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_insert_pin, container, false);
        accno = (EditText) view.findViewById(R.id.paccno);
        pin = (EditText) view.findViewById(R.id.ppin);
     //   date = (EditText) view.findViewById(R.id.pdate);
        amount = (EditText) view.findViewById(R.id.pamount);
        submit = (Button) view.findViewById(R.id.pin_submit);
        view_pin = (Button) view.findViewById(R.id.view_pin);

        globalPreference = new GlobalPreference(getActivity());
        accno.setText(globalPreference.getAccno());
        accno.setEnabled(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        view_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getActivity(), ViewSinglePin.class));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void insert() {

        String ip = globalPreference.RetriveIP();

        String url = "http://" + ip + "/Aumento/three_level_security/insert_pin.php";

        final String accno1 = accno.getText().toString();
        final String amount1 = amount.getText().toString();
        final String pin1 = pin.getText().toString();
     //   final String date1 = date.getText().toString();

        if (accno1.equals("") || amount1.equals("") || pin1.equals("")) {

            Toast.makeText(getActivity(), "please fill the  fields", Toast.LENGTH_SHORT).show();
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "onResponse: " + response);
                    Toast.makeText(getActivity(), "inserted Sucessfully", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationDialog();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accno", accno1);
                    params.put("amount", amount1);
                    params.put("pin", pin1);
                 //   params.put("pin_date", date1);
                    params.put("type", "fixed");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }


    ///////////////////////// notification //////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog() {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.capture)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.capture))
                .setTicker("Tutorialspoint")
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Pin Generated")
                .setContentText("A new Pin have been generated")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());

        playNotificationSound();
    }

    public void playNotificationSound() {
        try
        {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActivity().getApplicationContext().getPackageName() + "/raw/notification.mp3");
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), alarmSound);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //////////////////////////////////// notification //////////////////////////////////////////////////////////
}
