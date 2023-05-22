package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.data.Food;

public interface OnGetAddressListener {
    void onComplete(Address address);
}