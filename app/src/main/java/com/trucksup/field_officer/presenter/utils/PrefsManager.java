package com.trucksup.field_officer.presenter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    private static SharedPreferences sharedPreferences = null;

    /**
     * Get shared preference instance.
     *
     * @param context for initialize shared preference.
     * @return shared preferences instance.
     */
    private static SharedPreferences getSharedPreference(final Context context) {
        if (context != null) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            }
        }
        return sharedPreferences;
    }

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setString(String key, String value, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setInt(String key, int value, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setDouble(String key, float value, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setBoolean(String key, boolean value, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get a string shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getString(String key, Context context) {
        return getSharedPreference(context).getString(key, null);
    }

    /**
     * Get a integer shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getInt(String key, Context context) {
        return getSharedPreference(context).getInt(key, 0);
    }

    public static void removeFromPrefs(String key, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * clear all shared preference.
     * @param context
     */
    public static void clearPref(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }

    /**
     * fgijdkf
     * Get a boolean shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getBoolean(String key, Context context) {
        return getSharedPreference(context).getBoolean(key, false);
    }


    /*public static void saveArrayListItinerary(ArrayList<Itinerary> list, Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("keyItinerary", json);
        editor.apply();

    }

    public static ArrayList<Itinerary> getArrayListItinerary(Context context){
        SharedPreferences prefs = getSharedPreference(context);
        Gson gson = new Gson();
        String json = prefs.getString("keyItinerary", null);
        Type type = new TypeToken<ArrayList<Itinerary>>() {}.getType();
        return gson.fromJson(json, type);
    }

    */
}

