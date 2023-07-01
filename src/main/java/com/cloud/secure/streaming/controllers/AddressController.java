package com.cloud.secure.ecommerce.controllers;

import com.cloud.secure.ecommerce.common.exceptions.ApplicationException;
import com.cloud.secure.ecommerce.common.utils.RestAPIResponse;
import com.cloud.secure.ecommerce.common.utils.RestAPIStatus;
import com.cloud.secure.ecommerce.common.utils.Validator;
import com.cloud.secure.ecommerce.config.security.AuthSession;
import com.cloud.secure.ecommerce.config.security.AuthUser;
import com.cloud.secure.ecommerce.controllers.helper.AddressHelper;
import com.cloud.secure.ecommerce.controllers.model.request.CreateAddressRequest;
import com.cloud.secure.ecommerce.controllers.model.request.UpdateAddressRequest;
import com.cloud.secure.ecommerce.entities.Address;
import com.cloud.secure.ecommerce.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(ApiPath.ADDRESS_API)
public class AddressController extends AbstractBaseController {
    final AddressService addressService;
    final AddressHelper addressHelper;

    public AddressController(AddressService addressService, AddressHelper addressHelper) {
        this.addressService = addressService;
        this.addressHelper = addressHelper;
    }

    /**
     * create Address API
     *
     * @param createAddressRequest
     * @param authUser
     * @return
     */

    @PostMapping()
    @Operation(summary = "Create Address")
    public ResponseEntity<RestAPIResponse> createAddress(
            @RequestBody CreateAddressRequest createAddressRequest,
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {

        // create address
        Address address = addressHelper.createAddress(createAddressRequest, authUser);
        addressService.saveAddress(address);

        return responseUtil.successResponse(address);
    }

    /**
     * get address API
     *
     * @param id
     * @return
     */


    @GetMapping(path = ApiPath.ID)
    @Operation(summary = "Get Address")
    public ResponseEntity<RestAPIResponse> getAddress(
            @PathVariable(name = "id") String id
    ) {
        //get address by id
        Address address = addressService.getId(id);
        Validator.notNull(address, RestAPIStatus.NOT_FOUND, "id address not found");

        return responseUtil.successResponse(address);
    }



    @GetMapping(path = ApiPath.ALL)
    @Operation(summary = "Get All Address")
    public ResponseEntity<RestAPIResponse> getAllAddress(
            @Parameter(hidden = true) @AuthSession AuthUser authUser

    ) {
        List<Address> addresses = addressService.getByUserId(authUser.getId());

        return responseUtil.successResponse(addresses);

    }


    /**
     * update address API
     *
     * @param id
     * @param updateAddressRequest
     * @return
     */

    @PutMapping(path = ApiPath.ID)
    @Operation(summary = "Update Address")
    public ResponseEntity<RestAPIResponse> updateAddress(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateAddressRequest updateAddressRequest

    ) {
        Address address = addressService.getId(id);
        Validator.notNull(address, RestAPIStatus.NOT_FOUND, "id address not found");
        address = addressHelper.updateAddress(address, updateAddressRequest);
        addressService.saveAddress(address);

        return responseUtil.successResponse(address);
    }

    /**
     * delete address API
     *
     * @param ids
     * @return
     */

    @DeleteMapping
    @Operation(summary = "Delete Address")
    public ResponseEntity<RestAPIResponse> deleteAddress(
            @RequestParam(name = "ids") List<String> ids

    ) {
        // check ids
        List<String> idsList = ids.stream().distinct().collect(Collectors.toList());
        // get address
        List<Address> addresses = addressService.getAllById(idsList);
        if (idsList.size() != addresses.size()) {
            throw new ApplicationException(RestAPIStatus.EXISTED, "id address not found");
        }
        addressService.deleteAddress(addresses);
        return responseUtil.successResponse("ok");

    }
}
