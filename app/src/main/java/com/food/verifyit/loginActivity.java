package com.food.verifyit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView title, linkRegistration;
    private EditText password, email;
    private Button loginButton;
    private String emailStr;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        user=new User(this);
        logo=findViewById(R.id.logo);
        title=findViewById(R.id.page_title);
        password=findViewById(R.id.password_input);
        email=findViewById(R.id.email_input);
        linkRegistration=findViewById(R.id.link_registration);
        loginButton=findViewById(R.id.login_user_btn);

        linkRegistration.setOnClickListener(v-> navigateToRegistration());
        loginButton.setOnClickListener(v-> performLogin());
    }

    public void navigateToRegistration(){

        Intent intent = new Intent(loginActivity.this, UserRegistrationActivity.class);
        startActivity(intent);
    }
    public  void performLogin() {

        emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();


        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(loginActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            user.login(emailStr, passwordStr, new CallBack() {
                @Override
                public void onSuccess() {
                    Intent intent1 = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent1);
                }

                @Override
                public void onFailure(String errormessage) {
                    Toast.makeText(loginActivity.this, "Authentication failed: " + errormessage, Toast.LENGTH_LONG).show();


                }
            });
        }
    }

}

