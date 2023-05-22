package com.jbm.biteburgerv2.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.AcercaDeActivity;
import com.jbm.biteburgerv2.AuthActivity;
import com.jbm.biteburgerv2.ConseguirPuntosActivity;
import com.jbm.biteburgerv2.DatosPersonalesActivity;
import com.jbm.biteburgerv2.DireccionesActivity;
import com.jbm.biteburgerv2.MainActivity;
import com.jbm.biteburgerv2.SignUpActivity;
import com.jbm.biteburgerv2.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tvNombre = binding.textViewNombre;
        accountViewModel.getNombre().observe(getViewLifecycleOwner(), tvNombre::setText);

        final TextView tvPuntos = binding.textViewPuntos;
        accountViewModel.getPuntos().observe(getViewLifecycleOwner(), tvPuntos::setText);



        // LISTENERS PARA LOS TEXTOS/BOTONES
        LinearLayout linearLayout_conseguirPtos = binding.linearLayoutConseguirPtos;
        linearLayout_conseguirPtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComoConseguirPuntos();
            }
        });

        LinearLayout linearLayout_datosPersonales = binding.linearLayoutDatosPersonales;
        linearLayout_datosPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatosPersonalesActivity();
            }
        });

        LinearLayout linearLayout_direccionesEnvio = binding.linearLayoutDireccionesEnvio;
        linearLayout_direccionesEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDireccionesActivity();
            }
        });

        LinearLayout linearLayout_acercaDe = binding.linearLayoutAcercaDe;
        linearLayout_acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAcercaDeActivity();
            }
        });

        LinearLayout linearLayout_cerrarSesion = binding.linearLayoutCerrarSesion;
        linearLayout_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // MÉTODOS PARA EJECUTAR LAS OPCIONES
    public void startAcercaDeActivity() {
        Intent i = new Intent(getActivity(), AcercaDeActivity.class);
        startActivity(i);
    }

    public void startComoConseguirPuntos() {
        Intent i = new Intent(getActivity(), ConseguirPuntosActivity.class);
        startActivity(i);
    }

    public void startDatosPersonalesActivity() {
        Intent i = new Intent(getActivity(), DatosPersonalesActivity.class);
        startActivity(i);
    }

    public void startDireccionesActivity() {
        Intent i = new Intent(getActivity(), DireccionesActivity.class);
        startActivity(i);
    }

    public void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getActivity(), AuthActivity.class);
        startActivity(i);
    }
}