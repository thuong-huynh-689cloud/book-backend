package com.cloud.secure.streaming.controllers.helper;


import com.cloud.secure.streaming.common.enums.AddressType;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.config.security.AuthUser;
import com.cloud.secure.streaming.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressHelper {
    public Address createAddress(CreateAddressRequest createAddressRequest, AuthUser authUser){

        Address address = new Address();
        //add id to data
        address.setId(UniqueID.getUUID());
        //ad userId to data
        address.setUserId(authUser.getId());
        //ad city to data
        address.setCity(createAddressRequest.getCity());
        //ad District to data
        address.setDistrict(createAddressRequest.getDistrict());
        //ad Address to data
        address.setAddress(createAddressRequest.getAddress());
        //ad ZipCode to data
        address.setZipCode(createAddressRequest.getZipCode());
        //ad DefaultAddress to data
        address.setDefaultAddress(createAddressRequest.getDefaultAddress());
        //ad AddressType to data
        address.setAddressType(AddressType.HOME);
        //ad Country to data
        address.setCountry(createAddressRequest.getCountry());

        return address;
    }

    public Address updateAddress(Address address , UpdateAddressRequest updateAddressRequest){
        if (updateAddressRequest.getCity() != null && !updateAddressRequest.getCity().trim().isEmpty() &&
        !updateAddressRequest.getCity().trim().equals(address.getCity())){
            address.setCity(updateAddressRequest.getCity().trim());
        }
        if (updateAddressRequest.getDistrict() != null && !updateAddressRequest.getDistrict().trim().isEmpty() &&
        !updateAddressRequest.getDistrict().trim().equals(address.getDistrict())){
            address.setDistrict(updateAddressRequest.getDistrict());
        }
        if (updateAddressRequest.getAddress() != null && !updateAddressRequest.getAddress().trim().isEmpty()&&
        !updateAddressRequest.getAddress().trim().equals(address.getAddress())){
            address.setAddress(updateAddressRequest.getAddress());
        }
        if (updateAddressRequest.getZipCode() != null && !updateAddressRequest.getZipCode().trim().isEmpty() &&
        !updateAddressRequest.getZipCode().trim().equals(address.getZipCode())){
            address.setZipCode(updateAddressRequest.getZipCode());
        }
        if (updateAddressRequest.getCountry() !=null&& !updateAddressRequest.getCountry().trim().isEmpty()&&
        !updateAddressRequest.getCountry().trim().equals(address.getCountry())){
            address.setCountry(updateAddressRequest.getCountry());
        }
        if (updateAddressRequest.getAddressType() != null) {
            address.setAddressType(updateAddressRequest.getAddressType());
        }

        return address;
    }
}
