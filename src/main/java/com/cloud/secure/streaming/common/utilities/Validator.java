package com.cloud.secure.streaming.common.utilities;

import com.cloud.secure.streaming.common.enums.APIStatusMessage;
import com.cloud.secure.streaming.common.exceptions.ApplicationException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 689Cloud
 */
public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param apiStatusMessage
     * @throws ApplicationException if {@code obj} is NOT null
     */

    public static void mustNull(Object obj, RestAPIStatus RestAPIStatus,APIStatusMessage apiStatusMessage) {

        if (obj != null) {
            throw new ApplicationException(RestAPIStatus, apiStatusMessage);
        }
    }

    /**
     * Validate Object can not be null
     *
     * @param obj
     * @param RestAPIStatus
     * @param message
     */
    public static void notNull(Object obj, RestAPIStatus RestAPIStatus, String message) {
        if (obj == null) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate list object not null & not empty
     *
     * @param obj
     * @param message
     */
    public static void notNullAndNotEmpty(List<?> obj, RestAPIStatus RestAPIStatus, APIStatusMessage message) {

        if (obj == null || obj.isEmpty()) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }

    /**
     * Validate object not null & not empty
     *
     * @param obj
     * @param message
     */

    public static void notNullAndNotEmpty(Object obj, RestAPIStatus RestAPIStatus, APIStatusMessage message) {

        if (obj == null || "".equals(obj)) {
            throw new ApplicationException(RestAPIStatus, message);
        }
    }


    /**
     * Validate list object must null & must empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void mustNullAndMustEmpty(List<?> obj, RestAPIStatus apiStatus, APIStatusMessage message) {
        if (obj != null && !obj.isEmpty()) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    /**
     * Validate 2 strings must equals
     * @param str1
     * @param str2
     * @param RestAPIStatus
     * @param message
     */
    public static void mustEquals(String str1, String str2, RestAPIStatus RestAPIStatus, APIStatusMessage message) {
        if (!str1.equals(str2)) {
            throw new ApplicationException(RestAPIStatus, message);

        }
    }

    /**
     * Validate 2 strings must not equals
     * @param str1
     * @param str2
     * @param RestAPIStatus
     * @param message
     */
    public static void mustNotEquals(String str1, String str2, RestAPIStatus RestAPIStatus, APIStatusMessage message) {
        if (str1.equals(str2)) {
            throw new ApplicationException(RestAPIStatus, message);

        }
    }

    /**
     * validate email format
     *
     * @param emailAddress
     */
    public static void validateEmailFormat(String emailAddress) {
        boolean isEmailFormat = isEmailFormat(emailAddress);
        if (!isEmailFormat) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Invalid email format");
        }
    }

    /**
     * @param valueToValidate
     * @return
     */
    private static boolean isEmailFormat(String valueToValidate) {
        Pattern regexPattern = Pattern.compile(Constant.EMAIL_PATTERN);
        if (valueToValidate != null) {
            if (valueToValidate.indexOf("@") <= 0) {
                return false;
            }
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(valueToValidate);
            return matcher.matches();
        } else {
            // The case of empty Regex expression must be accepted
            Matcher matcher = regexPattern.matcher("");
            return matcher.matches();
        }
    }

    /**
     * Validate numbers have to greater or equal a given number
     *
     * @param baseNumber
     * @param restAPIStatus
     * @param message
     * @param numbers
     */
    public static void mustGreaterThanOrEqual(int baseNumber, RestAPIStatus restAPIStatus, APIStatusMessage message, int... numbers) {
        for (int i : numbers) {
            if (i < baseNumber) {
                throw new ApplicationException(restAPIStatus, message);
            }
        }
    }

    /**
     * Validate numbers have to greater a given number
     *
     * @param baseNumber
     * @param restAPIStatus
     * @param message
     * @param numbers
     */
    public static void mustGreaterThan(int baseNumber, RestAPIStatus restAPIStatus, APIStatusMessage message, int... numbers) {
        for (int i : numbers) {
            if (i <= baseNumber) {
                throw new ApplicationException(restAPIStatus, message);
            }
        }
    }

    /**
     * Validate numbers have to less than a given number
     *
     * @param baseNumber
     * @param restAPIStatus
     * @param message
     * @param numbers
     */
    public static void mustLessThan(int baseNumber, RestAPIStatus restAPIStatus, APIStatusMessage message, int... numbers) {
        for (int i : numbers) {
            if (i >= baseNumber) {
                throw new ApplicationException(restAPIStatus, message);
            }
        }
    }

    /**
     * Validate numbers have to equal a given number
     *
     * @param baseNumber
     * @param restAPIStatus
     * @param message
     * @param numbers
     */
    public static void mustEqual(int baseNumber, RestAPIStatus restAPIStatus, APIStatusMessage message, int... numbers) {
        for (int i : numbers) {
            if (i > baseNumber || i < baseNumber) {
                throw new ApplicationException(restAPIStatus, message);
            }
        }
    }

    /**
     * Validate string have to match with a pattern
     * @param text
     * @param pattern
     * @param restAPIStatus
     * @param apiStatusMessage
     */
    public static void mustMatch(String text, String pattern, RestAPIStatus restAPIStatus, APIStatusMessage apiStatusMessage) {
        Pattern accountNumPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = accountNumPattern.matcher(text.trim());
        boolean matches = matcher.matches();
        if (!matches) {
            throw new ApplicationException(restAPIStatus, apiStatusMessage);
        }
    }

    public static  void mustIn(String text, String[] texts, RestAPIStatus restAPIStatus, APIStatusMessage apiStatusMessage ){
        if (!Arrays.asList(texts).contains(text)) {
            throw new ApplicationException(restAPIStatus, apiStatusMessage);
        }
    }
    public static void validPhoneNumber(String str) {
        String regex = "\\d{8,15}";
        boolean isPhone = str.matches(regex);
        if (!isPhone) {
            throw new ApplicationException(RestAPIStatus.INVALID_PHONE);
        }
    }
}
