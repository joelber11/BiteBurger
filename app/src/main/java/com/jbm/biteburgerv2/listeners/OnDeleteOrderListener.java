package com.jbm.biteburgerv2.listeners;

public interface OnDeleteOrderListener {
    void onSuccess();
    void onFailure(Exception e);
}
