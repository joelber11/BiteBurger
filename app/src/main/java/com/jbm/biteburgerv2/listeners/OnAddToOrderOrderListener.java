package com.jbm.biteburgerv2.listeners;

public interface OnAddToOrderOrderListener {
    void onSuccess();
    void onFailure(Exception e);
}
