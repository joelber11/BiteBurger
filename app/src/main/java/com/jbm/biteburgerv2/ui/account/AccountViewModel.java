package com.jbm.biteburgerv2.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.jbm.biteburgerv2.DataOperations;
import com.jbm.biteburgerv2.data.User;
import com.jbm.biteburgerv2.listeners.OnGetUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<String> mNombre;
    private final MutableLiveData<String> mPuntos;
    private final MutableLiveData<User> mUser;

    public AccountViewModel() {
        mUser = new MutableLiveData<>();
        mNombre = new MutableLiveData<>();
        mPuntos = new MutableLiveData<>();


        // Obtengo el UID del usuario de la BD
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FireBaseOperations.getCurrentUser(uid, new OnGetUserListener() {
            @Override
            public void onComplete(User user) {
                mUser.setValue(user);
                mNombre.setValue(DataOperations.capitalizeString(user.getName()));
                mPuntos.setValue(user.getPoints() + " puntos");
            }
        });

    }

    public LiveData<String> getNombre() {
        return mNombre;
    }

    public LiveData<String> getPuntos() {
        return mPuntos;
    }

    public LiveData<User> getUser() {
        return mUser;
    }

}