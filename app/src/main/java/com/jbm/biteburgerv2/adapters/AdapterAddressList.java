package com.jbm.biteburgerv2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbm.biteburgerv2.CrearDireccionActivity;
import com.jbm.biteburgerv2.DataOperations;
import com.jbm.biteburgerv2.DireccionesActivity;
import com.jbm.biteburgerv2.EditarDireccionActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.data.FoodSummary;
import com.jbm.biteburgerv2.listeners.OnDeleteOrderListener;
import com.jbm.biteburgerv2.listeners.OnUpdateAddressListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;
import java.util.List;

public class AdapterAddressList extends BaseAdapter {

    private Context context;
    private List<Address> listItems;
    private String uid;

    public AdapterAddressList(Context context, List<Address> item, String uid) {
        this.context = context;
        this.listItems = item;
        this.uid = uid;
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
        streetTextView.setText("C/ " + DataOperations.capitalizeString(address.getStreet()));
        if(address.getFloorStairs().length() == 0) {
            numberFloorTextView.setText("Número " + address.getNumber());
        } else {
            numberFloorTextView.setText("Número " + DataOperations.capitalizeString(address.getNumber() + ", " + address.getFloorStairs()));
        }
        cityTextView.setText(DataOperations.capitalizeString(address.getCity()) + ",  " + address.getPostCode());


        /* -- LISTENER DE BOTÓN DE MODIFICAR -- */
        Button btnEditarDireccion = view.findViewById(R.id.btnEditAddress);
        Button btnBorrarDireccion = view.findViewById(R.id.btnDeleteAddress);
        ImageView btnFavorito = view.findViewById(R.id.addressFavorite);

        btnEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditarDireccionActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("uid", uid);
                i.putExtra("address", address);
                i.putExtra("addressId", address.getId());
                context.startActivity(i);
            }
        });

        btnBorrarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.isFavorite()) {
                    Toast.makeText(context, "La dirección es favorita. No se puede borrar.", Toast.LENGTH_SHORT).show();
                } else {
                    FireBaseOperations.deleteAddress(uid, address.getId(), new OnDeleteOrderListener() {
                        @Override
                        public void onSuccess() {
                            List<Address> newAddressList = new ArrayList<>(listItems);
                            for (int i = 0; i < newAddressList.size(); i++) {
                                if (newAddressList.get(i).getId().equals(address.getId())) {
                                    newAddressList.remove(i);
                                    break;
                                }
                            }

                            updateData(newAddressList);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "No se ha podido borrar.\nInténtelo más tarde.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!address.isFavorite()) {
                    List<Address> newAddressList = new ArrayList<>(listItems);
                    for(Address address : newAddressList) {
                        address.setFavorite(false);
                    }

                    boolean nuevoEstado = true;
                    FireBaseOperations.updateFavouriteAddress(uid, address.getId(), new OnUpdateAddressListener() {
                        @Override
                        public void onSuccess() {
                            // Actualizar el estado de favorito del elemento de la lista
                            address.setFavorite(nuevoEstado);

                            // Notificar al adaptador que los datos han cambiado
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
            }
        });

        return view;
    }

    public void updateData(List<Address> newAddressList) {
        listItems.clear();
        listItems.addAll(newAddressList);
        notifyDataSetChanged();
    }

}
