package com.mangarider.test;

import lombok.RequiredArgsConstructor;

public class ServiceEndpoints {
    @RequiredArgsConstructor
    public enum AuthenticationController {
        REGISTRATION("/api/v1/users/registration"),
        LOGIN("/api/v1/users/login");

        public final String value;
    }

    @RequiredArgsConstructor
    public enum UserController {
        GET_USER("/api/v1/users/%s"),
        GET_USERS("/api/v1/users"),
        GET_PERSONAL_ACCOUNT("/api/v1/users/personal/account"),
        UPDATE_PERSONAL_ACCOUNT("/api/v1/users/personal/account"),
        ADD_IMAGE_ON_ACCOUNT("/api/v1/users/personal/account/images");

        public final String value;
    }
}
