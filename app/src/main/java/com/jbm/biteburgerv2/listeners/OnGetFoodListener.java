package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;

public interface OnGetFoodListener {
    void onComplete(Food food, AdapterFoodMenu adaptador);
}