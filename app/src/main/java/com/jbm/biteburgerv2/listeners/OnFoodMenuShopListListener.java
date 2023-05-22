package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenuShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodShop;
import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;

public interface OnFoodMenuShopListListener {
    void onComplete(ArrayList<Food> foodList, AdapterFoodMenuShop adaptador);
}