package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.adapters.AdapterAddressList;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.databinding.ActivityDireccionesBinding;
import com.jbm.biteburgerv2.listeners.OnAddressListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;
import com.jbm.biteburgerv2.uiPaymentProcedure.RealizarPagoActivity;

import java.util.ArrayList;

public class DireccionesActivity extends AppCompatActivity {

    private ActivityDireccionesBinding binding;
    private AdapterAddressList adaptador;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDireccionesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String uid = FirebaseAuth.getInstance().getUid();

        adaptador = new AdapterAddressList(getApplicationContext(), new ArrayList<>(), uid);
        lv = binding.directionsList;

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = binding.addressToolbar;
        //Toolbar myToolbar = findViewById(R.id.addressToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);


        FireBaseOperations.listAddress(uid, adaptador, new OnAddressListListener() {
            @Override
            public void onComplete(ArrayList<Address> addressList, AdapterAddressList adaptador) {
                adaptador = new AdapterAddressList(getApplicationContext(), addressList, uid);
                lv.setAdapter(adaptador);
            }
        });


    }

    public void abrirCrearDireccion(View view) {
        String uid = FirebaseAuth.getInstance().getUid();

        Intent i = new Intent(DireccionesActivity.this, CrearDireccionActivity.class);
        i.putExtra("uid", uid);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cuando vuelve a estar en primer plano el activity, recarga el listView
        String uid = FirebaseAuth.getInstance().getUid();
        FireBaseOperations.listAddress(uid, adaptador, new OnAddressListListener() {
            @Override
            public void onComplete(ArrayList<Address> addressList, AdapterAddressList adaptador) {
                adaptador = new AdapterAddressList(getApplicationContext(), addressList, uid);
                lv.setAdapter(adaptador);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}