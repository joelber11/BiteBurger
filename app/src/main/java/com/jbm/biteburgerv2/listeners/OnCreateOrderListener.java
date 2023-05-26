package com.jbm.biteburgerv2.listeners;

public interface OnCreateOrderListener {
    void onSuccess(String docId);
    void onFailure(Exception e);

    void onFailure(String mensaje);
}
