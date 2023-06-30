package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreatePointPackageRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdatePointPackageRequest;
import com.cloud.secure.streaming.entities.PointPackage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PointPackageHelper {

    /**
     * Create BuyPoint API
     *
     * @param createPointPackageRequest
     * @return
     */
    public PointPackage createPointPackage(CreatePointPackageRequest createPointPackageRequest) {

        PointPackage pointPackage = new PointPackage();
        pointPackage.setId(UniqueID.getUUID());
        pointPackage.setPoint(createPointPackageRequest.getPoint());
        pointPackage.setPrice(createPointPackageRequest.getPrice());
        pointPackage.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return pointPackage;
    }

    /**
     * @param pointPackage
     * @return
     */
    public PointPackage updatePointPackage(PointPackage pointPackage, UpdatePointPackageRequest updatePointPackageRequest) {

        //check point
        if (updatePointPackageRequest.getPoint() != null &&
                !updatePointPackageRequest.getPoint().equals(pointPackage.getPoint())) {
            pointPackage.setPoint(updatePointPackageRequest.getPoint());
        }
        //check money
        if (updatePointPackageRequest.getPrice() != null &&
                !updatePointPackageRequest.getPrice().equals(pointPackage.getPrice())) {
            pointPackage.setPrice(updatePointPackageRequest.getPrice());
        }
        return pointPackage;
    }
}
