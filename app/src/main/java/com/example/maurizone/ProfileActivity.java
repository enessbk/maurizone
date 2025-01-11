package com.example.maurizone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maurizone.adapters.ProductAdapter;
import com.example.maurizone.models.Product;
import com.example.maurizone.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText nameEditText, emailEditText;
    private Spinner languageSpinner;
    private Button orderHistoryButton, saveButton;
    private RecyclerView userProductsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> userProductsList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        languageSpinner = findViewById(R.id.languageSpinner);
        orderHistoryButton = findViewById(R.id.orderHistoryButton);
        saveButton = findViewById(R.id.saveButton);
        userProductsRecyclerView = findViewById(R.id.userProductsRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userProductsList = new ArrayList<>();
        productAdapter = new ProductAdapter(userProductsList, this);

        userProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userProductsRecyclerView.setAdapter(productAdapter);

        // Load user profile
        loadUserProfile();

        // Set up language spinner
        List<String> languages = Arrays.asList("English", "Spanish", "French", "German", "Chinese");
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        orderHistoryButton.setOnClickListener(v -> {
            // Navigate to Order History Activity
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        saveButton.setOnClickListener(v -> {
            // Save profile changes
            saveUserProfile();
        });

        profileImageView.setOnClickListener(v -> {
            // Handle profile picture change (e.g., open gallery to select a new picture)
            Toast.makeText(ProfileActivity.this, "Change profile picture clicked", Toast.LENGTH_SHORT).show();
        });

        // Load user's products
        loadUserProducts();
    }

    private void loadUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        UserProfile userProfile = document.toObject(UserProfile.class);

                        if (userProfile != null) {
                            nameEditText.setText(userProfile.getName());
                            emailEditText.setText(userProfile.getEmail());
                            Glide.with(this).load(userProfile.getProfilePic()).into(profileImageView);

                            // Set language preference
                            String language = userProfile.getLanguage();
                            int position = ((ArrayAdapter<String>) languageSpinner.getAdapter()).getPosition(language);
                            languageSpinner.setSelection(position);
                        }
                    }
                });
    }

    private void saveUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String language = languageSpinner.getSelectedItem().toString();

        UserProfile userProfile = new UserProfile(userId, name, email, language, null); // Assuming profilePic URL will be updated separately

        db.collection("users").document(userId).set(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserProducts() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId != null) {
            db.collection("products")
                    .whereEqualTo("sellerID", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            userProductsList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                userProductsList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
