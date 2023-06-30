/*
 * Copyright (c) 689Cloud LLC. All Rights Reserved.
 * This software is the confidential and proprietary information of 689Cloud,
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with 689Cloud.
 */
package com.cloud.secure.streaming.common.validator;

import com.cloud.secure.streaming.common.utilities.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSValidator implements ConstraintValidator<NoXSS, String> {
  public static final Pattern VALID_XSS = Pattern.compile(Constant.XSS_PATTERN, Pattern.CASE_INSENSITIVE);

  @Override
  public void initialize(NoXSS noXSS) {
    //TODO nothing to do in here
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    boolean valid;
    Matcher matcher = VALID_XSS.matcher(value);
    valid = matcher.matches();
    return valid;
  }
}
