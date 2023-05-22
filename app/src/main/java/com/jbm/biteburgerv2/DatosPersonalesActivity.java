package com.jbm.biteburgerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.data.User;
import com.jbm.biteburgerv2.listeners.OnUpdateUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatosPersonalesActivity extends AppCompatActivity {

    static final String FORMATO_FECHA = "dd/MM/yyyy";
    static final String USER_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    EditText nombre, apellidos, fechaNac, email, numTelf;
    Switch comunicaciones;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        // Poner el action bar con la flecha de retroceso
        Toolbar myToolbar = findViewById(R.id.personalDataToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        // Obtengo instancia de Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Inicializo los elementos
        nombre = findViewById(R.id.nameAccount);
        apellidos = findViewById(R.id.surnameAccount);
        fechaNac = findViewById(R.id.birthdateAccount);
        email = findViewById(R.id.emailAccount);
        numTelf = findViewById(R.id.telephoneAccount);
        comunicaciones = findViewById(R.id.switch_Communications);


        // Rellenar datos
        DataOperations.getUser(USER_UID).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult();

                nombre.setText(DataOperations.capitalizeString(user.getName()));
                apellidos.setText(DataOperations.capitalizeString(user.getSurname()));
                fechaNac.setText(DataOperations.formatDate(user.getFechaNac(), FORMATO_FECHA));
                email.setText(user.getEmail());
                numTelf.setText(user.getNumTelef() + "");
                comunicaciones.setChecked(user.isCommunications());
            } else {
                Toast.makeText(getApplicationContext(), "No se han podido actualizar los datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btnActualizar(View view) throws ParseException {
        String name = nombre.getText().toString();
        String surname = apellidos.getText().toString();
        String fechaNacTxt = fechaNac.getText().toString();
        String userEmail = email.getText().toString();
        int nTelf = Integer.parseInt(numTelf.getText().toString());
        boolean communications = comunicaciones.isChecked();

        Date fechaNacDate = DataOperations.formatStringDate(fechaNacTxt, FORMATO_FECHA);

        //Llamada al método para actualizar el usuario
        User user = new User(name, surname, fechaNacDate, nTelf, userEmail, communications);
        FireBaseOperations.updateUser(USER_UID, user, new OnUpdateUserListener() {
            @Override
            public void onSuccess() {
                // Tarea completada correctamente
                Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                // Tarea falló
                System.out.println(e.toString());
                Toast.makeText(getApplicationContext(), "No se han podido actualizar los datos", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}