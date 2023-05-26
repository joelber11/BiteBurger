package com.jbm.biteburgerv2;

import static com.jbm.biteburgerv2.DataOperations.getUser;

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
import com.jbm.biteburgerv2.listeners.OnLoginUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

import java.util.List;

public class AuthActivity extends AppCompatActivity {

    EditText email, passwd;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_BiteBurgerV2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailEditText);
        passwd = findViewById(R.id.passwordEditText);


    }

    // METODOS DE LOS BOTONES
    public void startSignUpActivity(View view) {
        Intent i = new Intent(AuthActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    public void loginExistingUser(View view) {
        String emailUser = email.getText().toString().trim();
        String passUser = passwd.getText().toString().trim();

        if (emailUser.isEmpty() || passUser.isEmpty()){
            Toast.makeText(AuthActivity.this, "Rellenar los campos obligatorios", Toast.LENGTH_SHORT).show();
        } else {
            FireBaseOperations.loginUser(emailUser, passUser, new OnLoginUserListener() {
                @Override
                public void onSuccess() {
                    // Ha iniciado sesión correctamente

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

                    Intent i = new Intent(AuthActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

                @Override
                public void onFailure(Exception e) {
                    // No ha podido iniciar sesión correctamente
                    Toast.makeText(AuthActivity.this, "No se ha podido iniciar sesión correctamente.", Toast.LENGTH_LONG).show();
                }
            });

        }
    }




    // MÉTODOS PARA FIREBASE
    private void loginUser(String userEmail, String userPasswd) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPasswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent i = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AuthActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }

}
