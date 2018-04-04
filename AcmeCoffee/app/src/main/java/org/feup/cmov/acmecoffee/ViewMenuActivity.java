package org.feup.cmov.acmecoffee;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewMenuActivity extends AppCompatActivity {

    int[] IMAGES = {R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery};
    ArrayList<String> NAMES = new ArrayList<>();
    ArrayList<Double> PRICES = new ArrayList<>();
    ArrayList<String> TYPES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        fieldViewData();

        CustomAdapter cursorAdapter = new CustomAdapter();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapter);
    }
    private void fieldViewData() {
        DatabaseHelper items = new DatabaseHelper(this);
        SQLiteDatabase db = items.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, price, type FROM items",
                null);

        while(cursor.moveToNext()) {
            NAMES.add(cursor.getString(0));
            PRICES.add(cursor.getDouble(1));
            TYPES.add(cursor.getString(2));
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return NAMES.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);
            TextView textView_name = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_amount = (TextView)view.findViewById(R.id.textView_amount);

            imageView.setImageResource(IMAGES[0]);
            textView_name.setText(NAMES.get(i));
            textView_amount.setText(String.valueOf(PRICES.get(i)));
            return view;
        }
    }
}
