package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emprendimiento.multiproductosmascotas.databinding.ActivityCorect2Binding;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityCorectBinding;

public class CorectActivity2 extends BaseActivity {
    private ActivityCorect2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCorect2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }

    private void setVariable() {
        binding.endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CorectActivity2.this,Intro_Activity.class));
            }
        });
    }
}