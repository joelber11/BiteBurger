package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterFoodMenu;
import com.jbm.biteburgerv2.adapters.AdapterOffersList;
import com.jbm.biteburgerv2.data.Food;

import java.util.ArrayList;

public interface OnOffersListListener {
    void onComplete(ArrayList<Food> foodList, AdapterOffersList adaptador);
}