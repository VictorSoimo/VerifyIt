package com.food.verifyit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRegistrationActivity extends AppCompatActivity {

    private EditText emailInput, firstNameInput, lastNameInput, licenseNumberInput, passwordInput, confirmPasswordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registration);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        licenseNumberInput = findViewById(R.id.license_number_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_admin_btn);

        // Set up button click listener
        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String firstName = firstNameInput.getText().toString();
            String lastName = lastNameInput.getText().toString();
            String licenseNumber = licenseNumberInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (password == confirmPassword) {
                // Proceed to store in the database
                //  saveToDatabase(email, firstName, lastName, licenseNumber, password, confirmPassword)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
