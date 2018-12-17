package com.andy.andymvp.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.andy.andymvp.data.ResponseData;
import com.andy.andymvp.utils.StringUtils;

import java.util.Map;

public class ListDataApi extends GsonRequest<ResponseData> {
    public ListDataApi(Response.Listener<ResponseData> listener,
                       Response.ErrorListener errorListener, Map<String, String> header) {
        super(Request.Method.GET,
                StringUtils.BOX_URL, ResponseData.class,
                listener, errorListener, header);
    }
}
