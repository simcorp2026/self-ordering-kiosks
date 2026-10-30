package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emprendimiento.multiproductosmascotas.databinding.ActivityCorectBinding;

public class CorectActivity extends BaseActivity {
    private ActivityCorectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCorectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }

    private void setVariable() {
        binding.endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CorectActivity.this,Intro_Activity.class));
            }
        });
    }
}