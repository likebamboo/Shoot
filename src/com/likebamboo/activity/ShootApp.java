
package com.likebamboo.activity;

import android.app.Application;

import com.likebamboo.exception.CrashHandler;

/**
 * @author likebamboo
 */
public class ShootApp extends Application {
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        /** 设置本应用程序的默认崩溃处理器 */
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

}
