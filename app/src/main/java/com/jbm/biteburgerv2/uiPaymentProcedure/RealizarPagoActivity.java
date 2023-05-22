package com.jbm.biteburgerv2.uiPaymentProcedure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.PagoRealizadoActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.listeners.OnAddToOrderOrderListener;
import com.jbm.biteburgerv2.listeners.OnConfirmOrderListener;
import com.jbm.biteburgerv2.listeners.OnGetAddressListListener;
import com.jbm.biteburgerv2.listeners.OnGetAddressListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

public class RealizarPagoActivity extends AppCompatActivity {

    private int favAddressPosition = -1;
    private int selectedAddressPosition;
    private TextView direc1;
    private TextView direc2;
    private String uid;
    private String idOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        Intent intent = getIntent();
        idOrder = intent.getStringExtra("idOrder");
        uid = intent.getStringExtra("uid");
        double totAmount = intent.getDoubleExtra("totalAmount", 0);

        TextView total = this.findViewById(R.id.totalPagar);
        total.setText(String.format("%.2f €", totAmount));

        direc1 = this.findViewById(R.id.direc1TextView);
        direc2 = this.findViewById(R.id.direc2TextView);

        FireBaseOperations.getFavouriteAddress(uid, new OnGetAddressListener() {
            @Override
            public void onComplete(Address address) {
                direc1.setText("C/ " + address.getStreet() + ", " + address.getNumber());
                direc2.setText(address.getCity() + ", " + address.getPostCode());
            }
        });
        /* https://github.com/material-components/material-components-android/blob/master/docs/components/Menu.md#exposed-dropdown-menus */

        /* https://github.com/material-components/material-components-android/blob/master/docs/components/Dialog.md */

    }


    public void abrirDialogoDirecciones(View v) {
        FireBaseOperations.getAddressList(uid, new OnGetAddressListListener() {
            @Override
            public void onComplete(ArrayList<Address> address) {
                CharSequence[] choices = new CharSequence[address.size()];
                int i = 0;
                favAddressPosition = 0;
                for(Address a : address) {
                    choices[i] = "C/ " + a.getStreet() + ", " + a.getNumber() + ", " + a.getCity() + ", " + a.getPostCode();
                    if(a.isFavorite() && favAddressPosition < 0) {
                        favAddressPosition = i;
                        selectedAddressPosition = favAddressPosition;
                    }
                    i++;
                }

                AlertDialog materialAlertDialogBuilder = new MaterialAlertDialogBuilder(RealizarPagoActivity.this)
                        .setTitle("Escoge nueva dirección")
                        .setSingleChoiceItems(choices, selectedAddressPosition, (dialog, which) -> {
                            // Aquí puedes realizar acciones según la opción seleccionada
                        })
                        .setPositiveButton("Ok", (dialog, which) -> {
                            // Aquí puedes realizar acciones cuando se hace clic en el botón "Ok"
                            selectedAddressPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                            String direccion[] = choices[selectedAddressPosition].toString().split(", ");
                            direc1.setText(direccion[0] + ", " + direccion[1]);
                            direc2.setText(direccion[2] + ", " + direccion[3]);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            // Aquí puedes realizar acciones cuando se hace clic en el botón "Cancel"
                        })
                        .show();

            }
        });
    }

    public void efectuarPago(View view) {

        ProgressBar spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        FireBaseOperations.confirmOrder(idOrder, uid, new OnConfirmOrderListener() {
            @Override
            public void onSuccess(int points) {
                spinner.setVisibility(View.GONE);

                Intent i = new Intent(RealizarPagoActivity.this, PagoRealizadoActivity.class);
                i.putExtra("pts", points);
                i.putExtra("uid", uid);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {
                spinner.setVisibility(View.GONE);

                // Se muestra mensaje de error
                AlertDialog materialAlertDialogBuilder = new MaterialAlertDialogBuilder(RealizarPagoActivity.this)
                        .setTitle("Escoge nueva dirección")
                        .setMessage("Inténtelo más tarde...")
                        .setPositiveButton("Ok", (dialog, which) -> {

                        })
                        .show();
            }
        });
    }

}