package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterOrderSummary;
import com.jbm.biteburgerv2.data.Address;
import com.jbm.biteburgerv2.data.OrderSummary;

import java.util.ArrayList;

public interface OnGetOrdersListListener {
    void onComplete(ArrayList<OrderSummary> orders, AdapterOrderSummary adaptador);
}