package com.jbm.biteburgerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jbm.biteburgerv2.listeners.OnAddToOrderOrderListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class PagoRealizadoActivity extends AppCompatActivity {

    private int puntos;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_realizado);

        Intent intent = getIntent();
        puntos = intent.getIntExtra("pts", 0);
        uid = intent.getStringExtra("uid");

        double ptosFormat = puntos;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String puntosFormateado = decimalFormat.format(ptosFormat);


        TextView ptosObtenidos = this.findViewById(R.id.ptsObtenidos);
        ptosObtenidos.setText(puntosFormateado);

    }

    public void volverInicio(View view) {
        FireBaseOperations.updateUserPoints(uid, puntos, new OnAddToOrderOrderListener() {
            @Override
            public void onSuccess() {
                // Obtener el contexto actual
                Context context = view.getContext();

                // Obtener la clase del activity actual
                Class<? extends Activity> currentActivityClass = (Class<? extends Activity>) context.getClass();

                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
                for (ActivityManager.AppTask appTask : appTasks) {
                    ActivityManager.RecentTaskInfo taskInfo = appTask.getTaskInfo();
                    ComponentName componentName = taskInfo.baseActivity;
                    String packageName = componentName.getPackageName();
                    if (!packageName.equals(context.getPackageName()) || !componentName.getClassName().equals(currentActivityClass.getName())) {
                        appTask.finishAndRemoveTask();
                    }
                }

                Intent i = new Intent(PagoRealizadoActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        // No realizar ninguna acción al pulsar el botón de retroceso
    }

}