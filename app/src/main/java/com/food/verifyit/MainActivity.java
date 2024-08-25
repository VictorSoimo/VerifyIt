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

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView title;
    private FloatingActionButton menuButton;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        // Find views by ID

        user=new User(this);
        logo=findViewById(R.id.app_logo);
        title=findViewById(R.id.app_title);
        menuButton=findViewById(R.id.floatingActionButton);

        menuButton.setOnClickListener(v->{
            showNavigationDialog();
        });


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
        Button userProfilePageButton = dialogView.findViewById(R.id.user_profile_page_button);
        Button scanningHistoryButton=dialogView.findViewById(R.id.scanning_history_button);
        Button logoutButton=dialogView.findViewById(R.id.logout_button);

        // Set listeners
        scanDrinkPageButton.setOnClickListener(v -> {

            navigateToScanDrinkPage();
            dialog.dismiss();
        });

        userProfilePageButton.setOnClickListener(v -> {

            navigateToUserProfilePage();
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

        Intent intent=new Intent(MainActivity.this, ScanDrinkActivity.class);
        startActivity(intent);
    }

    private void navigateToUserProfilePage() {

        Intent intent=new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
    private void navigateToScanHistoryPage() {

        Intent intent=new Intent(MainActivity.this, ScanHistoryActivity.class);
        startActivity(intent);
    }
    public void logout(){

       user.logout(new CallBack() {
           @Override
           public void onSuccess() {
               Intent intent=new Intent(MainActivity.this, loginActivity.class);
               startActivity(intent);
           }

           @Override
           public void onFailure(String errormessage) {

           }
       });
    }
}
