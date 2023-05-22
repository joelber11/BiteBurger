package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.data.Address;

public interface OnConfirmOrderListener {
    void onSuccess(int points);
    void onFailure(Exception e);
}