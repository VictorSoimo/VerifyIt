package com.food.verifyit;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

// User class definition
public class User {
    // Properties
    private String idNumber;
    private String username;
    private String email;
    private String password;
    private localDb userDb;
    private FirebaseAuth mAuth;
    private Context context;

    // Constructor
    public User(Context context) {
        this.context = context;
        userDb = new localDb(context);
        mAuth = FirebaseAuth.getInstance();  // Initialize Firebase Auth
    }
    public Context getContext() {
        return context;
    }

    // Getters
    public String getUserid() {
        return idNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUserid(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // User Registration
    public void register(String idNo,String uName, String uMail, String pass, CallBack callback1) {
        // Check if user exists in Firebase or DB
        setUserid(idNo);
        setUsername(uName);
        setEmail(uMail);
        setPassword(pass);

        if (!doesUserIdExist(getUserid())) {
            // Add user to Firebase
            mAuth.createUserWithEmailAndPassword(uMail, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration success
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUser();
                            callback1.onSuccess();

                        } else {
                            callback1.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        } else {
            // Display error message
            Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
        }
    }

    // Firebase Login
    public void login(String email, String password, CallBack callBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = String.valueOf(getUserId(email));
                            storeUserId(userId);
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                            callBack.onSuccess();
                        }
                    } else {
                        callBack.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    // Firebase Logout
    public void logout(CallBack callback) {
        mAuth.signOut();
        clearUserId();
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
        callback.onSuccess();

    }

    // Add a user to the database
    public void addUser() {
        SQLiteDatabase db = userDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_number", getUserid());
        values.put("username", getUsername());
        values.put("email", getEmail());
        values.put("password", getPassword());
        long result = db.insert("users", null, values);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "User added Successfully", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Check if user exists by ID
    public boolean doesUserIdExist(String userId) {
        SQLiteDatabase db = userDb.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id_number"},
                "id_number = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close(); // Close the cursor
        return exists;
    }

    // Return user by ID
    @SuppressLint("Range")
    public User getUserById(String id) {
        SQLiteDatabase db = userDb.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id_number", "username", "email"},
                "id_number = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(context);
            user.setUserid(cursor.getString(cursor.getColumnIndex("id_number")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            cursor.close();
            return user;
        }
        return null;
    }

    // Update user data
    public void updateUser(User user) {
        SQLiteDatabase db = userDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        db.update("users", values, "id_number = ?", new String[]{String.valueOf(user.getUserid())});
        db.close();
    }


    // Delete user account
    public void deleteUser() {
        String id=getUserIdFromPreferences();
        SQLiteDatabase db = userDb.getWritableDatabase();
        db.delete("users", "id_number = ?", new String[]{String.valueOf(id)});
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Account deletion successful
                    Log.d("DeleteAccount", "User account deleted.");
                    Toast.makeText(getContext(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure
                    Log.e("DeleteAccount", "Failed to delete user account.", task.getException());
                    Toast.makeText(getContext(), "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No user is currently logged in.", Toast.LENGTH_SHORT).show();
        }
        clearUserId();
        db.close();

    }




    // Store user ID in shared preferences
    public void storeUserId(String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_ID", userId);
        editor.apply(); // or editor.commit();
    }
    public String getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("USER_ID", null); // Returns null if no value is found
    }

    // Clear user ID from shared preferences
    public void clearUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_ID");
        editor.apply();
    }

    // Retrieve user ID by email from SQLite database
    public int getUserId(String emailString) {
        SQLiteDatabase db = userDb.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id_number"},
                "email = ?",
                new String[]{emailString},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("id_number"));
            cursor.close();
            return userId;
        }

        return -1; // Return -1 if user not found
    }
    public String getEmailOfCurrentUSer() {
        String uid = getUserIdFromPreferences();
        if (uid == null) {
            throw new IllegalArgumentException("UID cannot be null");
        }
        SQLiteDatabase db1 = userDb.getReadableDatabase();
        Cursor cursor = db1.query(
                "users",
                new String[]{"email"},
                "id_number = ?",
                new String[]{uid},
                null, null, null
        );

        String currentUserEmail = null;
        if (cursor != null && cursor.moveToFirst()) {
            currentUserEmail = cursor.getString(cursor.getColumnIndex("email"));
            cursor.close();

        }
        return currentUserEmail;
    }
    public void changePassword(String oldpass, String newpass){

    }

}
