package org.feup.cmov.acmecoffee.Utils;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {

    public static void createSession(JSONObject message, SharedPreferences prefs) {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", message.getString("email"));
            editor.putLong("id",message.getLong("id"));
            editor.putString("name", message.getString("name"));
            editor.putString("nif", message.getString("nif"));
            System.out.println("VOUCHERS: " + message.getString("vouchers"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSession(SharedPreferences prefs) {
        prefs.edit().clear().apply();
    }
}
