package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emprendimiento.multiproductosmascotas.Adapter.FactAdapter;
import com.emprendimiento.multiproductosmascotas.Domain.Product;
import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.databinding.ActivityCardBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CardActivity extends BaseActivity {
    private ActivityCardBinding binding;
    private ManagmentCart managmentCart;
    private RecyclerView.Adapter adapter;
    private ArrayList<Product> cartList;
    private Calendar currentDate;
    private DatabaseReference databaseReference;
    private  double tax;
    String SECRET_KEY="your_token";
    String PUBLISH_KEY="your_token";
    PaymentSheet paymentSheet;
    String customerID;
    String Ephericalkey;
    String ClientSecret;
    double totalTarjeta, taxTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        cartList = managmentCart.getListCart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        double percentTax = 0.15;
        double IVA = 10;
        taxTarjeta = ((managmentCart.getTotalFee()*(0.15))*100.0)/100;
        totalTarjeta = ((managmentCart.getTotalFee()+taxTarjeta)*100.0)/100;
        initList();
        calculateCart();
        setVariable();


        PaymentConfiguration.init(this,PUBLISH_KEY);
        paymentSheet=new PaymentSheet(this,paymentSheetResult ->{

            onPaymentResult(paymentSheetResult);
        } );

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            //Toast.makeText(CardActivity.this,customerID,Toast.LENGTH_SHORT).show();
                            getEphericalKey(customerID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CardActivity.this);
        requestQueue.add( stringRequest);

    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            updateStockInFirebase();
            managmentCart.clearCart(); // Vaciar el carrito
            showCustomToastCorrect("Pago realizado con exito");
            Intent intent = new Intent(CardActivity.this, CorectActivity2.class);
            startActivity(intent);

        }else{
            showCustomToastIncorrect("Transacciónn Cancelada");
        }
    }

    private void PayFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret,new PaymentSheet.Configuration("Multiproductos Mascotas"
                ,new PaymentSheet.CustomerConfiguration(
                        customerID,
                        Ephericalkey
                ))
        );
    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            Ephericalkey = object.getString("id");

                            //Toast.makeText(CardActivity.this,Ephericalkey,Toast.LENGTH_SHORT).show();
                            getClientSecret(customerID,Ephericalkey);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                header.put("Stripe-Version","2024-06-20");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CardActivity.this);
        requestQueue.add( stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalkey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            //Toast.makeText(CardActivity.this,ClientSecret,Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",customerID);
                params.put("amount",String.valueOf((int) (totalTarjeta * 100)));
                params.put("currency","usd");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CardActivity.this);
        requestQueue.add( stringRequest);


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

        binding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayFlow(); // pago
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