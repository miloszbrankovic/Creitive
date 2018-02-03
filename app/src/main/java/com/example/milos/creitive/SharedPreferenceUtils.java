package com.example.milos.creitive;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by milos on 03/02/2018.
 */

public class SharedPreferenceUtils {

    private static final String SHARED_PREFERENCE_FILE = "CreitiveSharedPreferences";

    public static void saveValue(Context context, String variableName, String variableValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(variableName, variableValue);
        mEditor.commit();
        //Log.e("MILOS", "SharedPreferencesUtils ->saveValue() " + variableName + " " + variableValue);
    }

    public static String getStringValue(Context context, String variableName, String defaultValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(variableName, defaultValue);
    }
}
