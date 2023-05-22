package com.jbm.biteburgerv2.uiPaymentProcedure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.adapters.AdapterFoodMenuShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodSummary;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.data.FoodSummary;
import com.jbm.biteburgerv2.databinding.FragmentRevisarPedidoBinding;
import com.jbm.biteburgerv2.listeners.OnFoodShopListListener;
import com.jbm.biteburgerv2.listeners.OnFoodSummaryListListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RevisarPedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RevisarPedidoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentRevisarPedidoBinding binding;
    private OrderProcedureActivity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RevisarPedidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RevisarPedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RevisarPedidoFragment newInstance(String param1, String param2) {
        RevisarPedidoFragment fragment = new RevisarPedidoFragment();
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
        binding = FragmentRevisarPedidoBinding.inflate(inflater, container, false);
        activity = (OrderProcedureActivity) getActivity();
        View fragmentView = getView();

        if (fragmentView != null) {
            fragmentView.invalidate();
        }

        // Obtener el ID del pedido
        String idPedido = getActivity().getIntent().getStringExtra("idPedido");
        String uid = FirebaseAuth.getInstance().getUid();

        AdapterFoodSummary adaptador = null;
        ListView listView = binding.foodListSummary;

        FireBaseOperations.listSummaryOrder(uid, idPedido, adaptador, new OnFoodSummaryListListener() {
            @Override
            public void onComplete(ArrayList<FoodSummary> foodList, AdapterFoodSummary adaptador) {
                adaptador = new AdapterFoodSummary(getContext(), foodList, idPedido, activity);
                listView.setAdapter(adaptador);
            }
        });




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

                        CollectionReference collection = db.collection("customers")
                                .document(uid)
                                .collection("orders")
                                .document(idPedido)
                                .collection("order_lines");
                        FireBaseOperations.getNumDocuments(collection).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Integer numDocuments = task.getResult();
                                activity.lineasPedido = numDocuments;
                            } else {
                                // Error
                            }
                        });
                    }
                });

        return binding.getRoot();
    }
}