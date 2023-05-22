package com.jbm.biteburgerv2.listeners;

import com.jbm.biteburgerv2.adapters.AdapterAddressList;
import com.jbm.biteburgerv2.data.Address;

import java.util.ArrayList;

public interface OnAddressListListener {
    void onComplete(ArrayList<Address> addressList, AdapterAddressList adaptador);
}
