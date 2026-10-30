package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Adapter.CategoryAdapter;
import com.emprendimiento.multiproductosmascotas.Domain.CategoryOne;
import com.emprendimiento.multiproductosmascotas.Domain.CategoryTwo;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityIntroBinding;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCategoryOne();

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });
        binding.cancelBtn.setOnClickListener(view -> finish());
    }


    private void initCategoryOne() {
        DatabaseReference myRed = database.getReference("CategoryOne");
        binding.progressBarBestProducts.setVisibility(View.VISIBLE);
        ArrayList<CategoryOne> list= new ArrayList<>();

        myRed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(CategoryOne.class));
                    }
                    if(list.size() > 0){
                        binding.categoryOneView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.categoryOneView.setAdapter(adapter);
                    }
                    binding.progressBarBestProducts.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}