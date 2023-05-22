package com.jbm.biteburgerv2.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jbm.biteburgerv2.AcercaDeActivity;
import com.jbm.biteburgerv2.ConseguirPuntosActivity;
import com.jbm.biteburgerv2.DataOperations;
import com.jbm.biteburgerv2.OrderProcedureActivity;
import com.jbm.biteburgerv2.PedidoSeleccionarCategoriaComidaActivity;
import com.jbm.biteburgerv2.R;
import com.jbm.biteburgerv2.VerPedidosActivity;
import com.jbm.biteburgerv2.databinding.FragmentHomeBinding;
import com.jbm.biteburgerv2.listeners.OnCreateOrderListener;
import com.jbm.biteburgerv2.listeners.OnDeleteOrderListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView userTextView = binding.homeNombre;
        homeViewModel.getUserName().observe(getViewLifecycleOwner(), userTextView::setText);
        final TextView pointsTextView = binding.homePuntos;
        homeViewModel.getUserPoints().observe(getViewLifecycleOwner(), pointsTextView::setText);


        // LISTENERS PARA LOS TEXTOS/BOTONES
        LinearLayout linearLayout_verQR = binding.linearLayoutVerQR;
        Dialog detailsQRuser = new Dialog(getContext());
        detailsQRuser.setContentView(R.layout.popup_qr_user);


        ImageView btnCloseDetailsQRuserDialog = detailsQRuser.findViewById(R.id.btnCloseQRuser);
        ImageView qrUserImage = detailsQRuser.findViewById(R.id.qrUser);
        TextView userIDtextView = detailsQRuser.findViewById(R.id.userID);


        linearLayout_verQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Listener para cerrar el popup desde la X del popup
                btnCloseDetailsQRuserDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailsQRuser.dismiss();
                    }
                });

                // Rellenar los datos del popup
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String numUserBBid = DataOperations.hashToNumber(uid) + "";
                Bitmap bitmap = null;

                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.encodeBitmap(numUserBBid, BarcodeFormat.QR_CODE, 150, 150);
                    qrUserImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                detailsQRuser.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                userIDtextView.setText(numUserBBid);


                detailsQRuser.show();
            }
        });


        Button btnCrearPedido = binding.btnHacerPedido;
        btnCrearPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getUid();
                FireBaseOperations.deleteFailOrders(uid, new OnDeleteOrderListener() {
                    @Override
                    public void onSuccess() {
                        FireBaseOperations.createOrder(UID, new OnCreateOrderListener() {
                            @Override
                            public void onSuccess(String docId) {
                                if (docId != null) {
                                    Intent i = new Intent(getActivity(), OrderProcedureActivity.class);
                                    i.putExtra("idPedido", docId);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("\nNo se ha podido crear el pedido\n");
                            }
                        });

                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("\nNo se ha podido crear el pedido\n");
                    }
                });




            }
        });


        Button btnVerPedidos = binding.btnVerPedidos;
        btnVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), VerPedidosActivity.class);
                startActivity(i);
            }
        });




        // Eliminar junto con su xml
        final TextView mailTextView = binding.emailTextView;
        homeViewModel.getUserEmail().observe(getViewLifecycleOwner(), mailTextView::setText);
        final TextView provTextView = binding.providerTextView;
        homeViewModel.getUserProvider().observe(getViewLifecycleOwner(), provTextView::setText);

        return root;
    }



    // MÉTODOS PARA EJECUTAR LAS OPCIONES
    public void startAcercaDeActivity() {
        Intent i = new Intent(getActivity(), AcercaDeActivity.class);
        startActivity(i);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}