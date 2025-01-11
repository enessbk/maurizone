package com.example.maurizone.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maurizone.R;
import com.example.maurizone.models.BasketItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private List<BasketItem> basketItemList;
    private Context context;

    public BasketAdapter(List<BasketItem> basketItemList, Context context) {
        this.basketItemList = basketItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket, parent, false);
        Log.i("basket adapter log ", "onCreateViewHolder" + view.getContext());

        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        BasketItem basketItem = basketItemList.get(position);
        Log.i("basket adapter log ", "onBindViewHolder: product name = " + basketItem.getProductName());
        Log.i("basket adapter log ", "onBindViewHolder: product image = " + basketItem.getProductImageUrl());
        Log.i("basket adapter log ", "onBindViewHolder: product id = " + basketItem.getProductId());
        holder.productName.setText(basketItem.getProductName());
        holder.productQuantity.setText("Quantity: " + basketItem.getQuantity());
        holder.productPrice.setText("$" + basketItem.getProductPrice());
        Glide.with(holder.productImage.getContext()).load(basketItem.getProductImageUrl()).into(holder.productImage);

        // Handle item removal
        holder.removeButton.setOnClickListener(v -> removeItemFromBasket(basketItem, position));
    }

    private void removeItemFromBasket(BasketItem basketItem, int position) {
        String userId = basketItem.getUserId();
        String productId = basketItem.getProductId();

        if (userId != null && productId != null) {
            FirebaseFirestore.getInstance().collection("basketItems")
                    .document(userId + "_" + productId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            basketItemList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, basketItemList.size());
                            // Optional: Show a toast message
                            Toast.makeText(context, "Item removed from basket", Toast.LENGTH_SHORT).show();
                        } else {
                            // Optional: Show a failure message
                            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return basketItemList.size();
    }

    static class BasketViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productQuantity, productPrice;
        ImageButton removeButton;

        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            Log.i("basket adapter log ", "BasketViewHolder product image = " + productImage);
            productName = itemView.findViewById(R.id.productName);
            Log.i("basket adapter log ", "BasketViewHolder product name = " + productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
