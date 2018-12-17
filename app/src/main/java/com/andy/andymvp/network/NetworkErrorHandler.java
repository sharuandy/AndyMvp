package com.andy.andymvp.network;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.andy.andymvp.utils.AppUtils;
import com.andy.andymvp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkErrorHandler<T> {

    private final Class<T> clazz;

    private Context context;

    private Response.Listener<T> successListener;
    private Response.ErrorListener errorListener;

    public NetworkErrorHandler(Context context, Response.Listener<T> successListener, Response.ErrorListener errorListener, Class<T> clazz) {
        this.context = context;
        this.successListener = successListener;
        this.errorListener = errorListener;
        this.clazz = clazz;
    }

    public boolean handleError(VolleyError error) {
        // check network is available or not
        if (!AppUtils.isNetworkAvailable(context)) {
            //  showMessage(TConstants.NETWORK_NOT_AVAILABLE);
            showMessage("Oops, no internet or wifi detected.... keep calm. Wait for the signal and refresh.");
            return true;
        }

        if (error == null || error.networkResponse == null) {
            showMessage(StringUtils.toastInteralServalError);
            return true;
        }

        return handleError(error.networkResponse);
    }

    private boolean handleError(NetworkResponse networkResponse) {
        int statusCode = networkResponse.statusCode;
        JSONObject response = parseData(networkResponse.data);
        if (statusCode == 500) {
            showMessage(StringUtils.toastBadRequestError + ":" + statusCode);
            return true;
        }

        if (statusCode == 401) {
            showMessage(StringUtils.toastBadRequestError + ":" + statusCode);
            return true;

        }

        if (statusCode == 400) {
            showMessage(StringUtils.toastBadRequestError + ":" + statusCode);
            return true;
        }

        if (statusCode == 404) {
            showMessage(StringUtils.toastBadRequestError + ":" + statusCode);
            return true;
        }
        showMessage(StringUtils.toastInteralServalError);
        return true;

    }

    private void showMessage(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

    private String getErrorCode(JSONObject response) {
        if (response == null)
            return null;
        return this.getField(response, "errorCode");
    }

    private JSONObject parseData(byte[] data) {
        if (data == null) {
            return null;
        }
        String json = new String(data);
        if (json.isEmpty()) {
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
    }

    private String getField(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

}

