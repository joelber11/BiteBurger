package com.jbm.biteburgerv2.uiOrderProcedure;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.PedidoSeleccionarProductoComidaActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarProductoPedidoBinding;
import com.jbm.biteburgerv2.listeners.OnFoodShopListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeleccionarProductoPedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeleccionarProductoPedidoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentSeleccionarProductoPedidoBinding binding;
    private OrderProcedureActivity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeleccionarProductoPedidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeleccionarProductoPedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeleccionarProductoPedidoFragment newInstance(String param1, String param2) {
        SeleccionarProductoPedidoFragment fragment = new SeleccionarProductoPedidoFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderProcedureActivity) {
            activity = (OrderProcedureActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar la interfaz MiActividad");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSeleccionarProductoPedidoBinding.inflate(inflater, container, false);
        View fragmentView = getView();

        Bundle bundle = getArguments();
        String categoriaProducto = "";
        String idPedido = "";
        if (bundle != null) {
            categoriaProducto = bundle.getString("categoria");
            idPedido = bundle.getString("idPedido");
        }
        if (fragmentView != null) {
            fragmentView.invalidate();
        }

        AdapterFoodShop adaptador = null;
        ListView listView = binding.listViewProductos;

        String finalCategoriaProducto = categoriaProducto;
        String finalIdPedido = idPedido;
        FireBaseOperations.listTypeFood(categoriaProducto, adaptador, new OnFoodShopListListener() {
            @Override
            public void onComplete(ArrayList<Food> foodList, AdapterFoodShop adaptador) {
                adaptador = new AdapterFoodShop(getContext(), foodList, finalCategoriaProducto, finalIdPedido);
                listView.setAdapter(adaptador);
            }
        });


        return binding.getRoot();
    }




}