package com.emprendimiento.multiproductosmascotas.Activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Adapter.FactAdapter;
import com.emprendimiento.multiproductosmascotas.Domain.Product;
import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityCashBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class CashActivity extends BaseActivity{
    private ActivityCashBinding binding;
    private ManagmentCart managmentCart;
    private RecyclerView.Adapter adapter;
    private ArrayList<Product> cartList;
    private Calendar currentDate;
    private DatabaseReference databaseReference;
    private  double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        cartList = managmentCart.getListCart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        initList();
        calculateCart();
        setVariable();

        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void calculateCart() {
        double percentTax = 0.15;
        double IVA = 10;
        tax = ((managmentCart.getTotalFee()*percentTax)*100.0)/100;
        double total = ((managmentCart.getTotalFee()+tax)*100.0)/100;
        double itemTotal = (managmentCart.getTotalFee()*100.0)/100;

        binding.totalFreeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+String.format("%.2f",tax));
        binding.ivaTxt.setText("$"+itemTotal);
        //binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+String.format("%.2f",total));
    }

    @SuppressLint("SetTextI18n")
    private void initList() {
        if (cartList.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.reycleView.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.reycleView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.reycleView.setLayoutManager(linearLayoutManager);
        adapter = new FactAdapter(cartList,this,()-> calculateCart());
        binding.reycleView.setAdapter(adapter);
        currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        binding.dateTxt.setText(String.format("%02d-%02d-%02d",year, (month+1),day));
        String cedulaTxt = getIntent().getStringExtra("CEDULA");
        String nombreTxt = getIntent().getStringExtra("NOMBRE");
        String direccionTxt = getIntent().getStringExtra("DIRECCION");
        String telefonoTxt = getIntent().getStringExtra("TELEFONO");
        String correoTxt = getIntent().getStringExtra("CORREO");


        if(cedulaTxt!=null){
            binding.cedulaVal.setText(cedulaTxt);
        }else{
            binding.cedulaVal.setText("0000000000");
        }
        if(nombreTxt!=null){
            binding.nombreVal.setText(nombreTxt);
        }else{
            binding.nombreVal.setText("xxxxxxxxxx");
        }
        if(direccionTxt!=null){
            binding.direccionVal.setText(direccionTxt);
        }else{
            binding.direccionVal.setText("xxxxxxxxxx");
        }
        if(telefonoTxt!=null){
            binding.telefonoVal.setText(telefonoTxt);
        }else{
            binding.telefonoVal.setText("00000000");
        }
        if(correoTxt!=null){
            binding.correoVal.setText(correoTxt);
        }else{
            binding.correoVal.setText(" xxxxxxxxxx");
        }
    }

    private void setVariable() {
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStockInFirebase();
                managmentCart.clearCart(); // Vaciar el carrito
                //Toast.makeText(CashActivity.this, "Imprimiendo y vaciando carrito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CashActivity.this,CorectActivity.class));

            }
        });
    }
    private void updateStockInFirebase() {
        for (Product product : cartList) {
            final String productId = String.valueOf(product.getId());  // Asegúrate de tener un campo 'id' en tu clase Product
            final int quantityPurchased = product.getNumberInCart();

            DatabaseReference productRef = databaseReference.child(productId);
            productRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    long currentStock = dataSnapshot.child("Stock").getValue(Long.class);
                    long newStock = currentStock - quantityPurchased;

                    productRef.child("Stock").setValue(newStock).addOnSuccessListener(aVoid -> {
                        // Actualización exitosa
                    }).addOnFailureListener(e -> {
                        // Manejar el error
                    });
                }
            }).addOnFailureListener(e -> {
                // Manejar el error
            });
        }
    }

}