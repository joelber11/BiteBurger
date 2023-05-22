package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.adapters.AdapterAddressList;
import com.jbm.biteburgerv2.adapters.AdapterOrderSummary;
import com.jbm.biteburgerv2.data.OrderSummary;
import com.jbm.biteburgerv2.databinding.ActivityDireccionesBinding;
import com.jbm.biteburgerv2.databinding.ActivityVerPedidosBinding;
import com.jbm.biteburgerv2.listeners.OnGetOrdersListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

public class VerPedidosActivity extends AppCompatActivity {

    private ActivityVerPedidosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerPedidosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        AdapterOrderSummary adaptador = null;
        ListView lv = binding.ordersList;

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = binding.ordersToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        String uid = FirebaseAuth.getInstance().getUid();
        FireBaseOperations.getOrdersList(uid, adaptador, new OnGetOrdersListListener() {
            @Override
            public void onComplete(ArrayList<OrderSummary> orders, AdapterOrderSummary adaptador) {
                adaptador = new AdapterOrderSummary(getApplicationContext(), orders);
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