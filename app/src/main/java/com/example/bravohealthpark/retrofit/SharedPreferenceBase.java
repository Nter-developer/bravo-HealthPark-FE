package com.example.bravohealthpark.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferenceBase {

    private static SharedPreferences sharedPreferences;

    public static void setSharedPreference(String key, Set<String> value) {
        if (sharedPreferences == null) {
            // Error handling or initialization logic if needed
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static Set<String> getSharedPreference(String key, Set<String> defaultValue) {
        if (sharedPreferences == null) {
            // Error handling or initialization logic if needed
            return defaultValue;
        }
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(
                APIPreferences.SHARED_PREFERENCE_NAME_COOKIE, Context.MODE_PRIVATE);
    }
}

