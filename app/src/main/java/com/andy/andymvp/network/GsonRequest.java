package com.andy.andymvp.network;


import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.andy.andymvp.utils.AppLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson mGson;
    private final Class<T> mClazz;
    private final Listener<T> mListener;
    //private final Map<String, String> mParams;
    private String mParams;
    Map<String, String> responseHeaders;
    private String[] mParams_;
    private Map<String, String> mHeaders = new HashMap<>();
    private ErrorListener mErrorListener;

    private void printReq(String reqBody) {
        int maxLogSize = 2000;
        if (reqBody != null)
            for (int i = 0; i <= reqBody.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > reqBody.length() ? reqBody.length() : end;
                AppLog.e("body", reqBody.substring(start, end));
            }
    }

    @Override
    public Request<?> setCacheEntry(Cache.Entry entry) {
        return super.setCacheEntry(entry);
    }

    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        mGson = new Gson();
        mParams = null;
        mErrorListener = errorListener;

        setTimeout(90000); // 30 seconds
    }

    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener, Gson gson) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        mGson = gson;
        mParams = null;
        mHeaders = null;
        mErrorListener = errorListener;

        setTimeout(90000); // 30 seconds
    }

    // login specific GSON request
    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener, String param) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        mGson = new Gson();
        this.mParams = param;
        mErrorListener = errorListener;
        printReq(mParams);
        setTimeout(90000); // 30 seconds
    }

    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener, Map<String, String> headers, String param) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        this.mHeaders = headers;
        mGson = new Gson();
        this.mParams = param;
        mErrorListener = errorListener;
        System.out.println("mParams = " + headers);
        System.out.println("url = " + url);
        printReq(mParams);
        setTimeout(90000); // 30 seconds
    }


    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
        //this.mParams = params;
        this.mHeaders = headers;
        mGson = new Gson();
        this.mParams = null;
        System.out.print("url = " + url);
        printReq(mParams);
        setTimeout(90000); // 30 seconds
    }


    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    private void setTimeout(int timeout) {
        RetryPolicy policy = new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(policy);
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            if (mParams != null) {
                byte[] bytes = mParams.getBytes();
                return mParams.getBytes(getParamsEncoding());
            }
        } catch (Exception e) {
            AppLog.e(AppLog.logMsg, e);
        }
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (isJSONObject(json)) {
                if (!TextUtils.isEmpty(json)) {
                    JSONObject jsonObject = new JSONObject(json);
                    jsonObject.put("response_headers", new JSONObject(response.headers));
                    JSONObject statusCodeObj = new JSONObject();
                    statusCodeObj.put("STATUS_CODE", response.statusCode);
                    jsonObject.put("statuscode", statusCodeObj);
                    return Response.success(mGson.fromJson(jsonObject.toString(), mClazz),
                            HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("response_headers", new JSONObject(response.headers));
                    JSONObject statusCodeObj = new JSONObject();
                    statusCodeObj.put("STATUS_CODE", response.statusCode);
                    jsonObject.put("statuscode", statusCodeObj);
                    return Response.success(mGson.fromJson(jsonObject.toString(), mClazz),
                            HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                return Response.success(mGson.fromJson(json, mClazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            AppLog.e(AppLog.logMsg, e);
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            AppLog.e(AppLog.logMsg, e);
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            AppLog.e(AppLog.logMsg, e);
            return Response.error(new ParseError(e));
        }
    }

    private boolean isJSONObject(String json) {
        if (json.length() > 0) {
            String firstChar = String.valueOf(json.charAt(0));

            if (firstChar.equalsIgnoreCase("[")) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    protected String getBaseUrl() {
        return "dropbox.com";
    }
}