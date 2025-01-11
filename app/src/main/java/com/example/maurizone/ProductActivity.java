package com.example.maurizone;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maurizone.adapters.ReviewAdapter;
import com.example.maurizone.models.BasketItem;
import com.example.maurizone.models.Product;
import com.example.maurizone.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    private Spinner colorSpinner;
    private RecyclerView reviewsRecyclerView;
    private Button addToBasketButton;

    private FirebaseFirestore db;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    private Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        colorSpinner = findViewById(R.id.colorSpinner);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        addToBasketButton = findViewById(R.id.addToBasketButton);

        db = FirebaseFirestore.getInstance();
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList,this);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewAdapter);

        loadProductDetails();

        addToBasketButton.setOnClickListener(v -> addToBasket());
    }

    private void loadProductDetails() {
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            Glide.with(this).load(product.getImages().get(0)).into(productImageView);
            productNameTextView.setText(product.getName());
            productDescriptionTextView.setText(product.getDescription());
            productPriceTextView.setText("$" + product.getPrice());

            loadReviews();
            loadProductOptions();
        }
    }

    private void loadProductOptions() {
        List<String> colors = product.getAvailableColors();
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
    }

    private void loadReviews() {
        db.collection("reviews")
                .whereEqualTo("productID", product.getProductID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        reviewList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            reviewList.add(review);
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void addToBasket() {
        String selectedColor = colorSpinner.getSelectedItem().toString();
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            BasketItem basketItem = new BasketItem(
                    userId,
                    product.getProductID(),
                    product.getName(),
                    product.getImages().get(0),
                    product.getPrice(), product.getCategory(),
                    1 // Default quantity
            );

            db.collection("basketItems")
                    .document(userId + "_" + product.getProductID())
                    .set(basketItem)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductActivity.this, "Added to basket", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductActivity.this, "Failed to add to basket", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
