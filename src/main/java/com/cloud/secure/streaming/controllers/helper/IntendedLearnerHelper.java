package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateCoursePreviewRequest;
import com.cloud.secure.streaming.controllers.model.request.CreateDetailCoursePreview;
import com.cloud.secure.streaming.controllers.model.request.CreateIntendedLearnerRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateIntendedLearnerRequest;
import com.cloud.secure.streaming.entities.IntendedLearner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 689Cloud
 */
@Component
public class IntendedLearnerHelper {

    /**
     * Create Intended Learner
     *
     * @param courseId
     * @param createIntendedLearnerRequests
     * @return
     */
    public List<IntendedLearner> createIntendedLearner(String courseId,
                                                       List<CreateIntendedLearnerRequest> createIntendedLearnerRequests) {
        List<IntendedLearner> intendedLearners = new ArrayList<>();
        createIntendedLearnerRequests.forEach(createIntendedLearnerRequest -> {
            IntendedLearner intendedLearner = new IntendedLearner(
                    UniqueID.getUUID(),
                    courseId,
                    createIntendedLearnerRequest.getSentence(),
                    null,
                    createIntendedLearnerRequest.getType()
            );
            intendedLearner.setCreatedDate(DateUtil.convertToUTC(new Date()));
            intendedLearners.add(intendedLearner);
        });
        return intendedLearners;
    }

    /**
     * Update Intended Learner
     *
     * @param intendedLearner
     * @param updateIntendedLearnerRequest
     * @return
     */
    public IntendedLearner updateIntendedLearner(IntendedLearner intendedLearner,
                                                 UpdateIntendedLearnerRequest updateIntendedLearnerRequest) {
        if (updateIntendedLearnerRequest.getSentence() != null && !updateIntendedLearnerRequest.getSentence().trim().isEmpty()
                && !intendedLearner.getSentence().equals(updateIntendedLearnerRequest.getSentence().trim())) {
            intendedLearner.setSentence(updateIntendedLearnerRequest.getSentence().trim());
        }
        return intendedLearner;
    }

    /**
     * Update Intended Learner
     *
     * @param intendedLearners
     * @param requests
     * @return
     */
    public List<IntendedLearner> updateIntendedLearner(List<IntendedLearner> intendedLearners,
                                                       List<CreateIntendedLearnerRequest> requests) {
        for (IntendedLearner i : intendedLearners) {
            for (CreateIntendedLearnerRequest request : requests) {
                if (i.getId().equals(request.getId())) {
                    if (request.getSentence() != null && !request.getSentence().trim().isEmpty()
                            && !i.getSentence().equals(request.getSentence().trim())) {
                        i.setSentence(request.getSentence().trim());
                    }
                }
            }
        }

        return intendedLearners;
    }

    /**
     * Create Course Preview
     *
     * @param courseId
     * @param createDetailCoursePreviews
     * @return
     */
    public List<IntendedLearner> createCoursePreview(String courseId, List<CreateDetailCoursePreview> createDetailCoursePreviews) {
        List<IntendedLearner> intendedLearners = new ArrayList<>();
        createDetailCoursePreviews.forEach(createIntendedLearnerRequest -> {
            IntendedLearner intendedLearner = new IntendedLearner(
                    UniqueID.getUUID(),
                    courseId,
                    createIntendedLearnerRequest.getSentence(),
                    null,
                    IntendedLearnerType.PREVIEW
            );
            intendedLearner.setCreatedDate(DateUtil.convertToUTC(new Date()));
            intendedLearners.add(intendedLearner);
        });
        return intendedLearners;
    }

    public List<IntendedLearner> updateCoursePreview(List<IntendedLearner> listUpdate, List<CreateDetailCoursePreview> requests) {
        for (IntendedLearner i : listUpdate) {
            for (CreateDetailCoursePreview request : requests) {
                if (i.getId().equals(request.getId())) {
                    if (request.getSentence() != null && !request.getSentence().trim().isEmpty()
                            && !i.getSentence().equals(request.getSentence().trim())) {
                        i.setSentence(request.getSentence().trim());
                    }
                }
            }
        }

        return listUpdate;
    }
}
