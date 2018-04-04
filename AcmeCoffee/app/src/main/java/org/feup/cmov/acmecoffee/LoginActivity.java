package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.feup.cmov.acmecoffee.Model.User;
import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.feup.cmov.acmecoffee.Utils.JSONCreater;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        Button btnLogin = findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAsyncTask();
            }
        });

        Button btnGoToRegister = findViewById(R.id.dont_have_account_button);
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomepageActivity.class);
                //Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoginAsyncTask() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(emailText.length() >= 0 && passwordText.length() >= 0) {
            try {
                LoginAsync la = new LoginAsync();
                JSONObject message = JSONCreater.convertToJSON(JSONCreater.FormType.LOGIN_FORM, Arrays.asList(emailText,passwordText));
                String response = la.execute(message).get();

                if(response != null) {
                    message = new JSONObject(response);
                    User.createUser(message.getLong("id"), message.getString("email"), message.getString("name"),message.getString("nif"), message.getString("vouchers"));
                    Log.d("INFO", User.getInstance().toString());
                    Intent intent = new Intent(getApplicationContext(),HomepageActivity.class);
                    startActivity(intent);
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

    private static class LoginAsync extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            return HttpHandler.loginCustomer(jsonObjects[0].toString());
        }
    }
}
