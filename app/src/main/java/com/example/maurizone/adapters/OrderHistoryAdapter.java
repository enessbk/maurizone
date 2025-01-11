package com.example.maurizone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maurizone.R;
import com.example.maurizone.models.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<Order> orderHistoryList;
    private Context context;

    public OrderHistoryAdapter(List<Order> orderHistoryList, Context context) {
        this.orderHistoryList = orderHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orderHistoryList.get(position);
        holder.orderDate.setText(order.getOrderDate());
        holder.orderTotal.setText("$" + order.getTotalAmount());
        holder.orderStatus.setText(order.getStatus());

        StringBuilder products = new StringBuilder();
        for (int i = 0; i < order.getProducts().size(); i++) {
            products.append(order.getProducts().get(i).getProductName())
                    .append(" x ")
                    .append(order.getProducts().get(i).getQuantity());
            if (i < order.getProducts().size() - 1) {
                products.append("\n");
            }
        }
        holder.orderProducts.setText(products.toString());
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, orderTotal, orderStatus, orderProducts;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderProducts = itemView.findViewById(R.id.orderProducts);
        }
    }
}
