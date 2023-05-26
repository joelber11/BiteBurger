package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.databinding.ActivityCrearDireccionBinding;
import com.jbm.biteburgerv2.databinding.ActivityDireccionesBinding;
import com.jbm.biteburgerv2.listeners.OnCreateUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

public class CrearDireccionActivity extends AppCompatActivity {

    private ActivityCrearDireccionBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearDireccionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent i = getIntent();
        uid = i.getStringExtra("uid");

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = binding.crearDireccionToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

    }

    public void crearDireccion(View view) {
        String direccion = binding.streetAddress.getText().toString();
        int numero = Integer.parseInt(binding.numberAddress.getText().toString());
        String piso = binding.floorStairsAddress.getText().toString();
        String poblacion = binding.cityAddress.getText().toString();
        int cp = Integer.parseInt(binding.postalCodeAddress.getText().toString());
        boolean favorita = binding.switchFavAddress.isChecked();
        Address address = new Address("", direccion.toUpperCase(), numero, piso.toUpperCase(), poblacion.toUpperCase(), cp, favorita);


        FireBaseOperations.createAddress(uid, address, new OnCreateUserListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Dirección añadida", Toast.LENGTH_SHORT).show();
                cerrarActivity();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "No se ha podido añadir.\nInténtelo más tarde", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cerrarActivity() {
        this.finish();
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