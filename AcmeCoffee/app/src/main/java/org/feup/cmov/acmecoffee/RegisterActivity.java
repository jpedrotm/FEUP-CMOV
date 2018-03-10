package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Logic.User;

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
                String emailText = email.getText().toString();
                String nameText = name.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();
                String nifText = nif.getText().toString();

                if(User.validateFields(emailText,nameText,passwordText,confirmPasswordText,nifText)) {
                    System.out.println("BOA: " + emailText + "\n" + nameText + "\n" + passwordText + "\n" + nifText);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Something is not right!", Toast.LENGTH_LONG).show();
                }
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
}
