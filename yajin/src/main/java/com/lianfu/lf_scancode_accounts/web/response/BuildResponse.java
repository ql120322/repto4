package com.lianfu.lf_scancode_accounts.web.response;

import com.lianfu.lf_scancode_accounts.model.ResponseModel;

public class BuildResponse {
    public static <T> ResponseModel<T> success(T data) {
        return new ResponseModel<T>(200, true, "OK", data, System.currentTimeMillis());
    }

    public static <T> ResponseModel<T> error(String errorMessage) {
        return new ResponseModel<T>(100, false, errorMessage, null, System.currentTimeMillis());
    }
}
