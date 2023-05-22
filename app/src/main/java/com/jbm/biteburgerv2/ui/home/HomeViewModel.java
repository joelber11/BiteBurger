package com.jbm.biteburgerv2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jbm.biteburgerv2.DataOperations;
import com.jbm.biteburgerv2.data.User;
import com.jbm.biteburgerv2.listeners.OnGetUserListener;
import com.jbm.biteburgerv2.operations.FireBaseOperations;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mNombre;
    private final MutableLiveData<String> mPuntos;
    private final MutableLiveData<String> mProvider;
    private final MutableLiveData<String> mUserEmail;

    public HomeViewModel() {
        mNombre = new MutableLiveData<>();
        mPuntos = new MutableLiveData<>();
        mProvider = new MutableLiveData<>();
        mUserEmail = new MutableLiveData<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FireBaseOperations.getCurrentUser(uid, new OnGetUserListener() {
            @Override
            public void onComplete(User user) {
                mNombre.setValue("Hola " + DataOperations.capitalizeString(user.getName()) + ",");
                mPuntos.setValue("Tienes " + user.getPoints() + " puntos");
            }
        });

        mUserEmail.setValue(user.getEmail());
        mProvider.setValue("BASIC");
    }

    public LiveData<String> getUserName() {
        return mNombre;
    }

    public LiveData<String> getUserPoints() {
        return mPuntos;
    }

    public LiveData<String> getUserProvider() {
        return mProvider;
    }

    public LiveData<String> getUserEmail() {
        return mUserEmail;
    }

}