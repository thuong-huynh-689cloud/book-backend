package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateRatingRequest;
import com.cloud.secure.streaming.entities.Rating;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RatingHelper {

    public Rating createRating(CreateRatingRequest createRatingRequest){
        Rating rating = new Rating();
        rating.setId(UniqueID.getUUID());
        rating.setCourseId(createRatingRequest.getCourseId());
        rating.setSelectRating(createRatingRequest.getSelectRating());
        rating.setDescription(createRatingRequest.getDescription());
        rating.setCreatedDate(DateUtil.convertToUTC(new Date()));

        return rating;
    }
}
