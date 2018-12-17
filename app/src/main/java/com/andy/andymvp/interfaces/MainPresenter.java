package com.andy.andymvp.interfaces;

import android.content.Context;

public interface MainPresenter {
    void getDataForList(Context context, boolean isRestoring);

    void onDestroy();
}
