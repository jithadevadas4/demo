package com.aumento.there_level_atm_security.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.os.CancellationSignal;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aumento.there_level_atm_security.BottomActivity;
import com.aumento.there_level_atm_security.R;
import com.aumento.there_level_atm_security.RateActivity;


public class FingerPrintHandler extends AuthenticationCallback {

    private Context mcontext;

    public FingerPrintHandler(Context context) {
        this.mcontext = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal signal=new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        fingerprintManager.authenticate(cryptoObject,signal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        this.update("Fingerprint Authentication error\n" + errString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        Intent i= new Intent(mcontext,OtpActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(i);
        System.exit(0);
        this.update("Fingerprint Authentication succeeded.", true);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        this.update("Fingerprint Authentication failed.", false);
    }

    public void update(String message, Boolean flag){
        TextView textView = (TextView)((Activity)mcontext).findViewById(R.id.errorText);
        textView.setText(message);
        if(flag){
            textView.setTextColor(ContextCompat.getColor(mcontext,R.color.colorPrimaryDark));
        }
    }
}
