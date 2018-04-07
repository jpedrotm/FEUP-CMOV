package org.feup.cmov.acmecoffee;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.feup.cmov.acmecoffee.Utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences prefs;

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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseHelper.getInstance(this).getVouchers();

        GetItems getItems = new GetItems(this);
        Thread thr = new Thread(getItems);
        thr.start();

        setNavigationViewItemsTitle();
    }

    private void setNavigationViewItemsTitle() {
        Map<String, ?> sessionContent = prefs.getAll();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE)); Meter aqui a cor primaria
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_name).setTitle((String) sessionContent.get("name"));
        menu.findItem(R.id.nav_email).setTitle((String) sessionContent.get("email"));
        menu.findItem(R.id.nav_nif).setTitle((String) sessionContent.get("nif"));
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            SessionManager.deleteSession(prefs);
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

    public void goToVouchers(View view){
        Intent intent = new Intent(getApplicationContext(), ViewVouchersActivity.class);
        startActivity(intent);
    }

    private class GetItems implements Runnable {
        Context context = null;

        GetItems(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            String response = HttpHandler.getAllItems();

            if(response != null) {
                try {
                    DatabaseHelper.getInstance(context).updateItemsTable(new JSONArray(response));
                    DatabaseHelper.getInstance(context).getItems();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
