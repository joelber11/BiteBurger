package com.jbm.biteburgerv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Address;

import java.util.List;

public class AdapterAddressList extends BaseAdapter {

    private Context context;
    private List<Address> listItems;

    public AdapterAddressList(Context context, List<Address> item) {
        this.context = context;
        this.listItems = item;
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
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.address_list_design, viewGroup, false);
        }

        // Declarar los textView que haya en la "tarjeta"
        ImageView favorite = view.findViewById(R.id.addressFavorite);
        TextView streetTextView = view.findViewById(R.id.addressStreet);
        TextView numberFloorTextView = view.findViewById(R.id.addressNumberFloor);
        TextView cityTextView = view.findViewById(R.id.addressCity);

        Address address = listItems.get(position);

        // Lo "inicializamos" dandole valor false
        favorite.setSelected(false);
        favorite.setImageResource(R.drawable.address_favourite_switch);

        // Damos valor a cada elemento de la tarjeta
        if(address.isFavorite()) {
            favorite.setSelected(true);
            favorite.setImageResource(R.drawable.address_favourite_switch);
        }
        streetTextView.setText("C/ " + address.getStreet());
        if(address.getFloorStairs().length() == 0) {
            numberFloorTextView.setText("Número " + address.getNumber());
        } else {
            numberFloorTextView.setText("Número " + address.getNumber() + ", " + address.getFloorStairs());
        }
        cityTextView.setText(address.getCity() + ",  " + address.getPostCode());


        /* -- LISTENER DE BOTÓN DE MODIFICAR -- */
        Button btnEditarDireccion = view.findViewById(R.id.btnEditAddress);
        Button btnBorrarDireccion = view.findViewById(R.id.btnDeleteAddress);

        btnEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Editar direccion", Toast.LENGTH_SHORT).show();
            }
        });

        btnBorrarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Borrar direccion", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
