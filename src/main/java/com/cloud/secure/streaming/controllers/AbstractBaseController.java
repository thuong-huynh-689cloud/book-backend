package com.cloud.secure.streaming.controllers;

import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import com.cloud.secure.streaming.common.utilities.ResponseUtil;
import com.cloud.secure.streaming.common.notification.VelocityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 689Cloud
 */
public abstract class AbstractBaseController {

    @Autowired
    public ResponseUtil responseUtil;

    @Autowired
    ApplicationConfigureValues applicationConfigureValues;

    @Autowired
    VelocityService velocityService;

    @Autowired
    PasswordEncoder passwordEncoder;

}
