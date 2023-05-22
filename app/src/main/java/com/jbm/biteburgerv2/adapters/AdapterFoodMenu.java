package com.jbm.biteburgerv2.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;

import java.util.List;

public class AdapterFoodMenu extends BaseAdapter {

    private Context context;
    private List<Food> listItems;

    public AdapterFoodMenu(Context context, List<Food> item) {
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
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.foodmenu_list_design, viewGroup, false);
        }

        ImageView imageView = view.findViewById(R.id.imgFood);
        TextView nameTextView = view.findViewById(R.id.foodName);
        TextView priceTextView = view.findViewById(R.id.foodPrice);

        Food food = listItems.get(position);

        // Calculamos 15dp en px para redondear bordes
        int roundingRadius = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics()));

        Glide.with(context)
                .load(food.getImgUrl())
                .placeholder(R.drawable.ic_isotipo)
                .error(R.drawable.ic_isotipo)
                .transform(new CenterCrop(), new RoundedCorners(roundingRadius))
                .into(imageView);
        nameTextView.setText(food.getName());
        priceTextView.setText(String.format("%.2f €", food.getPrice()));


        /* -- LISTENER DE BOTÓN DE DETALLES -- */
        Button btnDetallesComida = view.findViewById(R.id.btnSeeDetailsFood);
        Dialog detailsFoodDialog = new Dialog(context);
        detailsFoodDialog.setContentView(R.layout.popup_food_details);

        ImageView btnCloseDetailsFoodDialog = detailsFoodDialog.findViewById(R.id.btnCloseFoodDetails);
        ImageView imgDetailsImageView = detailsFoodDialog.findViewById(R.id.detailsFoodImage);
        TextView nameDetailsTextView = detailsFoodDialog.findViewById(R.id.detailsFoodName);
        TextView descDetailsTextView = detailsFoodDialog.findViewById(R.id.detailsFoodDescription);
        TextView priceDetailsTextView = detailsFoodDialog.findViewById(R.id.detailsFoodPrice);

        btnDetallesComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Listener para cerrar el popup desde la X del popup
                btnCloseDetailsFoodDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailsFoodDialog.dismiss();
                    }
                });

                // Rellenar los datos del popup
                detailsFoodDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Glide.with(context)
                        .load(food.getImgUrl())
                        .placeholder(R.drawable.ic_isotipo)
                        .error(R.drawable.ic_isotipo)
                        .transform(new CenterCrop(), new RoundedCorners(roundingRadius))
                        .into(imgDetailsImageView);
                nameDetailsTextView.setText(food.getName());
                descDetailsTextView.setText(food.getDescription());
                priceDetailsTextView.setText(String.format("%.2f €", food.getPrice()));

                detailsFoodDialog.show();
            }
        });

        return view;
    }
}
