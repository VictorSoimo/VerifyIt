package com.food.verifyit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;


public class UserRegistrationActivity extends AppCompatActivity {

    private ImageView logo;
    private EditText idNumberInput, emailInput, passwordInput, confirmPasswordInput, usernameInput;
    private Button registerButton;
    private TextView linkLogin;
    private User user1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);

        // Initialize views


        user1=new User(this);
        logo=findViewById(R.id.app_logo);
        idNumberInput = findViewById(R.id.id_number_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_user_btn);
        linkLogin=findViewById(R.id.link_login);
        usernameInput=findViewById(R.id.username);

        // Set up button click listener
        registerButton.setOnClickListener(v -> {
           performRegistration();
        });

        linkLogin.setOnClickListener(v->{
            navigateToLogin();
        });


    }

    public  void performRegistration(){

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String idNumber = idNumberInput.getText().toString().trim();
        String passConfirm = confirmPasswordInput.getText().toString().trim();
        String uname = usernameInput.getText().toString().trim();



//confirm if all fields contain data otherwise prompt user to fill blanks
        if (email.isEmpty()||password.isEmpty()||idNumber.isEmpty()||passConfirm.isEmpty()) {
           Toast.makeText(this,"All fields are required",Toast.LENGTH_SHORT).show();

        }
        else{
            user1.register(idNumber, uname, email, password, new CallBack() {
                @Override
                public void onSuccess() {
                    navigateToLogin();
                }

                @Override
                public void onFailure(String errormessage) {
                  Toast.makeText(UserRegistrationActivity.this,"Registration Failed!!!",Toast.LENGTH_SHORT).show();
                  clearFields();

                }
            });
        }



    }
    public  void navigateToLogin(){
        Intent intent = new Intent(UserRegistrationActivity.this, loginActivity.class);
        startActivity(intent);

    }
    private void clearFields() {
        idNumberInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
    }
}
