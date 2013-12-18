
package com.likebamboo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.likebamboo.shoot.R;
import com.likebamboo.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 引导页
 * 
 * @author likebamboo
 */
public class Splash extends Activity {

    /**
     * 定时器
     */
    private Timer mTimer = null;

    /**
     * 定时器任务
     */
    private TimerTask mTask = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 获取屏幕宽高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Utils.DEVICE_WIDTH = metrics.widthPixels;
        Utils.DEVICE_HEIGHT = metrics.heightPixels;

        setContentView(R.layout.activity_splash);

        // TODO: do some db init word
        // new Handler() {
        // }.postDelayed(new Runnable() {
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // Intent i = new Intent(Splash.this, LoginActivity.class);
        // startActivity(i);
        // finish();
        // }
        // }, 3000L);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mTimer.schedule(mTask, 1000 * 3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mTimer) {
            mTimer.cancel();
            mTask.cancel();
        }
    }
}
