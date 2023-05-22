package com.jbm.biteburgerv2.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.listeners.OnAddToOrderOrderListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;
import com.jbm.biteburgerv2.uiOrderProcedure.SeleccionarProductoPedidoFragment;

import java.util.ArrayList;
import java.util.List;

public class AdapterFoodShop extends BaseAdapter {

    private Context context;
    private List<Food> listItems;
    private String categoriaProducto;
    private String idPedido;

    public AdapterFoodShop(Context context, List<Food> item, String categoriaProducto, String idPedido) {
        this.context = context;
        this.listItems = item;
        this.categoriaProducto = categoriaProducto;
        this.idPedido = idPedido;
    }

    public void clear(){listItems.clear();}

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.foodshop_list_design, viewGroup, false);
        }

        ImageView imageView = view.findViewById(R.id.imgFoodShop);
        TextView nameTextView = view.findViewById(R.id.foodNameShop);
        TextView priceTextView = view.findViewById(R.id.foodPriceShop);

        Food food = listItems.get(position);

        // Calculamos 15dp en px para redondear bordes
        int roundingRadius = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics()));

        Glide.with(context)
                .load(food.getImgUrl())
                .placeholder(R.drawable.ic_isotipo)
                .error(R.drawable.ic_isotipo)
                .transform(new CenterCrop(), new RoundedCorners(roundingRadius))
                .into(imageView);
        nameTextView.setText(food.getName());
        priceTextView.setText(String.format("%.2f €", food.getPrice()));


        /* -- LISTENER DE BOTÓN DE AÑADIR AL CARRITO -- */
        Button btnInsertarAlCarrito = view.findViewById(R.id.btnAddCartFoodShop);
        btnInsertarAlCarrito.setText("Añadir");
        btnInsertarAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Food productoSeleccionado = food;
                if(!categoriaProducto.equals("Hamburguesas")) {

                    ArrayList<Food> listFood = new ArrayList<>();
                    listFood.add(food);
                    FireBaseOperations.addToOrder(idPedido, FirebaseAuth.getInstance().getUid(), listFood, false, new OnAddToOrderOrderListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "Producto añadido al pedido", Toast.LENGTH_SHORT).show();

                            // Elimino todos los fragmentos de la pila y vuelvo al inicio del pedido
                            NavController navController = Navigation.findNavController(view);
                            navController.popBackStack(navController.getGraph().getStartDestination(), false);

                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "No se ha podido añadir al pedido", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    ArrayList<Food> listFood = new ArrayList<>();
                    listFood.add(0, food);

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("menu", listFood);
                    bundle.putString("idPedido", idPedido);
                    Navigation.findNavController(view).navigate(R.id.action_seleccionarProductoPedidoFragment_to_seleccionarSoloMenuHamburguesaFragment, bundle);
                }

            }
        });

        return view;
    }
}
