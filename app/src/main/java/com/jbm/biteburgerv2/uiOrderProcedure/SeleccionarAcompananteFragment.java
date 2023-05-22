package com.jbm.biteburgerv2.uiOrderProcedure;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.adapters.AdapterFoodMenuShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarAcompananteBinding;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarProductoPedidoBinding;
import com.jbm.biteburgerv2.listeners.OnFoodMenuShopListListener;
import com.jbm.biteburgerv2.listeners.OnFoodShopListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeleccionarAcompananteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeleccionarAcompananteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentSeleccionarAcompananteBinding binding;
    private OrderProcedureActivity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeleccionarAcompananteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeleccionarAcompananteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeleccionarAcompananteFragment newInstance(String param1, String param2) {
        SeleccionarAcompananteFragment fragment = new SeleccionarAcompananteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeleccionarAcompananteBinding.inflate(inflater, container, false);
        View fragmentView = getView();

        Bundle bundle = getArguments();
        ArrayList<Food> listFood = new ArrayList<Food>();
        String idPedido = "";
        if (bundle != null) {
            listFood = getArguments().getParcelableArrayList("menu");
            idPedido = bundle.getString("idPedido");
        }
        if (fragmentView != null) {
            fragmentView.invalidate();
        }

        AdapterFoodMenuShop adaptador = null;
        ListView listView = binding.listViewAcompanante;

        String categoriaProducto = "Snacks/entrantes";
        String finalCategoriaProducto = categoriaProducto;
        String finalIdPedido = idPedido;
        ArrayList<Food> finalListFood = listFood;
        FireBaseOperations.listTypeFood(categoriaProducto, adaptador, new OnFoodMenuShopListListener() {
            @Override
            public void onComplete(ArrayList<Food> foodList, AdapterFoodMenuShop adaptador) {
                adaptador = new AdapterFoodMenuShop(getContext(), foodList, finalCategoriaProducto, finalIdPedido, finalListFood);
                listView.setAdapter(adaptador);
            }
        });



        return binding.getRoot();
    }
}