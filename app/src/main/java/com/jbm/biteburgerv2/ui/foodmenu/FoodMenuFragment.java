package com.jbm.biteburgerv2.ui.foodmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.databinding.FragmentFoodmenuBinding;
import com.jbm.biteburgerv2.listeners.OnFoodListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

public class FoodMenuFragment extends Fragment {

    private FragmentFoodmenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FoodMenuViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FoodMenuViewModel.class);

        AdapterFoodMenu adaptador = null;

        binding = FragmentFoodmenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final ListView lv = binding.lista1;




        /* -- RELLENAR EL LIST VIEW -- */

        // Asignar el adaptador de lista al AutoCompleteTextView
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView);

        // Obtener el conjunto de datos
        String[] menu = getResources().getStringArray(R.array.foodmenu);

        // Crear el adaptador de lista
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, menu);

        // Asigna el ArrayAdapter al AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter2);

        autoCompleteTextView.setSelection(0);
        //autoCompleteTextView.setText(menu[0]);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Acción a realizar cuando se selecciona un elemento del Spinner
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                FireBaseOperations.listTypeFood(selectedItem, adaptador, new OnFoodListListener() {
                    @Override
                    public void onComplete(ArrayList<Food> foodList, AdapterFoodMenu adaptador) {
                        adaptador = new AdapterFoodMenu(getContext(), foodList);
                        lv.setAdapter(adaptador);
                    }
                });
            }

        });






        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}