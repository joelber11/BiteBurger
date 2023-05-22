package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.data.User;

import java.util.ArrayList;

public interface OnGetUserListener {
    void onComplete(User user);
}
