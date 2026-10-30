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

import com.emprendimiento.multiproductosmascotas.Adapter.ProductListAdapter;
import com.emprendimiento.multiproductosmascotas.Domain.Product;
import com.emprendimiento.multiproductosmascotas.R;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityListProductsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListProductsActivity extends BaseActivity {
    ActivityListProductsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        initList();
        setVariable();

        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListProductsActivity.this,CartActivity.class));
            }
        });
    }

    private void getIntentExtra() {
        categoryId=getIntent().getIntExtra("CategoryId",0);
        categoryName=getIntent().getStringExtra("CategoryName");


        binding.titleTxt.setText(categoryName);
    }
    private void initList(){
        DatabaseReference myRef = database.getReference("Products");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Product> list = new ArrayList<>();

        Query query = myRef.orderByChild("CategoryTwoId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        if(issue.getValue(Product.class).getStock() > 0){
                            list.add(issue.getValue(Product.class));
                        }
                    }
                    if(list.size()>0){
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListProductsActivity.this,2));
                        adapterListFood = new ProductListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}