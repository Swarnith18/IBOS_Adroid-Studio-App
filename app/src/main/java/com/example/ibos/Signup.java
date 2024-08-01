package com.example.ibos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ibos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private static final String TAG = "Signup";

    private EditText usernameEditText;
    private EditText mobileNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ImageButton showHidePasswordButton;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        showHidePasswordButton = findViewById(R.id.showHidePasswordButton);
        Button signupButton = findViewById(R.id.button6);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Signup button clicked");
                signup();
            }
        });

        showHidePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    private void signup() {
        String username = usernameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mobileNumber) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordStrong(password)) {
            Toast.makeText(this, "Password is not strong enough", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user object
        User user = new User(username, mobileNumber);

        // Generate a new unique key for the user
        String userId = usersRef.push().getKey();

        // Save the user object under the generated key
        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Signup.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User data saved to Firebase");

                        // Proceed to the Home activity
                        Intent intent = new Intent(Signup.this, Home.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error adding user to Firebase: " + e.getMessage());
                    }
                });
    }

    private boolean isPasswordStrong(String password) {
        // Minimum length check
        if (password.length() < 8) {
            return false;
        }

        // Uppercase and lowercase letters check
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }
        if (!hasUppercase || !hasLowercase) {
            return false;
        }

        // Numeric digits check
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        if (!hasDigit) {
            return false;
        }

        // Special characters check
        boolean hasSpecialCharacter = !password.matches("[A-Za-z0-9]+");
        if (!hasSpecialCharacter) {
            return false;
        }

        return true;
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showHidePasswordButton.setImageResource(R.drawable.ic_hide_password);
        } else {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePasswordButton.setImageResource(R.drawable.ic_show_password);
        }

        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    public static class User {
        private String username;
        private String mobileNumber;

        public User() {
            // Default constructor required for Firebase database
        }

        public User(String username, String mobileNumber) {
            this.username = username;
            this.mobileNumber = mobileNumber;
        }

        public String getUsername() {
            return username;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }
    }
}
