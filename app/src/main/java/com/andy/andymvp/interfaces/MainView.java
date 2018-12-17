package com.andy.andymvp.interfaces;

import com.andy.andymvp.data.ResponseData;

public interface MainView {
    void onGetDataSuccess(String message, ResponseData responseData);

    void onGetDataFailure(String message);

    void showProgress();

    void hideProgress();
}
