package com.food.verifyit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {


    private TextView currentPassword, newPassword, confirmPassword;
    private Button submitButton, backNavigation;
    private User user;

        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        user=new User(this);
        currentPassword=findViewById(R.id.til_current_password);
        newPassword=findViewById(R.id.til_new_password);
        confirmPassword=findViewById(R.id.til_confirm_new_password);
        submitButton=findViewById(R.id.btn_submit);
        backNavigation=findViewById(R.id.btn_back_page);

        submitButton.setOnClickListener(v->{
            changePassword();
        });

        backNavigation.setOnClickListener(v->{
            navigateToPreviousPage();
        });
    }

    private void changePassword(){
            String oldpass=currentPassword.getText().toString().trim();
            String newpass=newPassword.getText().toString().trim();
        user.changePassword(oldpass, newpass);
    }

    private void navigateToPreviousPage(){
            Intent intent = new Intent(ChangePasswordActivity.this, UserProfileActivity.class);
            startActivity(intent);
    }
}
