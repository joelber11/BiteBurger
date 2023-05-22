package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenuShop;
import com.jbm.biteburgerv2.adapters.AdapterFoodSummary;
import com.jbm.biteburgerv2.data.Food;
import com.jbm.biteburgerv2.data.FoodSummary;

import java.util.ArrayList;

public interface OnFoodSummaryListListener {
    void onComplete(ArrayList<FoodSummary> foodList, AdapterFoodSummary adaptador);
}