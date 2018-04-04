package org.feup.cmov.acmecoffee;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.User;
import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper items;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        items = new DatabaseHelper(this);

        GetItems getItems = new GetItems(this);
        Thread thr = new Thread(getItems);
        thr.start();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE)); Meter aqui a cor primaria
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            User.delete();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewMenuActivity.class);
        startActivity(intent);
    }

    private void addItemsToDatabase(JSONArray its) throws JSONException {
        System.out.println(its.toString());

        db = items.getWritableDatabase();
        ContentValues values = new ContentValues();
        JSONObject item;
        for(int i = 0;i < its.length(); i++) {
            item = new JSONObject(its.getString(i));
            values.put("item_id", item.getString("id"));
            values.put("name", item.getString("name"));
            values.put("price", item.getLong("price"));
            values.put("type", item.getString("type"));
            db.insert("Items", null, values);
        }
    }

    private class GetItems implements Runnable {
        Context context = null;

        GetItems(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            String response = HttpHandler.getAllItems();
            try {
                addItemsToDatabase(new JSONArray(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToVouchers(View view){
        Intent intent = new Intent(getApplicationContext(), ViewVouchersActivity.class);
        startActivity(intent);
    }
}
