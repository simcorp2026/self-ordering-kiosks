package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.emprendimiento.multiproductosmascotas.databinding.ActivityPaymentMethod1Binding;

public class PaymentMethodActivity_1 extends BaseActivity {
    private ActivityPaymentMethod1Binding binding;
    private EditText cedula,nombre,direccion,telefono,correo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentMethod1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        setVariable();
    }

    private void initData() {
        cedula = binding.editTextText;
        nombre = binding.editTextText2;
        direccion = binding.editTextText3;
        telefono = binding.editTextText4;
        correo = binding.editTextText5;
    }

    private void setVariable() {
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cedulaTxt = cedula.getText().toString();
                String nombreTxt = nombre.getText().toString();

                String direccionTxt = direccion.getText().toString();
                String telefonoTxt = telefono.getText().toString();

                String correoTxt = correo.getText().toString();

                Intent intent = new Intent(PaymentMethodActivity_1.this, CashActivity.class);
                // Poner el valor de la cedula en el Intent
                intent.putExtra("CEDULA", cedulaTxt);
                intent.putExtra("NOMBRE",nombreTxt);
                intent.putExtra("DIRECCION",direccionTxt);
                intent.putExtra("TELEFONO",telefonoTxt);
                intent.putExtra("CORREO",correoTxt);

                // Iniciar la nueva actividad
                startActivity(intent);

            }
        });
    }
}