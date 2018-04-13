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
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.feup.cmov.acmecoffee.Utils.SessionManager;
import org.feup.cmov.acmecoffee.Utils.ToastManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    private SharedPreferences prefs;
    Map<String, ?> sessionContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sessionContent = prefs.getAll();



        setNavigationViewItemsTitle();
    }

    private void setNavigationViewItemsTitle() {
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
            DatabaseHelper.getInstance(this).deleteVouchers();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if(id == R.id.update_user_vouchers) {
            GetCustomerVouchers getCustomerVouchers = new GetCustomerVouchers(this, (Long) sessionContent.get("id"));

            Thread thr = new Thread(getCustomerVouchers);
            thr.start();
        } else if(id == R.id.update_menu) {
            GetItems getItems = new GetItems(this);
            Thread thr = new Thread(getItems);
            thr.start();
        }

        else if(id ==  R.id.nav_nif){
            GetCustomerRequests getCustomerRequests = new GetCustomerRequests(this, (Long) sessionContent.get("id"));
            Thread thr = new Thread(getCustomerRequests);
            thr.start();
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void goToMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewMenuActivity.class);
        startActivity(intent);
    }

    public void goToVouchers(View view){
        Intent intent = new Intent(getApplicationContext(), ViewVouchersActivity.class);
        startActivity(intent);
    }

    public void goToRequest(View view){
        Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
        startActivity(intent);
    }

    public void goToRequestsRecord(View view){
        Intent intent = new Intent(getApplicationContext(), RequestsRecordActivity.class);
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
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(context, ToastManager.ITEMS_LOAD_SUCCESS, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(context, ToastManager.ITEMS_LOAD_ERROR, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, ToastManager.ITEMS_LOAD_ERROR, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class GetCustomerVouchers implements Runnable {
        Context context = null;
        Long id;

        GetCustomerVouchers(Context context, Long id) {
            this.context = context;
            this.id = id;
        }

        @Override
        public void run() {
            String response = HttpHandler.getCustomerVouchers(id);

            if(response != null) {
                DatabaseHelper.getInstance(context).updateVouchersTable(response);
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, ToastManager.VOUCHERS_LOAD_SUCCESS, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, ToastManager.VOUCHERS_LOAD_ERROR, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class GetCustomerRequests implements Runnable {
        Context context = null;
        Long id;

        GetCustomerRequests(Context context, Long id) {
            this.context = context;
            this.id = id;
        }

        @Override
        public void run() {
            String response = HttpHandler.getCustomerRequests(id);

            if(response != null) {
                DatabaseHelper.getInstance(context).updateRequestsTable(response);
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, ToastManager.REQUEST_LOAD_SUCCESS, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, ToastManager.REQUEST_LOAD_ERROR, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
