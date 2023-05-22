package com.jbm.biteburgerv2.adapters;

import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.data.FoodSummary;
import com.jbm.biteburgerv2.listeners.OnAddToOrderOrderListener;
import com.jbm.biteburgerv2.listeners.OnDeleteOrderListener;
import com.jbm.biteburgerv2.listeners.OnUpdateQuantityOrderListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterFoodSummary extends BaseAdapter {

    private Context context;
    private List<FoodSummary> listItems;
    private String categoriaProducto;
    private String idPedido;
    private OrderProcedureActivity activity;
    private boolean pulsado = false;

    public AdapterFoodSummary(Context context, List<FoodSummary> item, String idPedido, OrderProcedureActivity activity) {
        this.context = context;
        this.listItems = item;
        this.categoriaProducto = categoriaProducto;
        this.idPedido = idPedido;
        this.activity = activity;
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
            view = inflater.inflate(R.layout.foodsummary_list_design, viewGroup, false);
        }

        TextView nameTextView = view.findViewById(R.id.foodNameSummary);
        TextView priceTextView = view.findViewById(R.id.foodPriceSummary);
        TextView detailsTextView = view.findViewById(R.id.detailMenuSummary);
        TextView quantityTextView = view.findViewById(R.id.foodQuantity);

        FoodSummary food = listItems.get(position);

        if(food.isMenu()) {
            nameTextView.setText("MENÚ " + food.getName1());
            detailsTextView.setText(food.getName2() + "\n" + food.getName3());
        } else {
            nameTextView.setText(food.getName1());
            detailsTextView.setText("");
        }
        quantityTextView.setText(food.getQuantity() + "");
        priceTextView.setText(String.format("%.2f €", food.getPrice()));



        /* -- LISTENER DE BOTÓN DE SUMAR/RESTAR CANTIDAD -- */
        Button btnInsertarCantidad = view.findViewById(R.id.btnAddQuantity);
        btnInsertarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodSummary productoSeleccionado = food;
                int nuevaCantidad = food.getQuantity() + 1;

                if (!pulsado) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    FireBaseOperations.updateQuantityOrder(idPedido, uid, productoSeleccionado, nuevaCantidad, new OnUpdateQuantityOrderListener() {
                        @Override
                        public void onSuccess() {
                            quantityTextView.setText(nuevaCantidad + "");
                            food.setQuantity(nuevaCantidad);

                            // Actualizar importe pedido
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("customers")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("orders")
                                    .document(idPedido)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            double currentTotalAmount = documentSnapshot.getDouble("total_amount");
                                            activity.importePedido.setText(String.format("%.2f €", currentTotalAmount));
                                        }
                                    });
                            pulsado = false;
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    pulsado = true;
                }

            }
        });

        Button btnQuitarCantidad = view.findViewById(R.id.btnDropQuantity);
        btnQuitarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodSummary productoSeleccionado = food;
                if(productoSeleccionado.getQuantity() >= 2) {
                    int nuevaCantidad = productoSeleccionado.getQuantity() - 1;

                    if (!pulsado) {
                        String uid = FirebaseAuth.getInstance().getUid();
                        FireBaseOperations.updateQuantityOrder(idPedido, uid, productoSeleccionado, nuevaCantidad, new OnUpdateQuantityOrderListener() {
                            @Override
                            public void onSuccess() {
                                quantityTextView.setText(nuevaCantidad + "");
                                productoSeleccionado.setQuantity(nuevaCantidad);

                                // Actualizar importe pedido
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("customers")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("orders")
                                        .document(idPedido)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                double currentTotalAmount = documentSnapshot.getDouble("total_amount");
                                                activity.importePedido.setText(String.format("%.2f €", currentTotalAmount));
                                            }
                                        });
                                pulsado = false;
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        pulsado = true;
                    }

                }
            }
        });

        Button btnQuitarProducto = view.findViewById(R.id.btnDropFood);
        btnQuitarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodSummary productoSeleccionado = food;

                if (!pulsado) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    FireBaseOperations.deleteLineOrder(productoSeleccionado, idPedido, uid, new OnDeleteOrderListener() {
                        @Override
                        public void onSuccess() {

                            List<FoodSummary> newFoodList = new ArrayList<>(listItems);
                            for (int i = 0; i < newFoodList.size(); i++) {
                                if (newFoodList.get(i).getLineaId().equals(productoSeleccionado.getLineaId())) {
                                    newFoodList.remove(i);
                                    break;
                                }
                            }

                            // Actualizar importe pedido
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("customers")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("orders")
                                    .document(idPedido)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            double currentTotalAmount = documentSnapshot.getDouble("total_amount");
                                            activity.importePedido.setText(String.format("%.2f €", currentTotalAmount));
                                        }
                                    });

                            // Actualizar los datos en el adaptador y notificar al ListView
                            updateData(newFoodList);
                            pulsado = false;
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    pulsado = true;
                }

            }
        });


        return view;
    }

    public void updateData(List<FoodSummary> newFoodList) {
        listItems.clear();
        listItems.addAll(newFoodList);
        notifyDataSetChanged();
    }

}
