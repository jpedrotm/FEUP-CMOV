package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.feup.cmov.acmecoffee.Utils.JSONCreater;
import org.feup.cmov.acmecoffee.Utils.SessionManager;
import org.feup.cmov.acmecoffee.Utils.ToastManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Button btnLogin = findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAsyncTask();
            }
        });

        Button btnGoToRegister = findViewById(R.id.dont_have_account_button);
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginAsyncTask() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(emailText.length() >= 0 && passwordText.length() >= 0) {
            try {
                LoginAsync la = new LoginAsync();
                JSONObject message = JSONCreater.convertToJSON(JSONCreater.FormType.LOGIN_FORM,
                        Arrays.asList(emailText,passwordText));
                String response = la.execute(message).get();

                if(response != null) {
                    message = new JSONObject(response);
                    SessionManager.createSession(message, prefs);
                    DatabaseHelper.getInstance(this).updateVouchersTable(message.getString("vouchers"));
                    Intent intent = new Intent(getApplicationContext(),HomepageActivity.class);
                    startActivity(intent);
                } else {
                    toastMessage(ToastManager.WRONG_FIELDS_LOGIN);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void toastMessage(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class LoginAsync extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            return HttpHandler.loginCustomer(jsonObjects[0].toString());
        }
    }
}
