package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Adapter.CategoryAdapter;
import com.emprendimiento.multiproductosmascotas.Adapter.CategoryAdapterTwo;
import com.emprendimiento.multiproductosmascotas.Domain.CategoryTwo;
import com.emprendimiento.multiproductosmascotas.R;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityListCategoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCategoryActivity extends BaseActivity {
    ActivityListCategoryBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        initList();
        setVariable();

        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.cancelBtn.setOnClickListener(view -> finish());
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListCategoryActivity.this,CartActivity.class));
            }
        });
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("CategoryTwo");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<CategoryTwo> list = new ArrayList<>();

        Query query=myRef.orderByChild("CategoryOneId").equalTo(categoryId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryTwo categoryTwo = issue.getValue(CategoryTwo.class);
                        if (categoryTwo != null) {
                            list.add(categoryTwo);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListCategoryActivity.this, 1));
                        adapterListFood = new CategoryAdapterTwo(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryOneId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");

        binding.titleTxt.setText(categoryName);
    }
}