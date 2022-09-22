package com.mjcdouai.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    private static final String SHARED_PREF = "SHARED_PREF";
    private static final String SHARED_PREF_RADIUS = "SHARED_PREF_RADIUS";
    private static final String SHARED_PREF_NOTIFICATION = "SHARED_PREF_NOTIFICATION";
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    public SharedPrefsHelper(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

    }

    public int getRadius() {
        return mPreferences.getInt(SHARED_PREF_RADIUS, 1500);
    }

    public void setRadius(int radius) {
        mEditor.putInt(SHARED_PREF_RADIUS, radius);
        mEditor.apply();
    }

    public boolean getNotification() {
        return mPreferences.getBoolean(SHARED_PREF_NOTIFICATION, true);
    }

    public void setNotification(boolean notification) {
        mEditor.putBoolean(SHARED_PREF_NOTIFICATION, notification);
        mEditor.apply();
    }

}
