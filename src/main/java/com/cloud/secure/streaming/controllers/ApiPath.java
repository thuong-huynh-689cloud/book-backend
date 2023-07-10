package com.cloud.secure.streaming.controllers;

/**
 * @author 689Cloud
 */
public interface ApiPath {
    // Base URL
    String BASE_API_PATH = "/api";

    String ALL = "/all";

    String ID = "/{id}";

    // Base URL
    String BASE_API_URL = "/api";
    // Authenticate APIs
    String AUTHENTICATE_API = BASE_API_URL + "/auth";
    String FORGOT_PASSWORD = "/forgot-password";
    String RESET_PASSWORD = "/reset-password/{reset_key}";
    String CHANGE_PASSWORD = "/change-password";
    String VERIFY_ACCOUNT = "/verify-account/{verify_key}";
    String SIGN_UP = "/sign-up";

    // User APIs
    String USER_API = BASE_API_URL + "/user";
    // Address APIs
    String ADDRESS_API = BASE_API_URL + "/address";
    // Card APIs
    String CARD_API = BASE_API_URL + "/card";
    // Payment APIs
    String PAYMENT_API = BASE_API_URL + "/payment";


    String SESSION_API = BASE_API_URL + "/session";
    String PRODUCT_IMAGES_API = BASE_API_URL + "/product-image";
    String CATEGORY_API = BASE_API_URL + "/category";
    String ORDER_API = BASE_API_URL + "/order";
    String ORDER_DETAIL_API = BASE_API_URL + "/orderDetail";
    String PRODUCT_CATEGORY_API = BASE_API_URL + "/product-category";
    String PRODUCT_API = BASE_API_URL + "/product";
    String PAGING = "/paging";
}
