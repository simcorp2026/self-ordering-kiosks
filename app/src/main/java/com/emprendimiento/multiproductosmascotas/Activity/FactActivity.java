package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityFactBinding;

public class FactActivity extends BaseActivity {
    private ActivityFactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  =ActivityFactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVarible();

        getWindow().setStatusBarColor(Color.parseColor("#226599"));
    }

    private void setVarible() {
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCustomToastCorrect("PAGO EN CAJA");
                startActivity(new Intent(FactActivity.this, PaymentMethodActivity_1.class));
            }
        });
        binding.cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCustomToastCorrect("PAGO AQUÍ");
                startActivity(new Intent(FactActivity.this, PaymentMethodActivity_2.class));
            }
        });

    }

}