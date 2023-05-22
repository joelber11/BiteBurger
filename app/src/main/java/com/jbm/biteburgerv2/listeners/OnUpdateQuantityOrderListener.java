package com.jbm.biteburgerv2.listeners;

public interface OnUpdateQuantityOrderListener {
    void onSuccess();
    void onFailure(Exception e);
}
