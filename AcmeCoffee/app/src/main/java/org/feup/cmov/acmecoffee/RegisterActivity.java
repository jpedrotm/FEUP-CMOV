package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Model.User;
import org.feup.cmov.acmecoffee.Utils.JSONCreater;
import org.feup.cmov.acmecoffee.Utils.ToastManager;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, name, password, confirmPassword, nif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        else if(false) { //verificar email
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
            ra.execute(JSONCreater.convertToJSON(JSONCreater.FormType.REGISTER_FORM, Arrays.asList(emailText, nameText, passwordText, nifText)));
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
        }
    }

    private class RegisterAsync extends AsyncTask<JSONObject, String, String[]> {

        @Override
        protected String[] doInBackground(JSONObject... jsonObjects) {
            System.out.println(jsonObjects[0].toString());
            return new String[0];
        }
    }
}
