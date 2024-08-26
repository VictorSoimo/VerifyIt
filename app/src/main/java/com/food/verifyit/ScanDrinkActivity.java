package com.food.verifyit;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;


public class ScanDrinkActivity extends AppCompatActivity {

    private TextView title, resultTitle, scanResult;
    private Button scanManualBtn;
    private DecoratedBarcodeView barcodeView;
    private FloatingActionButton dialogButton;
    private BarcodeRepository barcodeRepository;
    private Drinkscanned drink1;


    private User user;
    public static final String BASE_URL = "http://10.0.2.2/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_code);

        drink1=new Drinkscanned(this);
        user=new User(this);
        title=findViewById(R.id.title);
        resultTitle=findViewById(R.id.result_title);
        scanResult=findViewById(R.id.scanned_data_view);
        scanManualBtn=findViewById(R.id.scan_manual_btn);
        barcodeView=findViewById(R.id.barcode_scanner);
        dialogButton=findViewById(R.id.dialog_button);

        dialogButton.setOnClickListener(v-> showNavigationDialog());
        scanManualBtn.setOnClickListener(v->{
            showManualInputDialog();
        });

        // Initialize camera permission launcher
        ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startScanning();
                    } else {
                        Toast.makeText(ScanDrinkActivity.this, "Camera permission required", Toast.LENGTH_SHORT).show();
                    }
                }
        );

// Request camera permission if not already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            startScanning();
        }

        barcodeRepository = BarcodeRepository.getInstance();
        // Observe the LiveData
        barcodeRepository.getDrinkLiveData().observe(this, drink -> {
            if (drink != null) {
                // Show the manufacturer details in a dialog
                checkIfDrinkExistsAndAdd(this,drink.getDrinkcode());
                showManufacturerDetails(drink);
            }
            else{
                Toast.makeText(this,"Drink object is null",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showManualInputDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_manual_entry, null);

        // Find views in the dialog layout
        EditText serialNumberInput = dialogView.findViewById(R.id.serialNumberInput);
        Button submitButton = dialogView.findViewById(R.id.submitButton);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Serial Number Manually");
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> {
            String serialNumber = serialNumberInput.getText().toString();
            if (!serialNumber.isEmpty()) {
                queryBarcode(serialNumber); // Your existing method to query the barcode
                dialog.dismiss();
            } else {
                Toast.makeText(ScanDrinkActivity.this, "Please enter a serial number", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void queryBarcode(String drinkcode) {
        Data inputData = new Data.Builder()
                .putString("drinkCode", drinkcode)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                String scannedData = result.getText();
                scanResult.setText("Scanned Data: " + scannedData);
                // Pass the scanned data to the API for querying
                queryBarcode(scannedData);
            }
        }
    };

    private void startScanning(){

        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
    }

    private void showManufacturerDetails(Drinkscanned drink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manufacturer Details");

        String message = "Product code: " + drink.getDrinkcode() + "\n" +
                "Manufacturer: " + drink.getManufacturer() + "\n" +
                "Product: " + drink.getDrinkname();

        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
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
        Button homePageButton = dialogView.findViewById(R.id.home_page_button);
        Button userProfilePageButton = dialogView.findViewById(R.id.user_profile_page_button);
        Button scanningHistoryButton=dialogView.findViewById(R.id.scanning_history_button);
        Button logoutButton=dialogView.findViewById(R.id.logout_button);

        // Set listeners
        homePageButton.setOnClickListener(v -> {

            navigateToHomePage();
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
    private void navigateToHomePage() {

        Intent intent=new Intent(ScanDrinkActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToUserProfilePage() {

        Intent intent=new Intent(ScanDrinkActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
    private void navigateToScanHistoryPage() {

        Intent intent=new Intent(ScanDrinkActivity.this, ScanHistoryActivity.class);
        startActivity(intent);
    }
    public void logout(){

        user.logout(new CallBack() {
            @Override
            public void onSuccess() {
                Intent intent=new Intent(ScanDrinkActivity.this, loginActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(String errormessage) {

            }
        });
    }
    public void checkIfDrinkExistsAndAdd(LifecycleOwner lifecycleOwner, String drinkcode) {
        // Observe LiveData to get the latest drinkcode
        barcodeRepository=BarcodeRepository.getInstance();
        barcodeRepository.getDrinkLiveData().observe(this, drink -> {
            if (drink != null) {
                String scannedDrinkcode = drink.getDrinkcode();

                // Perform the database check in a background thread
                new Thread(() -> {
                    boolean exists = drink1.doesDrinkExist(scannedDrinkcode);
                    if (exists) {
                        drink1.updateDrink();
                        // Handle case where drink exists
                    } else {
                        // Handle case where drink does not exist
                        drink1.addDrink();
                    }
                }).start();
            }
        });
    }
}



