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

import org.feup.cmov.acmecoffee.Utils.HttpHandler;
import org.feup.cmov.acmecoffee.Utils.JSONCreater;
import org.feup.cmov.acmecoffee.Utils.SessionManager;
import org.feup.cmov.acmecoffee.Utils.ToastManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, name, password, confirmPassword, nif;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.contains("email")) {
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_register);

        email = findViewById(R.id.register_email);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        nif = findViewById(R.id.register_nif);


        Button btnRegister = findViewById(R.id.register_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterAsyncTask();
            }
        });

        Button btnGoToLogin = findViewById(R.id.already_have_account_button);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkFields(String email, String name, String password, String passwordConfirmation, String nif) {
       if(!validatePassword(password, passwordConfirmation)) {
           Toast.makeText(getApplicationContext(), ToastManager.WRONG_PASSWORDS, Toast.LENGTH_LONG).show();
           return false;
       }
       else if(!validateNif(nif)) {
            Toast.makeText(getApplicationContext(), ToastManager.WRONG_NIF, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(false) { // verificar email
           Toast.makeText(getApplicationContext(), ToastManager.WRONG_EMAIL, Toast.LENGTH_LONG).show();
           return false;
       }

        return true;
    }

    private static boolean validatePassword(String password, String confirmPassword) {
        return password.length() >= 6 && password.length() == confirmPassword.length() && password.equals(confirmPassword);
    }

    private static boolean validateNif(String nif) {
        return nif.length() == 9 && nif.matches("[0-9]+");
    }

    private void RegisterAsyncTask() {
        String emailText = email.getText().toString();
        String nameText = name.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();
        String nifText = nif.getText().toString();

        if(checkFields(emailText, nameText, passwordText, confirmPasswordText, nifText)) {
            RegisterAsync ra = new RegisterAsync();
            try {
                JSONObject message = JSONCreater.convertToJSON(JSONCreater.FormType.REGISTER_FORM,
                        Arrays.asList(emailText, nameText, passwordText, nifText));
                String response = ra.execute(message).get();
                if(response != null) {
                    message = new JSONObject(response);
                    SessionManager.createSession(message, prefs);
                    Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
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

    private static class RegisterAsync extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            return HttpHandler.insertNewCustomer(jsonObjects[0].toString());
        }
    }
}
