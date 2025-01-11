package com.example.maurizone;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maurizone.models.Product;
import com.example.maurizone.models.Review;
import com.example.maurizone.models.UserProfile;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class DatabaseInitializerActivity extends AppCompatActivity {

    private static final String TAG = "DatabaseInitializer";

    private FirebaseFirestore db;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_initializer);

        db = FirebaseFirestore.getInstance();

        initializeUsers();
        initializeProducts();
        initializeReviews();

        Toast.makeText(this, "Database initialization started.", Toast.LENGTH_SHORT).show();
    }

    private void initializeUsers() {
        List<UserProfile> users = Arrays.asList(
                new UserProfile("user1", "Alice Johnson", "alice@example.com", "English", "https://example.com/profiles/alice.jpg"),
                new UserProfile("user2", "Bob Smith", "bob@example.com", "English", "https://example.com/profiles/bob.jpg")
        );

        for (UserProfile user : users) {
            db.collection("users").document(user.getUserId())
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully: " + user.getUserId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding user: " + user.getUserId(), e));
        }
    }

    private void initializeProducts() {
        List<Product> products = Arrays.asList(
                new Product("product1", "user1", "Smartphone XYZ", "A high-end smartphone with a sleek design.", 999.99, "Electronics", Arrays.asList("https://example.com/products/smartphone1.jpg", "https://example.com/products/smartphone2.jpg"), 50),
                new Product("product2", "user2", "Wireless Headphones", "Noise-cancelling wireless headphones with high-fidelity sound.", 199.99, "Accessories", Arrays.asList("https://example.com/products/headphones1.jpg", "https://example.com/products/headphones2.jpg"), 100)
        );

        for (Product product : products) {
            db.collection("products").document(product.getProductID())
                    .set(product)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Product added successfully: " + product.getProductID()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding product: " + product.getProductID(), e));
        }
    }

    private void initializeReviews() {
        List<Review> reviews = Arrays.asList(
                new Review("review1", "product1", "user2", 5, "Excellent smartphone with great features!", "2023-05-12"),
                new Review("review3", "2MNgTFET535958alXFCn", "user2", 5, "Excellent with great features!", "2023-05-12"),
                new Review("review4", "L8jPqY2dwjBSIj5JDD04", "user2", 5, "Excellent with great features!", "2023-05-12"),
                new Review("review5", "O1tM4AbcD3hxZoRg1oRj", "user2", 5, "Excellent with great features!", "2023-05-12"),
                new Review("review6", "T7ctKILigPcQytMxPdCt", "user2", 5, "Excellent with great features!", "2023-05-12"),
                new Review("review7", "T7ctKILigPcQytMxPdCt", "user1", 5, "Excellent with great features!", "2023-05-12"),
                new Review("review2", "product2", "user1", 4, "Good quality headphones, but a bit expensive.", "2023-06-16")
        );

        for (Review review : reviews) {
            db.collection("reviews").document(review.getReviewID())
                    .set(review)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Review added successfully: " + review.getReviewID()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding review: " + review.getReviewID(), e));
        }
    }
}
