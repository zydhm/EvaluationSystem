package com.example.evaluation_system.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ParseUtil {
    public static RequestBody parseRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

}
