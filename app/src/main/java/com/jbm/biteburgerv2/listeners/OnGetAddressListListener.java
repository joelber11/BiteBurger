package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;

public interface OnGetAddressListListener {
    void onComplete(ArrayList<Address> address);
}