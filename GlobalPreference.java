package com.aumento.there_level_atm_security.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by MyPc on 09-09-2017.
 */

public class GlobalPreference {


    private SharedPreferences prefs;
    private Context context;
    SharedPreferences.Editor editor;

    public GlobalPreference(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void addIP(String ip) {
        editor.putString(Constants.IP, ip);
        editor.apply();

    }

    public String RetriveIP() {

        return prefs.getString(Constants.IP, "");
    }

    public void SaveCredentials(String accno, String pin) {
        editor.putString(Constants.ACCNO, accno);
        editor.putString(Constants.PIN, pin);
        editor.commit();
        Log.d("TEST", "SaveCredentials : "+accno+" "+pin);
    }

    public void SaveAccountNo(String accno) {
        editor.putString(Constants.ACCNO, accno);
        editor.commit();
        Log.d("TEST", "SaveCredentials : "+accno);
    }

    public String getAccno() {
        return prefs.getString(Constants.ACCNO, "");
    }

    public String getPin() {
        return prefs.getString(Constants.PIN, "");
    }

    public String getNames() {

        return prefs.getString(Constants.NAME, "");
    }

    public String getEmails() {
        return prefs.getString(Constants.EMAIL, "");
    }

    public String getPhones() {

        return prefs.getString(Constants.PHONE, "");
    }

    public String password() {
        return prefs.getString(Constants.PASSWORD, "");
    }

}
