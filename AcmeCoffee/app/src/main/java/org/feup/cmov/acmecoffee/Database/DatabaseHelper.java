package org.feup.cmov.acmecoffee.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    JSONArray items;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String PRICE = "price";

    public DatabaseHelper(Context context, JSONArray items) {
        super(context, DATABASE_NAME, null, 1);
        this.items = items;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY, name TEXT, type TEXT, price REAL);");

        ContentValues cv = new ContentValues();
        JSONObject item;
        for(int i = 0;i < items.length(); i++) {
            item = items.getJSONObject(i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }
}
