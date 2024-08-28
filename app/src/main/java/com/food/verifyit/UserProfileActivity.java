package com.food.verifyit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userEmail;
    private ImageView appLogo;
    private Button deleteAccountButton, changePasswordButton;
    private FloatingActionButton dialogButton;
    private User user;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        user=new User(this);
        userEmail=findViewById(R.id.tv_user_email);
        appLogo=findViewById(R.id.app_logo);
        deleteAccountButton=findViewById(R.id.btn_delete_account);
        changePasswordButton=findViewById(R.id.btn_change_password);
        dialogButton=findViewById(R.id.dialog_button);

        String mail=user.getEmailOfCurrentUSer();
        userEmail.setText(mail);

        deleteAccountButton.setOnClickListener(v->{
            deleteAccount();
        });

        changePasswordButton.setOnClickListener(v->{
            changePassword();
        });

        dialogButton.setOnClickListener(v->{
            showNavigationDialog();
        });
    }

    public void changePassword(){

        Intent intent=new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void deleteAccount(){


        user.deleteUser();
        logout();
        finish();

    }

    private void showNavigationDialog() {
        // Inflate the dialog's layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.navigation_dialog, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set up the dialog buttons
        Button scanDrinkPageButton = dialogView.findViewById(R.id.scan_drink_page_button);
        Button homePageButton = dialogView.findViewById(R.id.home_page_button);
        Button scanningHistoryButton=dialogView.findViewById(R.id.scanning_history_button);
        Button logoutButton=dialogView.findViewById(R.id.logout_button);

        // Set listeners
        scanDrinkPageButton.setOnClickListener(v -> {

            navigateToScanDrinkPage();
            dialog.dismiss();
        });

        homePageButton.setOnClickListener(v -> {

            navigateToHomePage();
            dialog.dismiss();
        });

        scanningHistoryButton.setOnClickListener(v -> {

            navigateToScanHistoryPage();
            dialog.dismiss();
        });

        logoutButton.setOnClickListener(v -> {

            logout();
            dialog.dismiss();
        });
    }

    // Methods to handle navigation
    private void navigateToScanDrinkPage() {

        Intent intent=new Intent(UserProfileActivity.this, ScanDrinkActivity.class);
        startActivity(intent);
    }

    private void navigateToHomePage() {

        Intent intent=new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void navigateToScanHistoryPage() {

        Intent intent=new Intent(UserProfileActivity.this, ScanHistoryActivity.class);
        startActivity(intent);
    }
    public void logout(){

        user.logout(new CallBack() {
            @Override
            public void onSuccess() {
                Intent intent=new Intent(UserProfileActivity.this, loginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String errormessage) {

            }
        });
    }
}
