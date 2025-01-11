package com.example.maurizone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maurizone.models.Order;
import com.example.maurizone.models.BasketItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends AppCompatActivity {

    private TextView orderSummaryDetails;
    private EditText cardNumberEditText, expiryDateEditText, cvvEditText;
    private Button payButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        orderSummaryDetails = findViewById(R.id.orderSummaryDetails);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        payButton = findViewById(R.id.payButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Retrieve order details from the intent
        order = (Order) getIntent().getSerializableExtra("order");

        if (order != null) {
            displayOrderSummary(order);
        }

        payButton.setOnClickListener(v -> processPayment());
    }

    private void displayOrderSummary(Order order) {
        StringBuilder summary = new StringBuilder();
        for (BasketItem item : order.getProducts()) {
            summary.append(item.getProductName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" - $")
                    .append(item.getProductPrice() * item.getQuantity())
                    .append("\n");
        }
        summary.append("\nTotal: $").append(order.getTotalAmount());
        orderSummaryDetails.setText(summary.toString());
    }

    private void processPayment() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Please fill in all payment details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate payment processing
        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
        updateOrderStatus();
    }

    private void updateOrderStatus() {
        if (order != null) {
            order.setStatus("Paid");
            db.collection("orders").document(order.getOrderID()).set(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Order updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Failed to update order", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
