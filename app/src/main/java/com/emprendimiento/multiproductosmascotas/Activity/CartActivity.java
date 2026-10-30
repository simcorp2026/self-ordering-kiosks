package com.emprendimiento.multiproductosmascotas.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Adapter.CartAdapter;
import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private boolean checking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart=new ManagmentCart(this);

        initList();
        calculateCart();
        setVariable();

        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }
    private void initList() {
        if(managmentCart.getListCart().isEmpty()){
            checking = true;
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        }else{
            checking = false;
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }
    private void calculateCart() {
        double itemTotal = (managmentCart.getTotalFee()*100.0)/100;
        binding.totalTxt.setText("$"+String.format("%.2f",itemTotal));
    }
    private void setVariable() {
        binding.cancelBtn.setOnClickListener(view -> finish());
        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(managmentCart.getTotalFee() > 0){
                    startActivity(new Intent(CartActivity.this,FactActivity.class));
                }else{
                    showCustomToastCorrect("¡No existen productos para pagar!");
                    //Toast.makeText(CartActivity.this, "¡No existen productos para pagar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}