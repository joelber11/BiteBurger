package com.jbm.biteburgerv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.data.OrderSummary;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterOrderSummary extends BaseAdapter {

    private Context context;
    private List<OrderSummary> listItems;

    public AdapterOrderSummary(Context context, List<OrderSummary> listItems) {
        this.context = context;
        this.listItems = listItems;
    }


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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.ordersummary_list_design, viewGroup, false);
        }

        TextView fechaTextView = view.findViewById(R.id.dateOrder);
        TextView precioTextView = view.findViewById(R.id.priceOrder);
        TextView puntosTextView = view.findViewById(R.id.pointsOrder);

        OrderSummary orderSumm = listItems.get(position);

        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(orderSumm.getDate());
        fechaTextView.setText(fechaFormateada);

        precioTextView.setText(String.format("%.2f €", orderSumm.getPrice()));

        double ptosFormat = orderSumm.getPoints();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        puntosTextView.setText(decimalFormat.format(ptosFormat) + " ptos.");


        return view;
    }
}
