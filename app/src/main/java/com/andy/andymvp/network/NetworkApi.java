package com.andy.andymvp.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.andy.andymvp.data.ResponseData;


import java.util.HashMap;
import java.util.Map;

public class NetworkApi {

    private static NetworkApi sInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private NetworkApi(Context context) {
        mContext = context;
        mRequestQueue = Volley
                .newRequestQueue(mContext.getApplicationContext());
    }

    public static NetworkApi getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkApi(context);
        }
        return sInstance;
    }

    public void getCompanyList(Listener<ResponseData> objectListener, ErrorListener errorListener, String tag) {
        ErrorListener networkErrorListener = new NetworkErrorListener<>(this.mContext, objectListener, errorListener);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json;");

        ListDataApi listDataApi = new ListDataApi(objectListener, networkErrorListener, headerMap);
        listDataApi.setTag(tag);
        mRequestQueue.add(listDataApi);
    }

    public void cancelAllRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private static class NetworkErrorListener<T> implements ErrorListener {
        private Context context;
        private Listener<T> successListener;
        private ErrorListener errorListener;
        private Class<T> clazz;

        public NetworkErrorListener(Context context, Listener<T> successListener, ErrorListener errorListener) {
            this.context = context;
            this.successListener = successListener;
            this.errorListener = errorListener;
            this.clazz = clazz;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (new NetworkErrorHandler(this.context, this.successListener, this.errorListener, this.clazz).handleError(volleyError)) {
                this.errorListener.onErrorResponse(volleyError);
            }
        }
    }


}
