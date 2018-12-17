package com.andy.andymvp.presenter;

import android.content.Context;

import com.andy.andymvp.data.ResponseData;
import com.andy.andymvp.data.ResponseDataManager;
import com.andy.andymvp.interfaces.GetDataListener;
import com.andy.andymvp.interfaces.MainInteractor;
import com.andy.andymvp.interfaces.MainPresenter;
import com.andy.andymvp.interfaces.MainView;

public class MainPresenterImpl implements MainPresenter, GetDataListener {

    private MainView mMainView;
    private MainInteractor mInteractor;

    public MainPresenterImpl(MainView mMainView) {
        this.mMainView = mMainView;
        this.mInteractor = new MainInteractorImpl(this);
    }

    public MainView getMainView() {
        return mMainView;
    }

    @Override
    public void getDataForList(Context context, boolean isRestoring) {

        // get this done by the interactor
        mMainView.showProgress();
        mInteractor.provideData(context, isRestoring);

    }

    @Override
    public void onDestroy() {

        mInteractor.onDestroy();
        if (mMainView != null) {
            mMainView.hideProgress();
            mMainView = null;
        }
    }

    @Override
    public void onSuccess(String message, ResponseData responseData) {

        // updating cache copy of data for restoring purpose
        ResponseDataManager.getInstance().setResponseData(responseData);

        if (mMainView != null) {
            mMainView.hideProgress();
            mMainView.onGetDataSuccess(message, responseData);
        }
    }

    @Override
    public void onFailure(String message) {
        if (mMainView != null) {
            mMainView.hideProgress();
            mMainView.onGetDataFailure(message);
        }

    }

}

