package com.example.maurizone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maurizone.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText productNameEditText, productDescriptionEditText, productPriceEditText, productCategoryEditText, productStockQuantityEditText;
    private Button uploadImageButton, addProductButton;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.productNameEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productCategoryEditText = findViewById(R.id.productCategoryEditText);
        productStockQuantityEditText = findViewById(R.id.productStockQuantityEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        addProductButton = findViewById(R.id.addProductButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageUrls = new ArrayList<>();

        uploadImageButton.setOnClickListener(v -> openFileChooser());
        addProductButton.setOnClickListener(v -> addProduct());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child("products/" + UUID.randomUUID().toString());
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(AddProductActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUrls.add(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddProductActivity.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void addProduct() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = productNameEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();
        double price = Double.parseDouble(productPriceEditText.getText().toString().trim());
        String category = productCategoryEditText.getText().toString().trim();
        int stockQuantity = Integer.parseInt(productStockQuantityEditText.getText().toString().trim());

        if (name.isEmpty() || description.isEmpty() || price <= 0 || category.isEmpty() || stockQuantity <= 0 || imageUrls.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String productID = db.collection("products").document().getId();
        Product product = new Product(productID, userID, name, description, price, category, imageUrls, stockQuantity);

        db.collection("products").document(productID).set(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddProductActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
