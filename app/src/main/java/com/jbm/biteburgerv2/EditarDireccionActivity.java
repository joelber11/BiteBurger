package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.databinding.ActivityCrearDireccionBinding;
import com.jbm.biteburgerv2.databinding.ActivityEditarDireccionBinding;
import com.jbm.biteburgerv2.listeners.OnCreateUserListener;
import com.jbm.biteburgerv2.listeners.OnUpdateAddressListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

public class EditarDireccionActivity extends AppCompatActivity {

    private ActivityEditarDireccionBinding binding;
    private Address address;
    private String addressId;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarDireccionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        addressId = i.getStringExtra("addressId");
        address = (Address) i.getSerializableExtra("address");

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = binding.editarDireccionToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        // Rellenar los campos
        binding.streetAddressEdit.setText(address.getStreet());
        binding.numberAddressEdit.setText(address.getNumber() + "");
        binding.floorStairsAddressEdit.setText(address.getFloorStairs());
        binding.cityAddressEdit.setText(address.getCity());
        binding.postalCodeAddressEdit.setText(address.getPostCode() + "");
        binding.switchFavAddressEdit.setChecked(address.isFavorite());
        if(address.isFavorite()) {
            binding.switchFavAddressEdit.setEnabled(false);
        }

    }

    public void editarDireccion(View view) {
        String direccion = binding.streetAddressEdit.getText().toString();
        int numero = Integer.parseInt(binding.numberAddressEdit.getText().toString());
        String piso = binding.floorStairsAddressEdit.getText().toString();
        String poblacion = binding.cityAddressEdit.getText().toString();
        int cp = Integer.parseInt(binding.postalCodeAddressEdit.getText().toString());
        boolean favorita = binding.switchFavAddressEdit.isChecked();
        Address address = new Address("", direccion.toUpperCase(), numero, piso.toUpperCase(), poblacion.toUpperCase(), cp, favorita);


        FireBaseOperations.updateAddress(uid, addressId, address, new OnUpdateAddressListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Cambios guardados.", Toast.LENGTH_SHORT).show();
                cerrarActivity();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "No se ha podido actualizar.\nInténtelo más tarde.", Toast.LENGTH_SHORT).show();
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