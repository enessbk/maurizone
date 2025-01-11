package com.example.maurizone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maurizone.adapters.BasketAdapter;
import com.example.maurizone.models.BasketItem;
import com.example.maurizone.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BasketActivity extends AppCompatActivity {

    private RecyclerView basketRecyclerView;
    private Button checkoutButton;

    private FirebaseFirestore db;
    private BasketAdapter basketAdapter;
    private List<BasketItem> basketItemList;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        basketRecyclerView = findViewById(R.id.basketRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);

        db = FirebaseFirestore.getInstance();
        basketItemList = new ArrayList<>();
        basketAdapter = new BasketAdapter(basketItemList, this);

        basketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        basketRecyclerView.setAdapter(basketAdapter);

        loadBasketItems();

        checkoutButton.setOnClickListener(v -> proceedToCheckout());
    }

    private void loadBasketItems() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            db.collection("basketItems")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            basketItemList.clear();
                            totalAmount = 0.0;
                            for (DocumentSnapshot document : task.getResult()) {
                                BasketItem basketItem = document.toObject(BasketItem.class);
                                basketItemList.add(basketItem);
                                totalAmount += basketItem.getProductPrice() * basketItem.getQuantity();
                            }
                            basketAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void proceedToCheckout() {
        if (basketItemList.isEmpty()) {
            Toast.makeText(this, "Your basket is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = db.collection("orders").document().getId();
        String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Order order = new Order(orderId, userId, basketItemList, totalAmount, orderDate, "Pending");

        Intent intent = new Intent(BasketActivity.this, PaymentActivity.class);
        intent.putExtra("order", order); // Pass the Order object
        startActivity(intent);
    }
}
