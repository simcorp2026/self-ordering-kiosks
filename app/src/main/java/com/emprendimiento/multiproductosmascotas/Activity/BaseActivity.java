package com.emprendimiento.multiproductosmascotas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BaseActivity extends AppCompatActivity {

    // Tiempo sin tocar la pantalla antes de volver al inicio (tótem)
    private static final long TIEMPO_INACTIVIDAD_MS = 60_000;
    // El mensaje de éxito se oculta solo; el de error se queda hasta que el cliente lo toque
    private static final long TIEMPO_FEEDBACK_CORRECTO_MS = 2_500;

    // Pantallas donde no aplica el regreso automático al inicio (bienvenida y flujo de pago)
    private static final Set<Class<?>> PANTALLAS_SIN_INACTIVIDAD = new HashSet<>(Arrays.asList(
            Intro_Activity.class,
            CardActivity.class,
            CashActivity.class,
            PaymentMethodActivity_1.class,
            PaymentMethodActivity_2.class));

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    public String  TAG="serverus";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable alExpirarInactividad = this::irAlInicio;
    private final Runnable ocultarFeedbackPendiente = this::ocultarFeedback;
    private View feedbackActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reiniciarInactividad();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(alExpirarInactividad);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Cualquier toque reinicia el contador de inactividad
        reiniciarInactividad();
        return super.dispatchTouchEvent(ev);
    }

    // Las subclases pueden sobrescribirlo para desactivar el regreso automático
    protected boolean isInactividadActiva() {
        return !PANTALLAS_SIN_INACTIVIDAD.contains(getClass());
    }

    private void reiniciarInactividad() {
        handler.removeCallbacks(alExpirarInactividad);
        if (isInactividadActiva()) {
            handler.postDelayed(alExpirarInactividad, TIEMPO_INACTIVIDAD_MS);
        }
    }

    private void irAlInicio() {
        // Vacía el carrito para que el siguiente cliente no vea el pedido anterior
        new ManagmentCart(this).clearCart();
        ocultarFeedback();

        Intent intent = new Intent(this, Intro_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void hideSystemUI() {
        // Habilitar el modo de inmersión
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Ocultar las barras de navegación y de estado
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // Permite que el contenido sea traslapado por el sistema UI
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void showCustomToastCorrect(String frase) {
        mostrarFeedback(R.layout.custom_toast, frase, TIEMPO_FEEDBACK_CORRECTO_MS);
    }

    public void showCustomToastIncorrect(String frase) {
        mostrarFeedback(R.layout.custom_toast_incorrect, frase, 0);
    }

    // Muestra el mensaje centrado sobre la pantalla actual, con un velo que bloquea los toques de fondo
    private void mostrarFeedback(int layoutId, String frase, long duracionMs) {
        ocultarFeedback();

        FrameLayout capa = new FrameLayout(this);
        capa.setBackgroundColor(0xB3000000);
        capa.setOnClickListener(v -> ocultarFeedback());

        View tarjeta = LayoutInflater.from(this).inflate(layoutId, capa, false);
        TextView texto = tarjeta.findViewById(R.id.text);
        texto.setText(frase);
        capa.addView(tarjeta, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        ViewGroup raiz = findViewById(android.R.id.content);
        raiz.addView(capa, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        feedbackActual = capa;
        if (duracionMs > 0) {
            handler.postDelayed(ocultarFeedbackPendiente, duracionMs);
        }
    }

    private void ocultarFeedback() {
        handler.removeCallbacks(ocultarFeedbackPendiente);
        if (feedbackActual != null && feedbackActual.getParent() instanceof ViewGroup) {
            ((ViewGroup) feedbackActual.getParent()).removeView(feedbackActual);
        }
        feedbackActual = null;
    }
}
