package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    final AddressRepository addressRepository;


    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getId(String id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public List<Address> getAllById(List<String> ids) {
        return addressRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteAddress(List<Address> address) {
        addressRepository.deleteAll(address);
    }

    @Override
    public List<Address> getByUserId(String userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getByIdAndUserId(String id, String userId) {
        return addressRepository.findByIdAndUserId(id, userId);
    }

}
