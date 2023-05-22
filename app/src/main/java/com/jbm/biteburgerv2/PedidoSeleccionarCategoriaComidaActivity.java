package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidoSeleccionarCategoriaComidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_seleccionar_categoria_comida);




    }

    public void startHamburguesasCategoriaPedido(View view) {
        String tipo = "Hamburguesas";
        startSeleccionarCategoriaPedido(tipo);
    }

    public void startEntrantesCategoriaPedido(View view) {
        String tipo = "Entrantes";
        startSeleccionarCategoriaPedido(tipo);
    }

    public void startBebidasCategoriaPedido(View view) {
        String tipo = "Bebidas";
        startSeleccionarCategoriaPedido(tipo);
    }

    public void startOfertasCategoriaPedido(View view) {
        String tipo = "Ofertas";
        startSeleccionarCategoriaPedido(tipo);
    }



    private void startSeleccionarCategoriaPedido(String tipo) {
        HashMap<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        String categoriaSeleccionada = tipos.get(tipo).toString();

        Intent intent = new Intent(this, PedidoSeleccionarProductoComidaActivity.class);
        intent.putExtra("categoriaProducto", categoriaSeleccionada);
        startActivity(intent);

    }

}