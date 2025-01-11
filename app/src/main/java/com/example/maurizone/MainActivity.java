package com.example.maurizone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maurizone.adapters.ProductAdapter;
import com.example.maurizone.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecyclerView;
    private RecyclerView bestSellersRecyclerView;
    private RecyclerView productsRecyclerView;
    private EditText searchBar;
    private String userId;
    private FloatingActionButton profileButton, basketButton, addProductButton;

    private FirebaseFirestore db;
    private ProductAdapter recommendedAdapter;
    private ProductAdapter bestSellersAdapter;
    private ProductAdapter productAdapter;
    private List<Product> recommendedProducts;
    private List<Product> bestSellers;
    private List<Product> productList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBar = findViewById(R.id.searchBar);
        profileButton = findViewById(R.id.profileButton);
        basketButton = findViewById(R.id.basketButton);
        addProductButton = findViewById(R.id.addProductButton);
        recommendedRecyclerView = findViewById(R.id.recommendedRecyclerView);
        bestSellersRecyclerView = findViewById(R.id.bestSellersRecyclerView);
        productsRecyclerView = findViewById(R.id.newRecyclerView);

        db = FirebaseFirestore.getInstance();
        recommendedProducts = new ArrayList<>();
        bestSellers = new ArrayList<>();
        productList = new ArrayList<>();
        recommendedAdapter = new ProductAdapter(recommendedProducts, this);
        bestSellersAdapter = new ProductAdapter(bestSellers, this);
        productAdapter = new ProductAdapter(productList, this);

        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        bestSellersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellersRecyclerView.setAdapter(bestSellersAdapter);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productsRecyclerView.setAdapter(productAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        loadRecommendedProducts();
        loadBestSellers();
        loadProducts();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        basketButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BasketActivity.class);
            startActivity(intent);
        });

        addProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecommendedProducts() {
        db.collection("products")
                .whereEqualTo("category", "recommended")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        recommendedProducts.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            recommendedProducts.add(product);
                        }
                        recommendedAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadBestSellers() {
        db.collection("products")
                .whereEqualTo("category", "best_seller")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        bestSellers.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            bestSellers.add(product);
                        }
                        bestSellersAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadProducts() {
        db.collection("products")
                .whereNotEqualTo("sellerID", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        productList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void filterProducts(String query) {
        List<Product> filteredRecommended = new ArrayList<>();
        List<Product> filteredBestSellers = new ArrayList<>();
        List<Product> filteredProduct = new ArrayList<>();
        for (Product product : recommendedProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredRecommended.add(product);
            }
        }
        for (Product product : bestSellers) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredBestSellers.add(product);
            }
        }
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProduct.add(product);
            }
        }
        productAdapter.filterList(filteredProduct);
        recommendedAdapter.filterList(filteredRecommended);
        bestSellersAdapter.filterList(filteredBestSellers);
    }
}
