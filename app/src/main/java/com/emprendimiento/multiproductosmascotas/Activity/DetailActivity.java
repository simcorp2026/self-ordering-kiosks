package com.emprendimiento.multiproductosmascotas.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.emprendimiento.multiproductosmascotas.Domain.Product;
import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Product object;
    private int num=1;
    private ManagmentCart manejmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
        getIntentExtra();
        setVariable();
    }

    private void setVariable(){
        manejmentCart=new ManagmentCart(this);

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.totalTxt.setText("$"+num*object.getPrice());


        binding.plusBtn.setOnClickListener(view -> {
            if(num < object.getStock()){
                num = num+1;
                binding.numTxt.setText((num+" "));
                binding.totalTxt.setText("$ "+String.format("%.2f",(num*object.getPrice())));
            }else{
                showCustomToastIncorrect("STOCK INSUFICIENTE");
            }
        });


        binding.minusBtn.setOnClickListener(view -> {
            if(num>1){
                num=num-1;
                binding.numTxt.setText(num+"");
                binding.totalTxt.setText("$ "+String.format("%.2f",(num*object.getPrice())));

            }
        });


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num <= object.getStock()){
                    object.setNumberInCart(num);
                    manejmentCart.insertFood(object);
                    showCustomToastCorrect("AGREGADO");
                }else{
                    showCustomToastIncorrect("STOCK INSUFICIENTE");
                }
            }
        });
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this,CartActivity.class));
            }
        });
    }

    private void getIntentExtra() {
        object=(Product) getIntent().getSerializableExtra("object");
    }
}