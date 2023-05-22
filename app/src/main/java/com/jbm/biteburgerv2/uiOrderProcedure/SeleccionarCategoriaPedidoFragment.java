package com.jbm.biteburgerv2.uiOrderProcedure;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.databinding.FragmentSeleccionarCategoriaPedidoBinding;

public class SeleccionarCategoriaPedidoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentSeleccionarCategoriaPedidoBinding binding;
    private OrderProcedureActivity activity;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeleccionarCategoriaPedidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeleccionarCategoriaPedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeleccionarCategoriaPedidoFragment newInstance(String param1, String param2) {
        SeleccionarCategoriaPedidoFragment fragment = new SeleccionarCategoriaPedidoFragment();
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

        binding = FragmentSeleccionarCategoriaPedidoBinding.inflate(inflater, container, false);
        activity = (OrderProcedureActivity) getActivity();
        activity.isSeleccionarCategoriaActive = true;

        // Obtener el ID del pedido
        String idPedido = getActivity().getIntent().getStringExtra("idPedido");
        activity.idOrder = idPedido;


        // Actualizar importe pedido
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("orders")
                .document(idPedido)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        double currentTotalAmount = documentSnapshot.getDouble("total_amount");
                        activity.importePedido.setText(String.format("%.2f €", currentTotalAmount));
                        activity.txtPagar.setText("Revisar pedido");
                    }
                });



        /* --- LISTENERS BOTONES --- */
        binding.categoriaHamburguesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.isSeleccionarCategoriaActive = false;

                Bundle bundle = new Bundle();
                bundle.putString("categoria", "Hamburguesas");
                bundle.putString("idPedido", idPedido);

                Navigation.findNavController(v).navigate(R.id.action_seleccionarCategoriaPedidoFragment_to_seleccionarProductoPedidoFragment, bundle);

            }
        });
        binding.categoriaSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.isSeleccionarCategoriaActive = false;

                Bundle bundle = new Bundle();
                bundle.putString("categoria", "Snacks/entrantes");
                bundle.putString("idPedido", idPedido);

                Navigation.findNavController(v).navigate(R.id.action_seleccionarCategoriaPedidoFragment_to_seleccionarProductoPedidoFragment, bundle);

            }
        });
        binding.categoriaBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.isSeleccionarCategoriaActive = false;

                Bundle bundle = new Bundle();
                bundle.putString("categoria", "Bebidas");
                bundle.putString("idPedido", idPedido);

                Navigation.findNavController(v).navigate(R.id.action_seleccionarCategoriaPedidoFragment_to_seleccionarProductoPedidoFragment, bundle);

            }
        });
        binding.categoriaOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.isSeleccionarCategoriaActive = false;

                Bundle bundle = new Bundle();
                bundle.putString("categoria", "Ofertas");
                bundle.putString("idPedido", idPedido);

                Navigation.findNavController(v).navigate(R.id.action_seleccionarCategoriaPedidoFragment_to_seleccionarProductoPedidoFragment, bundle);

            }
        });

        /*activity.btnRevisarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear instancia del nuevo fragmento
                RevisarPedidoFragment fragmento = new RevisarPedidoFragment();

                // Obtener instancia del FragmentManager
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Iniciar transacción de fragmentos
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el nuevo fragmento
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);

                // Agregar la transacción a la pila de retroceso
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();


                //activity.isSeleccionarCategoriaActive = false;

                Intent i = new Intent(getActivity(), PaymentProcedureActivity.class);
                i.putExtra("idPedido", idPedido);
                startActivity(i);
            }
        });*/

        return binding.getRoot();
    }




}