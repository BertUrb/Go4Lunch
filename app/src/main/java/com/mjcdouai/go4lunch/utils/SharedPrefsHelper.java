package com.mjcdouai.go4lunch.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    private static final String SHARED_PREF = "SHARED_PREF";
    private static final String SHARED_PREF_RADIUS = "SHARED_PREF_RADIUS";
    private static final String SHARED_PREF_LANGUAGE = "SHARED_PREF_LANGUAGE";
    private static final String SHARED_PREF_NOTIFICATION = "SHARED_PREF_NOTIFICATION";
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    public SharedPrefsHelper(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

    }

    public int getRadius(){
        return  mPreferences.getInt(SHARED_PREF_RADIUS,1500);
    }

    public String getLanguage() {
        return  mPreferences.getString(SHARED_PREF_LANGUAGE,"Auto");
    }

    public boolean getNotification(){
        return  mPreferences.getBoolean(SHARED_PREF_NOTIFICATION,true);
    }

    public void setRadius(int radius) {
        mEditor.putInt(SHARED_PREF_RADIUS,radius);
        mEditor.apply();
    }

    public void setLanguage(String language) {
        mEditor.putString(SHARED_PREF_LANGUAGE,language);
        mEditor.apply();
    }

    public void setNotification(boolean notification) {
        mEditor.putBoolean(SHARED_PREF_NOTIFICATION,notification);
        mEditor.apply();
    }

}
