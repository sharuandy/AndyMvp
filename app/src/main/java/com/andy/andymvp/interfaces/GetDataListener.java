package com.andy.andymvp.interfaces;

import com.andy.andymvp.data.ResponseData;

public interface GetDataListener {
    void onSuccess(String message, ResponseData respData);

    void onFailure(String message);
}
