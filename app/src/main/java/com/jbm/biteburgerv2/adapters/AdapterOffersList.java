package com.jbm.biteburgerv2.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;

import java.util.List;

public class AdapterOffersList extends BaseAdapter {

    private Context context;
    private List<Food> listItems;

    public AdapterOffersList(Context context, List<Food> listItems) {
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
            view = inflater.inflate(R.layout.foodoffers_list_design, viewGroup, false);
        }

        ImageView imageView = view.findViewById(R.id.imgOffer);
        TextView nameTextView = view.findViewById(R.id.offerName);
        TextView priceTextView = view.findViewById(R.id.offerPrice);

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


        return view;
    }
}
