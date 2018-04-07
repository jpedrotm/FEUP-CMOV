package org.feup.cmov.acmecoffee.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.feup.cmov.acmecoffee.Model.Item;
import org.feup.cmov.acmecoffee.Model.Voucher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Voucher> vouchers = new ArrayList<>();

    private static DatabaseHelper instance = null;

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
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

    public void updateItemsTable(JSONArray items) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("CREATE TABLE Items(_id INTEGER PRIMARY KEY AUTOINCREMENT,item_id INTEGER, name TEXT, price REAL, type TEXT);");
        ContentValues values = new ContentValues();
        JSONObject item;
        for(int i = 0;i < items.length(); i++) {
            item = new JSONObject(items.getString(i));
            values.put("item_id", item.getString("id"));
            values.put("name", item.getString("name"));
            values.put("price", item.getLong("price"));
            values.put("type", item.getString("type"));
            db.insert("Items", null, values);
        }
    }

    public ArrayList<Item> getItems() {
        if(items.size() != 0) {
            return items;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT item_id, name, price, type FROM items", null);

        while(cursor.moveToNext()) {
            items.add(new Item(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3)));
            System.out.println("AQUI VAI ITEM: " + cursor.getLong(0) + " ; " +cursor.getString(1) + " ; " + cursor.getDouble(2) + " ; " + cursor.getString(3));
        }

        return items;
    }

    public void updateVouchersTable(String vouchers) {
        deleteVouchers();

        SQLiteDatabase db = getWritableDatabase();
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

    public ArrayList<Voucher> getVouchers() {
        if(vouchers.size() != 0) {
            return vouchers;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT voucher_id, type FROM vouchers", null);

        while(cursor.moveToNext()) {
            vouchers.add(new Voucher(cursor.getLong(0),cursor.getString(1)));
            System.out.println("AQUI VAI VOUCHER: " + cursor.getLong(0) + " ; " +cursor.getString(1));
        }

        return vouchers;
    }

    public void deleteVouchers() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Vouchers");
    }
}
