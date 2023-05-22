package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.databinding.ActivityDireccionesBinding;
import com.jbm.biteburgerv2.databinding.ActivityPedidoSeleccionarProductoComidaBinding;
import com.jbm.biteburgerv2.listeners.OnFoodListListener;
import com.jbm.biteburgerv2.listeners.OnFoodShopListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;
import java.util.List;

public class PedidoSeleccionarProductoComidaActivity extends AppCompatActivity {

    private ActivityPedidoSeleccionarProductoComidaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPedidoSeleccionarProductoComidaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_pedido_seleccionar_producto_comida);

        AdapterFoodShop adaptador = null;
        //ListView listView = findViewById(R.id.listView2);
        ListView listView = binding.listView2;

        String categoriaProducto = getIntent().getStringExtra("categoriaProducto");
        FireBaseOperations.listTypeFood(categoriaProducto, adaptador, new OnFoodShopListListener() {
            @Override
            public void onComplete(ArrayList<Food> foodList, AdapterFoodShop adaptador) {
                /*adaptador = new AdapterFoodShop(getApplicationContext(), foodList, categoriaProducto);
                listView.setAdapter(adaptador);*/
            }
        });



    }


    public static void InsertarProductoEnPpedido(Food producto) {
        PedidoSeleccionarProductoComidaActivity activity = new PedidoSeleccionarProductoComidaActivity();
        activity.VolverSeleccionarCategoria();
    }

    private void VolverSeleccionarCategoria() {
        /*Intent intent = new Intent(this, PedidoSeleccionarCategoriaComidaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        this.finish();
    }

}