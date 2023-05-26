package com.jbm.biteburgerv2.operations;

import static android.content.ContentValues.TAG;

import static com.jbm.biteburgerv2.DataOperations.getPriceMenu;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.jbm.biteburgerv2.DataOperations;
import com.jbm.biteburgerv2.adapters.AdapterAddressList;
import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.adapters.AdapterFoodMenuShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodSummary;
import com.jbm.biteburgerv2.adapters.AdapterOffersList;
import com.jbm.biteburgerv2.adapters.AdapterOrderSummary;
import com.jbm.biteburgerv2.data.*;
import com.jbm.biteburgerv2.listeners.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FireBaseOperations {

    public static void loginUser(String userEmail, String userPasswd, OnLoginUserListener listener) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(userEmail, userPasswd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Se ha iniciado sesión correctamente
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ha ocurrido un error al iniciar sesión
                        listener.onFailure(e);
                    }
                });
    }

    public static void createDataUser(String uid, User user, OnCreateUserListener listener) {
        /* Crea al usuario en Firebase una vez creado en Auth */
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("customers").document(uid);

        // Crea un mapa que contenga los campos y valores
        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName().toUpperCase());
        data.put("surname", user.getSurname().toUpperCase());
        data.put("birthdate", user.getFechaNac());
        data.put("telephone", user.getNumTelef());
        data.put("points", user.getPoints());
        data.put("communications", user.isCommunications());
        data.put("short_id", DataOperations.hashToNumber(uid));

        // Doy los valores al documento
        docRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });

    }

    public static void updateUser(String uid, User user, OnUpdateUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crea un mapa que contenga los campos y valores que deseas actualizar
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", user.getName().toUpperCase());
        updates.put("surname", user.getSurname().toUpperCase());
        updates.put("birthdate", user.getFechaNac());
        updates.put("telephone", user.getNumTelef());
        updates.put("communications", user.isCommunications());

        // Actualiza el documento con los nuevos valores
        db.collection("customers").document(uid)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public static void listTypeFood(String tipo, AdapterFoodMenu adaptador, OnFoodListListener listener) {

        ArrayList<Food> foodList = new ArrayList<Food>();

        Map<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Snacks/entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        tipos.put("burgers", "burgers");
        tipos.put("snacks-starters", "snacks-starters");
        tipos.put("drinks", "drinks");
        tipos.put("offers", "offers");

        String tipoBD = tipos.get(tipo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection(tipoBD);

        // Recorrer la colección
        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = new Food(
                            document.getId(),
                            document.getString("name").toString(),
                            document.getString("desc").toString(),
                            document.getDouble("price"),
                            document.getString("image"));

                    foodList.add(food);
                }

                listener.onComplete(foodList, adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void listTypeFood(String tipo, AdapterOffersList adaptador, OnOffersListListener listener) {

        ArrayList<Food> foodList = new ArrayList<Food>();

        Map<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Snacks/entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        tipos.put("burgers", "burgers");
        tipos.put("snacks-starters", "snacks-starters");
        tipos.put("drinks", "drinks");
        tipos.put("offers", "offers");

        String tipoBD = tipos.get(tipo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection(tipoBD);

        // Recorrer la colección
        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = new Food(
                            document.getId(),
                            document.getString("name").toString(),
                            document.getString("desc").toString(),
                            document.getDouble("price"),
                            document.getString("image"));

                    foodList.add(food);
                }

                listener.onComplete(foodList, adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void listTypeFood(String tipo, AdapterFoodShop adaptador, OnFoodShopListListener listener) {

        ArrayList<Food> foodList = new ArrayList<Food>();

        Map<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Snacks/entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        tipos.put("burgers", "burgers");
        tipos.put("snacks-starters", "snacks-starters");
        tipos.put("drinks", "drinks");
        tipos.put("offers", "offers");

        String tipoBD = tipos.get(tipo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection(tipoBD);

        // Recorrer la colección
        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = new Food(
                            document.getId(),
                            document.getString("name").toString(),
                            document.getString("desc").toString(),
                            document.getDouble("price"),
                            document.getString("image"));

                    foodList.add(food);
                }

                listener.onComplete(foodList, adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void listTypeFood(String tipo, AdapterFoodMenuShop adaptador, OnFoodMenuShopListListener listener) {

        ArrayList<Food> foodList = new ArrayList<Food>();

        Map<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Snacks/entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        tipos.put("burgers", "burgers");
        tipos.put("snacks-starters", "snacks-starters");
        tipos.put("drinks", "drinks");
        tipos.put("offers", "offers");

        String tipoBD = tipos.get(tipo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection(tipoBD);

        // Recorrer la colección
        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = new Food(
                            document.getId(),
                            document.getString("name").toString(),
                            document.getString("desc").toString(),
                            document.getDouble("price"),
                            document.getString("image"));

                    foodList.add(food);
                }

                listener.onComplete(foodList, adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void listSummaryOrder(String uid, String idOrder, AdapterFoodSummary adaptador, OnFoodSummaryListListener listener) {
        ArrayList<FoodSummary> foodList = new ArrayList<FoodSummary>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference summary = db.collection("customers")
                .document(uid)
                .collection("orders")
                .document(idOrder)
                .collection("order_lines");

        summary.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    boolean isMenu = document.getBoolean("is_menu");
                    if(isMenu) {
                        FoodSummary food = new FoodSummary(
                                document.getId(),
                                isMenu,
                                document.getString("food_name_1").toString(),
                                document.getString("food_name_2").toString(),
                                document.getString("food_name_3").toString(),
                                document.getDouble("menu_price"),
                                document.getDouble("amount").intValue(),
                                document.getString("item_menu_image").toString()
                        );

                        foodList.add(food);
                    } else {
                        FoodSummary food = new FoodSummary(
                                document.getId(),
                                isMenu,
                                document.getString("food_name_1").toString(),
                                document.getDouble("food_price"),
                                document.getDouble("amount").intValue(),
                                document.getString("item_menu_image").toString()
                        );

                        foodList.add(food);
                    }

                }

                listener.onComplete(foodList, adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });

    }

    public static void listAddress(String uid, AdapterAddressList adaptador, OnAddressListListener listener) {
        ArrayList<Address> addressList = new ArrayList<Address>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference customerRef = db.collection("customers").document(uid);

        CollectionReference addressRef = customerRef.collection("address");


        // Recorrer la colección
        addressRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Address address = new Address(
                            document.getId(),
                            document.getString("street").toString(),
                            document.getLong("number").intValue(),
                            document.getString("floor_stairs").toString(),
                            document.getString("city").toString(),
                            document.getLong("postal_code").intValue(),
                            Boolean.parseBoolean(document.get("favorite").toString())
                    );

                    addressList.add(address);
                }

                listener.onComplete(addressList, adaptador);
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void getCurrentUser(String uid, OnGetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers").document(uid);

        // Recoger los datos del usuario
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                User user = new User();

                Timestamp timestamp = (Timestamp) document.get("birthdate");
                FirebaseUser userFB = FirebaseAuth.getInstance().getCurrentUser();

                user.setName(document.get("name").toString());
                user.setSurname(document.get("surname").toString());
                user.setPoints(Integer.parseInt(document.get("points").toString()));
                user.setFechaNac(timestamp.toDate());
                user.setNumTelef(Integer.parseInt(document.get("telephone").toString()));
                user.setEmail(userFB.getEmail());
                user.setCommunications(Boolean.valueOf(document.get("communications").toString()));
                user.setShortId(Integer.parseInt(document.get("short_id").toString()));

                listener.onComplete(user);
            } else {
                Log.d(TAG, "Error al obtener el usuario: ", task.getException());
            }
        });
    }

    public static void createOrder(String uid, OnCreateOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> pedidoData = new HashMap<>();
        pedidoData.put("creation_date", Calendar.getInstance().getTime());
        pedidoData.put("confirmation_date", null);
        pedidoData.put("total_amount", 0.0);

        CollectionReference docRef = db.collection("customers")
                .document(uid)
                .collection("address");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                long count = task.getResult().size();
                if(count == 0){
                    listener.onFailure("Es necesario añadir una dirección de envío.");
                } else {
                    CollectionReference customerRef = db.collection("customers");
                    customerRef
                            .document(uid)
                            .collection("orders")
                            .add(pedidoData)
                            .addOnSuccessListener(documentReference -> {
                                // Obtener la referencia del documento del pedido que acabas de crear
                                String docId = documentReference.getId();

                                // Obtener la referencia del documento del pedido que acabas de crear
                                DocumentReference pedidoRef = documentReference;

                                // Crear una subcolección para los productos del pedido
                                CollectionReference productosRef = pedidoRef.collection("order_lines");


                                listener.onSuccess(docId);
                            })
                            .addOnFailureListener(e -> listener.onSuccess(null));
                }
            }
        });




    }

    public static void getFood(String id, String categoria, AdapterFoodMenu adaptador, OnGetFoodListener listener) {
        ArrayList<Food> foodList = new ArrayList<Food>();

        Map<String, String> tipos = new HashMap<>();
        tipos.put("Hamburguesas", "burgers");
        tipos.put("Snacks/entrantes", "snacks-starters");
        tipos.put("Bebidas", "drinks");
        tipos.put("Ofertas", "offers");

        tipos.put("burgers", "burgers");
        tipos.put("snacks-starters", "snacks-starters");
        tipos.put("drinks", "drinks");

        String tipoBD = tipos.get(categoria);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection(tipoBD);

        // Recorrer la colección
        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.getId().equals(id)){
                        Food food = new Food(
                                document.getId(),
                                document.getString("name").toString(),
                                document.getString("desc").toString(),
                                document.getDouble("price"),
                                document.getString("image"));

                        foodList.add(food);
                        break;
                    }

                }

                listener.onComplete(foodList.get(0), adaptador); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void updateQuantityOrder(String idOrder, String uid, FoodSummary food, int nuevaCantidad, OnUpdateQuantityOrderListener listener) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("amount", nuevaCantidad);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .document(uid)
                .collection("orders")
                .document(idOrder)
                .collection("order_lines")
                .document(food.getLineaId())
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        db.collection("customers")
                                .document(uid)
                                .collection("orders")
                                .document(idOrder)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        double currentTotalAmount = documentSnapshot.getDouble("total_amount");

                                        double newTotalAmount;
                                        if(nuevaCantidad > food.getQuantity()) {
                                            newTotalAmount = currentTotalAmount + food.getPrice();
                                        } else {
                                            newTotalAmount = currentTotalAmount - food.getPrice();
                                        }


                                        // Actualizo el valor del campo "total_amount" en Firestore
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("total_amount", newTotalAmount);
                                        db.collection("customers")
                                                .document(uid)
                                                .collection("orders")
                                                .document(idOrder)
                                                .update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        listener.onSuccess();
                                                    }
                                                });
                                    }
                                });


                    }
                });
    }

    public static void deleteLineOrder(FoodSummary food, String idOrder, String uid, OnDeleteOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderLineRef = db.collection("customers")
                .document(uid)
                .collection("orders")
                .document(idOrder)
                .collection("order_lines")
                .document(food.getLineaId());

        orderLineRef.delete()
                .addOnSuccessListener(aVoid ->
                        db.collection("customers")
                                .document(uid)
                                .collection("orders")
                                .document(idOrder)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        double currentTotalAmount = documentSnapshot.getDouble("total_amount");

                                        double newTotalAmount = currentTotalAmount - (food.getPrice() * food.getQuantity());

                                        // Error en la resta
                                        double umbral = 1e-10;
                                        if (Math.abs(newTotalAmount) < umbral) {
                                            newTotalAmount = 0.0;
                                        }

                                        // Actualizo el valor del campo "total_amount" en Firestore
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("total_amount", newTotalAmount);
                                        db.collection("customers")
                                                .document(uid)
                                                .collection("orders")
                                                .document(idOrder)
                                                .update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        listener.onSuccess();
                                                    }
                                                });
                                    }
                                })

                )
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void addToOrder(String idOrder, String uid, ArrayList<Food> listFood, boolean isMenu, OnAddToOrderOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers")
                .document(uid)
                .collection("orders")
                .document(idOrder)
                .collection("order_lines")
                .document();

        if(isMenu) {
            Map<String, Object> productoData = new HashMap<>();
            productoData.put("is_menu", isMenu);
            productoData.put("food_ref_1", listFood.get(0).getId());
            productoData.put("food_name_1", listFood.get(0).getName());
            productoData.put("item_menu_image", listFood.get(0).getImgUrl());
            productoData.put("food_ref_2", listFood.get(1).getId());
            productoData.put("food_name_2", listFood.get(1).getName());
            productoData.put("food_ref_3", listFood.get(2).getId());
            productoData.put("food_name_3", listFood.get(2).getName());
            productoData.put("amount", 1);

            double importeMenu = getPriceMenu(listFood.get(0).getPrice(), listFood.get(1).getPrice(), listFood.get(2).getPrice());
            productoData.put("menu_price", importeMenu);

            docRef.set(productoData);

            db.collection("customers")
                    .document(uid)
                    .collection("orders")
                    .document(idOrder)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            double currentTotalAmount = documentSnapshot.getDouble("total_amount");

                            // Suma de la cantidad actual con la nueva cantidad
                            double newTotalAmount;
                            if(currentTotalAmount == 0.0) {
                                newTotalAmount = importeMenu;
                            } else {
                                newTotalAmount = currentTotalAmount + importeMenu;
                            }

                            // Actualizo el valor del campo "total_amount" en Firestore
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("total_amount", newTotalAmount);
                            db.collection("customers")
                                    .document(uid)
                                    .collection("orders")
                                    .document(idOrder)
                                    .update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            docRef.set(productoData);
                                            listener.onSuccess();
                                        }
                                    });
                        }
                    });
        } else {
            Map<String, Object> productoData = new HashMap<>();
            productoData.put("is_menu", isMenu);
            productoData.put("food_ref_1", listFood.get(0).getId());
            productoData.put("food_name_1", listFood.get(0).getName());
            productoData.put("item_menu_image", listFood.get(0).getImgUrl());
            productoData.put("food_price", listFood.get(0).getPrice());
            productoData.put("amount", 1);


            db.collection("customers")
                    .document(uid)
                    .collection("orders")
                    .document(idOrder)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            double currentTotalAmount = documentSnapshot.getDouble("total_amount");

                            // Suma de la cantidad actual con la nueva cantidad
                            double newTotalAmount = currentTotalAmount + listFood.get(0).getPrice();

                            // Actualizo el valor del campo "total_amount" en Firestore
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("total_amount", newTotalAmount);
                            db.collection("customers")
                                    .document(uid)
                                    .collection("orders")
                                    .document(idOrder)
                                    .update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            docRef.set(productoData);
                                            listener.onSuccess();
                                        }
                                    });
                        }
                    });
        }

    }

    public static void confirmOrder(String idOrder, String uid, OnConfirmOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updates = new HashMap<>();
        updates.put("confirmation_date", Calendar.getInstance().getTime());

        db.collection("customers")
                .document(uid)
                .collection("orders")
                .document(idOrder)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = db.collection("customers")
                                .document(uid)
                                .collection("orders")
                                .document(idOrder);

                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                double total = document.getDouble("total_amount");
                                int points = (int) (total * 100);

                                listener.onSuccess(points);
                            } else {
                                Log.d(TAG, "Error al obtener el usuario: ", task.getException());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public static void updateUserPoints(String uid, int ptos, OnAddToOrderOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("customers")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int currentTotalAmount = Integer.parseInt(documentSnapshot.get("points").toString());

                        // Suma de los puntos actuales con los nuevos puntos
                        int newPoints = currentTotalAmount + ptos;

                        // Actualizo el valor del campo "total_amount" en Firestore
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("points", newPoints);
                        db.collection("customers")
                                .document(uid)
                                .update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        listener.onSuccess();
                                    }
                                });
                    }
                });
    }

    public static Task<Integer> getNumDocuments(CollectionReference collection) {
        return collection.get().continueWith(task -> {
            QuerySnapshot querySnapshot = task.getResult();
            long size = querySnapshot.size();
            return ((int) size);
        });
    }

    public static void deleteFailOrders(String uid, OnDeleteOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersRef = db.collection("customers")
                .document(uid)
                .collection("orders");

        // Consulto los documentos que cumplen las condiciones
        ordersRef.whereEqualTo("confirmation_date", null)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        batch.delete(documentSnapshot.getReference());
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> listener.onSuccess())
                            .addOnFailureListener(e -> listener.onFailure(e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error al obtener los documentos a eliminar", e));
    }

    public static void deleteOrder(String uid, String orderId, OnDeleteOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersRef = db.collection("customers")
                .document(uid)
                .collection("orders");

        ordersRef.document(orderId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public static void getFavouriteAddress(String uid, OnGetAddressListener listener) {
        ArrayList<Address> addresList = new ArrayList<Address>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference addressRef = db
                .collection("customers")
                .document(uid)
                .collection("address");


        addressRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.getBoolean("favorite")){
                        Address address = new Address(
                                document.getId(),
                                document.getString("street"),
                                document.getDouble("number").intValue(),
                                document.getString("floor_stairs"),
                                document.getString("city"),
                                document.getDouble("postal_code").intValue(),
                                document.getString("province"),
                                document.getBoolean("favorite")
                        );

                        addresList.add(address);
                        break;
                    }

                }

                listener.onComplete(addresList.get(0)); // Aquí se llama al método onComplete del listener
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void getAddressList(String uid, OnGetAddressListListener listener) {
        ArrayList<Address> addresList = new ArrayList<Address>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference addressRef = db
                .collection("customers")
                .document(uid)
                .collection("address");


        addressRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Address address = new Address(
                            document.getId(),
                            document.getString("street"),
                            document.getDouble("number").intValue(),
                            document.getString("floor_stairs"),
                            document.getString("city"),
                            document.getDouble("postal_code").intValue(),
                            document.getString("province"),
                            document.getBoolean("favorite")
                    );

                    addresList.add(address);

                }

                listener.onComplete(addresList);
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void getOrdersList(String uid, AdapterOrderSummary adaptador, OnGetOrdersListListener listener) {
        ArrayList<OrderSummary> orderList = new ArrayList<OrderSummary>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference addressRef = db
                .collection("customers")
                .document(uid)
                .collection("orders");


        addressRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    //String date, double price, int points
                    OrderSummary order = new OrderSummary(
                            document.getDate("confirmation_date"),
                            document.getDouble("total_amount"),
                            (int) (document.getDouble("total_amount") * 100)
                    );

                    orderList.add(order);

                }

                listener.onComplete(orderList, adaptador);
            } else {
                Log.d(TAG, "Error al obtener los documentos: ", task.getException());
            }
        });
    }

    public static void createAddress(String uid, Address address, OnCreateUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crea un mapa que contenga los campos y valores
        Map<String, Object> data = new HashMap<>();
        data.put("street", address.getStreet());
        data.put("number", address.getNumber());
        data.put("floor_stairs", address.getFloorStairs());
        data.put("city", address.getCity());
        data.put("postal_code", address.getPostCode());
        data.put("favorite", address.isFavorite());


        if(address.isFavorite()) {
            CollectionReference docRef = db.collection("customers")
                    .document(uid)
                    .collection("address");

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    long count = task.getResult().size();
                    if(count == 0) {
                        CollectionReference docRef2 = db.collection("customers");
                        docRef2
                                .document(uid)
                                .collection("address")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {
                                    listener.onSuccess();
                                })
                                .addOnFailureListener(e -> listener.onFailure(e));
                    } else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean isFavorite = document.getBoolean("favorite");
                            if (isFavorite) {
                                String documentId = document.getId();
                                DocumentReference addressRef = docRef.document(documentId);
                                addressRef.update("favorite", false)
                                        .addOnSuccessListener(aVoid -> {
                                            CollectionReference docRef2 = db.collection("customers");
                                            docRef2
                                                    .document(uid)
                                                    .collection("address")
                                                    .add(data)
                                                    .addOnSuccessListener(documentReference -> {
                                                        listener.onSuccess();
                                                    })
                                                    .addOnFailureListener(e -> listener.onFailure(e));
                                        })
                                        .addOnFailureListener(e -> {
                                            listener.onFailure(e);
                                        });
                            }
                        }
                    }


                } else {
                    listener.onFailure(task.getException());
                }
            });

        } else {
            CollectionReference docRef = db.collection("customers")
                    .document(uid)
                    .collection("address");

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    long count = task.getResult().size();
                    if(count == 0) {
                        CollectionReference docRef2 = db.collection("customers");
                        data.put("favorite", true);

                        docRef2
                                .document(uid)
                                .collection("address")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {
                                    listener.onSuccess();
                                })
                                .addOnFailureListener(e -> listener.onFailure(e));
                    } else {
                        CollectionReference docRef2 = db.collection("customers");
                        docRef2
                                .document(uid)
                                .collection("address")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {
                                    listener.onSuccess();
                                })
                                .addOnFailureListener(e -> listener.onFailure(e));
                    }
                } else {
                    listener.onFailure(task.getException());
                }
            });



        }



    }

    public static void updateAddress(String uid, String addressId, Address address, OnUpdateAddressListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crea un mapa que contenga los campos y valores
        Map<String, Object> updates = new HashMap<>();
        updates.put("street", address.getStreet());
        updates.put("number", address.getNumber());
        updates.put("floor_stairs", address.getFloorStairs());
        updates.put("city", address.getCity());
        updates.put("postal_code", address.getPostCode());
        updates.put("favorite", address.isFavorite());

        if(address.isFavorite()) {
            CollectionReference docRef = db.collection("customers")
                    .document(uid)
                    .collection("address");

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        boolean isFavorite = document.getBoolean("favorite");
                        if (isFavorite) {
                            String documentId = document.getId();
                            DocumentReference addressRef = docRef.document(documentId);
                            addressRef.update("favorite", false)
                                    .addOnSuccessListener(aVoid -> {
                                        db.collection("customers")
                                                .document(uid)
                                                .collection("address")
                                                .document(addressId)
                                                .update(updates)
                                                .addOnSuccessListener(documentReference -> {
                                                    listener.onSuccess();
                                                })
                                                .addOnFailureListener(e -> listener.onFailure(e));
                                    })
                                    .addOnFailureListener(e -> listener.onFailure(e));
                        }
                    }
                } else {
                    listener.onFailure(task.getException());
                }
            });

        } else {
            db.collection("customers")
                    .document(uid)
                    .collection("address")
                    .document(addressId)
                    .update(updates)
                    .addOnSuccessListener(documentReference -> {
                        listener.onSuccess();
                    })
                    .addOnFailureListener(e -> listener.onFailure(e));
        }



    }

    public static void deleteAddress(String uid, String addressId, OnDeleteOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersRef = db.collection("customers")
                .document(uid)
                .collection("address");

        ordersRef.document(addressId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public static void updateFavouriteAddress(String uid, String addressId, OnUpdateAddressListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updates = new HashMap<>();
        updates.put("favorite", true);


        CollectionReference docRef = db.collection("customers")
                .document(uid)
                .collection("address");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    boolean isFavorite = document.getBoolean("favorite");
                    if (isFavorite) {
                        String documentId = document.getId();
                        DocumentReference addressRef = docRef.document(documentId);
                        addressRef.update("favorite", false)
                                .addOnSuccessListener(aVoid -> {
                                    db.collection("customers")
                                            .document(uid)
                                            .collection("address")
                                            .document(addressId)
                                            .update(updates)
                                            .addOnSuccessListener(documentReference -> {
                                                listener.onSuccess();
                                            })
                                            .addOnFailureListener(e -> listener.onFailure(e));
                                })
                                .addOnFailureListener(e -> listener.onFailure(e));
                    }
                }
            } else {
                listener.onFailure(task.getException());
            }
        });



    }

}
