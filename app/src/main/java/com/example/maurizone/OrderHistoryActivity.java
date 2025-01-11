package com.example.maurizone;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maurizone.adapters.OrderHistoryAdapter;
import com.example.maurizone.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderHistoryList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);

        db = FirebaseFirestore.getInstance();
        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList, this);

        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            db.collection("orders")
                    .whereEqualTo("buyerID", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            orderHistoryList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                orderHistoryList.add(order);
                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
