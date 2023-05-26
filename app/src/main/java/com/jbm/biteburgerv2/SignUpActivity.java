package com.jbm.biteburgerv2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jbm.biteburgerv2.data.User;
import com.jbm.biteburgerv2.listeners.OnCreateUserListener;
import com.jbm.biteburgerv2.listeners.OnLoginUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText email, passwd, nombre, apellidos, fechaNac;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailEditText);
        passwd = findViewById(R.id.passwordEditText);
        nombre = findViewById(R.id.nameEditText);
        apellidos = findViewById(R.id.surnameEditText);
        fechaNac = findViewById(R.id.birhtDateEditText);

    }


    //MÉTODOS BOTONES
    public void createNewUser(View view) {
        int err = 0;

        String emailUser = email.getText().toString().trim();
        String passUser = passwd.getText().toString().trim();

        if (emailUser.isEmpty() || passUser.isEmpty()){
            Toast.makeText(SignUpActivity.this, "Rellenar los datos", Toast.LENGTH_SHORT).show();
        } else {

            if(!DataOperations.isValidEmail(emailUser)) {
                Toast.makeText(SignUpActivity.this, "Correo electrónico inexistente", Toast.LENGTH_SHORT).show();
                err++;
            }

            if(!DataOperations.isSecurePasswd(passUser)) {
                Toast.makeText(SignUpActivity.this, "Contraseña poco segura", Toast.LENGTH_SHORT).show();
                err++;
            }

            // Si no hay errores, creamos al usuario
            //if(err == 0) createUser(emailUser, passUser);
            if(err == 0) createUser(view, emailUser, passUser, nombre.getText().toString(), apellidos.getText().toString(), fechaNac.getText().toString());
        }
    }


    // MÉTODOS PARA FIREBASE
    public void createUser(View view, String userEmail, String userPasswd, String name, String surname, String fechaNacTxt) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPasswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Obtengo su UID y creo su documento en Firestore
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Creo el objeto tipo User
                            Date fechaNacDate = null;
                            try {
                                fechaNacDate = DataOperations.formatStringDate(fechaNacTxt, "dd/MM/yyyy");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            User userObj = new User(name, surname, 0, fechaNacDate, 0, userEmail, false);

                            // Crear su documento en la BD
                            FireBaseOperations.createDataUser(uid, userObj, new OnCreateUserListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(SignUpActivity.this, "Cuenta creada", Toast.LENGTH_LONG).show();

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

                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) user.delete();
                                    Toast.makeText(SignUpActivity.this, "Error al crear la cuenta. Inténtelo más tarde", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error al crear la cuenta. Inténtelo más tarde", Toast.LENGTH_LONG).show();
                    }
                });




    }

    public void createUser(String userEmail, String userPasswd) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPasswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FireBaseOperations.loginUser(userEmail, userPasswd, new OnLoginUserListener() {
                                @Override
                                public void onSuccess() {
                                    // Creo su documento en Firestore
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    String name = nombre.getText().toString();
                                    String surname = apellidos.getText().toString();
                                    String fechaNacTxt = fechaNac.getText().toString();
                                    String userEmail = email.getText().toString();

                                    Date fechaNacDate = null;
                                    try {
                                        fechaNacDate = DataOperations.formatStringDate(fechaNacTxt, "dd/MM/yyyy");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    User userObj = new User(uid, name, surname, 0, fechaNacDate, 0, userEmail);

                                    // Crear su documento en la BD
                                    FireBaseOperations.createDataUser(uid, userObj, new OnCreateUserListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(SignUpActivity.this, "Cuenta creada", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(i);
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) user.delete();
                                            Toast.makeText(SignUpActivity.this, "Error al crear la cuenta. Inténtelo más tarde", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Error al crear la cuenta. Inténtelo más tarde", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error al crear la cuenta. Inténtelo más tarde", Toast.LENGTH_LONG).show();
                    }
                });
    }
}