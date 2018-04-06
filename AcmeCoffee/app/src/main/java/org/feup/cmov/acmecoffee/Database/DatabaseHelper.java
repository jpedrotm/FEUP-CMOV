package org.feup.cmov.acmecoffee.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.feup.cmov.acmecoffee.Model.Voucher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context ctx) {
        super(ctx, "items.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Items(_id INTEGER PRIMARY KEY AUTOINCREMENT,item_id INTEGER, name TEXT, price REAL, type TEXT);");
        db.execSQL("CREATE TABLE Vouchers(_id INTEGER PRIMARY KEY AUTOINCREMENT,voucher_id INTEGER, type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("DROP TABLE IF EXISTS Vouchers");
        onCreate(db);
    }
    public void addVouchers(String vouchers, SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Vouchers");
        db.execSQL("CREATE TABLE Vouchers(_id INTEGER PRIMARY KEY AUTOINCREMENT,voucher_id INTEGER, type TEXT);");

        try {
            JSONArray vouchersArray = new JSONArray(vouchers);

            ContentValues values = new ContentValues();
            JSONObject voucher;
            for(int i = 0;i < vouchersArray.length(); i++) {
                voucher = new JSONObject(vouchersArray.getString(i));
                values.put("voucher_id", voucher.getString("id"));
                values.put("type", voucher.getString("type"));
                db.insert("Vouchers", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
