package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;

public interface OnFoodListListener {
    void onComplete(ArrayList<Food> foodList, AdapterFoodMenu adaptador);
}