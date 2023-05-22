package com.jbm.biteburgerv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.uiOrderProcedure.SeleccionarCategoriaPedidoFragment;
import com.jbm.biteburgerv2.uiPaymentProcedure.RealizarPagoActivity;
import com.jbm.biteburgerv2.uiPaymentProcedure.RevisarPedidoFragment;

import java.util.HashMap;
import java.util.Map;

public class OrderProcedureActivity extends AppCompatActivity {

    public TextView importePedido;
    public TextView txtPagar;
    public LinearLayout btnRevisarPedido;
    public int lineasPedido = 0;
    public String idOrder;
    public boolean isSeleccionarCategoriaActive = false;
    public boolean isRevisarPedidoActive = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_procedure);

        // Obtener referencia a importePedido
        importePedido = findViewById(R.id.importePedido);
        txtPagar = findViewById(R.id.textView31);
        btnRevisarPedido = findViewById(R.id.linearLayoutBtnRevisarPedido);


        btnRevisarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSeleccionarCategoriaActive = false;

                //System.out.println("   --> " + lineasPedido);
                /*if(lineasPedido == 0) {
                    return;
                }*/

                if(!isRevisarPedidoActive) {
                    txtPagar.setText("Realizar pago");
                    isRevisarPedidoActive = true;

                    // Crear instancia del nuevo fragmento
                    RevisarPedidoFragment fragmento = new RevisarPedidoFragment();

                    // Obtener instancia del FragmentManager
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    // Iniciar transacción de fragmentos
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Reemplazar el fragmento actual por el nuevo fragmento
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);

                    // Agregar la transacción a la pila de retroceso
                    fragmentTransaction.addToBackStack(null);

                    // Confirmar la transacción
                    fragmentTransaction.commit();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String uid = FirebaseAuth.getInstance().getUid();
                    db.collection("customers")
                            .document(uid)
                            .collection("orders")
                            .document(idOrder)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    double currentTotalAmount = documentSnapshot.getDouble("total_amount");

                                    int numeroDecimales = 2;
                                    double factor = Math.pow(10, numeroDecimales);
                                    double totalRedondeado = Math.floor(currentTotalAmount * factor) / factor;

                                    // Actualizo el valor del campo "total_amount" en Firestore
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("total_amount", totalRedondeado);
                                    db.collection("customers")
                                            .document(uid)
                                            .collection("orders")
                                            .document(idOrder)
                                            .update(updates)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent i = new Intent(OrderProcedureActivity.this, RealizarPagoActivity.class);
                                                    i.putExtra("idOrder", idOrder);
                                                    i.putExtra("totalAmount", totalRedondeado);
                                                    i.putExtra("uid", uid);
                                                    startActivity(i);
                                                }
                                            });
                                }
                            });

                }



            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if(isRevisarPedidoActive) {
            isRevisarPedidoActive = false;
        }

        if (isSeleccionarCategoriaActive){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cancelar pedido");
            builder.setMessage("¿Estás seguro de que quieres cancelar el pedido?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //cancelOrder();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            super.onBackPressed();
        }

    }



}