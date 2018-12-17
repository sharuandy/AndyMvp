package com.andy.andymvp.interfaces;

import android.content.Context;

public interface MainInteractor {
    void provideData(Context context, boolean isRestoring);

    void onDestroy();
}
