package com.andy.andymvp.presenter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.andy.andymvp.R;
import com.andy.andymvp.data.ResponseData;
import com.andy.andymvp.data.ResponseDataManager;
import com.andy.andymvp.interfaces.GetDataListener;
import com.andy.andymvp.interfaces.MainInteractor;
import com.andy.andymvp.network.NetworkApi;
import com.andy.andymvp.utils.AppLog;
import com.andy.andymvp.utils.AppUtils;
import com.andy.andymvp.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class MainInteractorImpl implements MainInteractor {

    private GetDataListener mGetDatalistener;
    private RequestQueue mRequestQueue;

    private final String REQUEST_TAG = "DATA_API_CALL";
    private NetworkApi networkApi;

    public MainInteractorImpl(GetDataListener mGetDatalistener) {
        this.mGetDatalistener = mGetDatalistener;
    }

    @Override
    public void provideData(Context context, boolean isRestoring) {

        Boolean makeDataCall = false;
        if (isRestoring) {

            ResponseData existingData = ResponseDataManager.getInstance().getResponseData();

            if (existingData != null && !existingData.getRows().isEmpty()) {
                // we have cached copy of data for restoring purpose
                makeDataCall = false;
                mGetDatalistener.onSuccess("Restored Data", existingData);
            } else {
                makeDataCall = true;
            }
        } else {
            makeDataCall = true;
        }

        if (makeDataCall) {

            if (AppUtils.isNetworkAvailable(context)) {
                this.makeDataCall(context, StringUtils.BOX_URL);
            } else {
                mGetDatalistener.onFailure(context.getResources().getString(R.string.no_internet_info));
            }
        }
    }

    public void makeDataCall(Context context, String url) {
        if (networkApi != null)
            networkApi = NetworkApi.getInstance(context);
        networkApi.cancelAllRequests(REQUEST_TAG);

        networkApi.getCompanyList(dataApiSuccessListener(), dataErroListener(), REQUEST_TAG);

    }

    @Override
    public void onDestroy() {

    }

    //Volley Success Listener
    public com.android.volley.Response.Listener<ResponseData> dataApiSuccessListener() {
        return new com.android.volley.Response.Listener<ResponseData>() {
            @Override
            public void onResponse(ResponseData response) {
                try {
                    mGetDatalistener.onSuccess("data success", response);

                } catch (JsonSyntaxException ex) {
                    AppLog.e(REQUEST_TAG, ex.toString());
                    mGetDatalistener.onFailure(ex.toString());
                }
            }
        };
    }


    public com.android.volley.Response.ErrorListener dataErroListener() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(REQUEST_TAG, error.toString());
                mGetDatalistener.onFailure(error.toString());
            }
        };
    }
}
