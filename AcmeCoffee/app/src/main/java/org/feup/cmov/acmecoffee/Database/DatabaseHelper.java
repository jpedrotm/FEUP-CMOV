package org.feup.cmov.acmecoffee.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context ctx) {
        super(ctx, "items.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Items(_id INTEGER PRIMARY KEY AUTOINCREMENT,item_id INTEGER, name TEXT, price REAL, type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Items");
        onCreate(db);
    }
}
