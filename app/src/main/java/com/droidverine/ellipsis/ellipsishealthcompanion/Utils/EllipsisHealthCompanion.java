package com.droidverine.ellipsis.ellipsishealthcompanion.Utils;

import android.app.Application;

/**
 * Created by DELL on 15-12-2017.
 */

public class EllipsisHealthCompanion extends Application {
    private static EllipsisHealthCompanion mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized EllipsisHealthCompanion getInstance() {
        return mInstance;
    }


}
