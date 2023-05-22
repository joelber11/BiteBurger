package com.jbm.biteburgerv2.uiOrderProcedure;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarCategoriaPedidoBinding;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarSoloMenuHamburguesaBinding;
import com.jbm.biteburgerv2.listeners.OnAddToOrderOrderListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

public class SeleccionarSoloMenuHamburguesaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private FragmentSeleccionarSoloMenuHamburguesaBinding binding;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeleccionarSoloMenuHamburguesaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeleccionarSoloMenuHamburguesaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeleccionarSoloMenuHamburguesaFragment newInstance(String param1, String param2) {
        SeleccionarSoloMenuHamburguesaFragment fragment = new SeleccionarSoloMenuHamburguesaFragment();
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
        binding = FragmentSeleccionarSoloMenuHamburguesaBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        ArrayList<Food> listFood = new ArrayList<Food>(3);
        String idPedido = "";
        if (bundle != null) {
            listFood = getArguments().getParcelableArrayList("menu");
            idPedido = bundle.getString("idPedido");
        }

        String finalIdPedido = idPedido;
        ArrayList<Food> finalListFood = listFood;
        binding.opcionSola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseOperations.addToOrder(finalIdPedido, FirebaseAuth.getInstance().getUid(), finalListFood, false, new OnAddToOrderOrderListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Producto añadido al pedido", Toast.LENGTH_SHORT).show();

                        // Elimino todos los fragmentos de la pila y vuelvo al inicio del pedido
                        NavController navController = Navigation.findNavController(v);
                        navController.popBackStack(navController.getGraph().getStartDestination(), false);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), "No se ha podido añadir al pedido", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.opcionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("menu", finalListFood);
                bundle.putString("idPedido", finalIdPedido);
                Navigation.findNavController(v).navigate(R.id.action_seleccionarSoloMenuHamburguesaFragment_to_seleccionarAcompananteFragment, bundle);
            }
        });


        return binding.getRoot();
    }
}