package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.adapters.AdapterAddressList;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.databinding.ActivityDireccionesBinding;
import com.jbm.biteburgerv2.listeners.OnAddressListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

public class DireccionesActivity extends AppCompatActivity {

    private ActivityDireccionesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDireccionesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*binding = ActivityDireccionesBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_direcciones);*/


        AdapterAddressList adaptador = null;
        ListView lv = binding.directionsList;
        //ListView lv = findViewById(R.id.directionsList);

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = binding.addressToolbar;
        //Toolbar myToolbar = findViewById(R.id.addressToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        String uid = FirebaseAuth.getInstance().getUid();
        FireBaseOperations.listAddress(uid, adaptador, new OnAddressListListener() {
            @Override
            public void onComplete(ArrayList<Address> addressList, AdapterAddressList adaptador) {
                adaptador = new AdapterAddressList(getApplicationContext(), addressList);
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