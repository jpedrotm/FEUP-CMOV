package org.feup.cmov.acmecoffee.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

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

    public static KeyPair generateAndStoreKeys(Context context, Long customerId){
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(String.valueOf(customerId), null);         // verify if the keystore has already the keys
            if (entry == null) {
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);                                       // start and end validity
                KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA", Constants.ANDROID_KEYSTORE);    // keys for RSA algorithm
                AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)      // specification for key generation
                        .setKeySize(Constants.KEY_SIZE)                                       // key size in bits
                        .setAlias(String.valueOf(customerId))                                         // name of the entry in keystore
                        .setSubject(new X500Principal("CN=" + Constants.keyname))             // identity of the certificate holding the public key (mandatory)
                        .setSerialNumber(BigInteger.valueOf(12121212))                        // certificate serial number (mandatory)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                KeyPair kp = kgen.generateKeyPair();                                      // the keypair is automatically stored in the app keystore
                return kp;
            }
        }
        catch (Exception ex) {
            Log.d("ERROR GENERATING KEY: ", ex.getMessage());
        }

        return null;
    }
}
