package com.jbm.biteburgerv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jbm.biteburgerv2.data.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataOperations {

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isSecurePasswd(String password) {
        // La contraseña debe tener al menos 8 caracteres de longitud
        if (password.length() < 8) {
            return false;
        }

        // La contraseña debe contener al menos un número
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // La contraseña debe contener al menos una letra mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // La contraseña debe contener al menos una letra minúscula
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // La contraseña debe contener al menos un carácter especial
        if (!password.matches(".*[!@#$%^&*+=_?-].*")) {
            return false;
        }

        // La contraseña cumple con todos los requisitos de seguridad
        return true;
    }

    public static Task<User> getUser(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers").document(uid);

        return docRef.get().continueWith(task -> {
            User user;
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = capitalizeString(document.get("name").toString());
                    String surname = capitalizeString(document.get("surname").toString());
                    int points = Integer.parseInt(document.get("points").toString());
                    Timestamp timestamp = (Timestamp) document.get("birthdate");
                    Date fechaNac = timestamp.toDate();
                    FirebaseUser userFB = FirebaseAuth.getInstance().getCurrentUser();
                    int numTelef = Integer.parseInt(document.get("telephone").toString());
                    String email = userFB.getEmail();
                    boolean communications = Boolean.valueOf(document.get("communications").toString());
                    
                    user = new User(name, surname, points, fechaNac, numTelef, email, communications);
                } else {
                    user = new User();
                }
            } else {
                user = new User();
                Toast.makeText(MainActivity.getAppContext(), "Error al obtener el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
            return user;
        });
    }
    // Obtener en el intent --> MyObject obj = (MyObject) getIntent().getSerializableExtra("my_object");

    public static double getPriceMenu(double precioHamburguesa, double precioPatatas, double precioBebida) {
        double sumaPrecios = precioHamburguesa + precioPatatas + precioBebida;
        double descuento = sumaPrecios * 0.2;
        double totalConDescuento = sumaPrecios - descuento;

        int numeroDecimales = 2;
        double factor = Math.pow(10, numeroDecimales);
        double totalRedondeado = Math.floor(totalConDescuento * factor) / factor;

        return totalRedondeado;
    }


    // MÉTODOS DE TEXTO
    public static int hashToNumber(String input) {
        try {
            // Aplicar la función hash SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Convertir los bytes en un número positivo utilizando BigInteger
            BigInteger number = new BigInteger(1, hash);

            // Reducir el número a un número de 6 dígitos mediante la operación módulo 1,000,000
            int result = number.mod(BigInteger.valueOf(1000000)).intValue();
            return result;
        } catch (NoSuchAlgorithmException e) {
            // Manejar la excepción en caso de que el algoritmo no esté disponible
            e.printStackTrace();
            return 0;
        }
    }


    public static String capitalizeString(String cadena) {
        String nuevaCadena = "";

        String [] palabras = cadena.split(" ");
        for (int i = 0; i < palabras.length; i++){
            nuevaCadena += palabras[i].substring(0,1).toUpperCase() + palabras[i].substring(1).toLowerCase();
            nuevaCadena += " "; //espacio entre cada palabra
        }
        nuevaCadena = nuevaCadena.trim();

        return nuevaCadena;
    }


    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);  // --> dd/MM/yyyy
        String dateTxt = formatter.format(date);

        return dateTxt;
    }

    public static Date formatStringDate(String dateTxt, String format) throws ParseException {
        SimpleDateFormat sdFormat = new SimpleDateFormat(format);
        Date date = sdFormat.parse(dateTxt);

        return date;
    }

    /*public static void abrirPaginaWeb(String url) {
        Context context = getContext();
        Intent i = new Intent(getApplicationContext(), Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }*/


}
