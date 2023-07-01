package com.cloud.secure.streaming.services;


import com.cloud.secure.streaming.entities.Address;

import java.util.List;

public interface AddressService {

    Address saveAddress(Address address);

    Address getId(String id);

    List<Address> getAllById(List<String> ids);

    void deleteAddress(List<Address> address);

    List<Address> getByUserId(String ids);

    Address getByIdAndUserId(String id, String userId);


}
