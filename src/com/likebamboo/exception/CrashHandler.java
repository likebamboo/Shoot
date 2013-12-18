
package com.likebamboo.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UncaughtExceptionHandler：线程未捕获异常处理器，用来处理未捕获异常。 如果程序出现了未捕获异常，默认会弹出系统中强制关闭对话框。
 * 我们需要实现此接口，并注册为程序中默认未捕获异常处理。 这样当未捕获异常发生时，就可以做一些个性化的异常处理操作。
 * 
 * @author liwt@273.cn 2013-04-09
 */
public class CrashHandler implements UncaughtExceptionHandler {
    // TAG
    public static final String TAG = "CrashHandler";

    // 系统默认的UncaughtExceptionHandler
    private UncaughtExceptionHandler mDefaultHandler;

    // CashHandler 的实例 , 单例模式
    private static CrashHandler instance = null;

    // 程序的Context对象
    private Context mContext = null;

    /*
     * 用来存储设备异常信息和错误信息 当然也可以使用Properties，因为Properties有一个很便捷的方法
     * properties.store(OutputStream out, String comments)，
     * 用来将Properties实例中的键值对外输到输出流中，但是在使用的过程中发现生成的文件中异常信息打印在同一行，看起来极为费劲
     */
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期，作为日志文件的一部分
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 单例模式
     * 
     * @return CrashHandler的一个实例
     */
    public static CrashHandler getInstance() {
        if (null == instance) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * 初始化默认的处理器
     * 
     * @param ctx 上下文对象
     */
    public void init(Context ctx) {
        mContext = ctx;
        // 获取默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置当前的处理器为默认的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果本程序没有处理异常，交给系统默认的程序，让其处理异常，
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * @param ex 抛出的异常
     * @return true：处理了该异常，false：没有处理该异常（交给系统默认的异常处理器处理）
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        // saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 保存错误信息到文件中
     * 
     * @param ex
     * @return 返回文件名，便于将文件传送到服务器
     */
    @SuppressWarnings("unused")
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + " = " + value + "\n");
        }

        Writer writer = new StringWriter();
        ex.printStackTrace();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        // 至此，设备信息和设备信息均已保存到sb中

        try {
            String time = sdf.format(new Date());
            String fileName = "crash_" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing log file...", e);
        }
        return null;
    }

    /**
     * 收集设备参数信息
     * 
     * @param ctx 上下文对象
     */
    private void collectDeviceInfo(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                // 获得版本名和版本号信息
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName：", versionName);
                infos.put("versionCode：", versionCode);
            }
            // 通过反射获取系统的硬件信息
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                // 暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value;
                try {
                    value = field.get(null).toString();
                    infos.put(name, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
            e.printStackTrace();
        }

    }
}
