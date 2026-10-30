package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.emprendimiento.multiproductosmascotas.databinding.ActivityIntroBinding;

public class Intro_Activity extends BaseActivity {

    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }
    private void setVariable() {
        binding.btnInicio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToastCorrect("BIENVENIDO");
                startActivity(new Intent(Intro_Activity.this, MainActivity.class));
            }
        });
    }
}