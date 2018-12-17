package com.andy.andymvp.data;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataManager {

    private ResponseData responseData;

    private static ResponseDataManager instance = null;

    private ResponseDataManager() {
    }

    public static ResponseDataManager getInstance() {

        synchronized (ResponseDataManager.class) {
            if (instance == null) {
                instance = new ResponseDataManager();
            }
        }

        return instance;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }
}
