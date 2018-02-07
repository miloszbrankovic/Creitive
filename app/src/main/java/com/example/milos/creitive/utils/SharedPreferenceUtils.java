package com.example.milos.creitive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by milos on 03/02/2018.
 */

public class SharedPreferenceUtils {

    private static final String SHARED_PREFERENCE_FILE = "CreitiveSharedPreferences";

    public static void saveStringValue(Context context, String variableName, String variableValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(variableName, variableValue);
        mEditor.commit();
        //Log.e("MILOS", "SharedPreferencesUtils ->saveValue() " + variableName + " " + variableValue);
    }

    public static void saveLongValue(Context context, String variableName, long variableValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong(variableName, variableValue);
        mEditor.commit();
        //Log.e("MILOS", "SharedPreferencesUtils ->saveValue() " + variableName + " " + variableValue);
    }

    public static String getStringValue(Context context, String variableName, String defaultValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(variableName, defaultValue);
    }

    public static long getLongValue(Context context, String variableName, long defaultValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(variableName, defaultValue);
    }

}
