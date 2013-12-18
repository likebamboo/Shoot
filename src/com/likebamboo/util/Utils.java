
package com.likebamboo.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.likebamboo.activity.MainActivity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static final boolean FIX = true;

    public static final String TENCENT_ID = "100548215";

    // ==========================================================================
    /** 设备的宽度和高度 */
    public static int DEVICE_WIDTH = 480;

    public static int DEVICE_HEIGHT = 800;

    /**
     * 存放所有精灵的大图片
     */
    public static Bitmap mSpriteBitmap = null;

    /**
     * 游戏背景图片
     */
    public static Bitmap mBackgroundBm = null;

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getBitmapFromAssets(Context ctx, String fileName) {
        Bitmap image = null;
        AssetManager am = ctx.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 从Assets中读取图片
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context ctx, String fileName, int x, int y, int width,
            int height) {
        if (mSpriteBitmap == null) {
            mSpriteBitmap = getBitmapFromAssets(ctx, ImageConstants.SHOOT_IMG);
        }
        Bitmap newImg = null;
        if (TextUtils.isEmpty(fileName)) {
            newImg = Bitmap.createBitmap(mSpriteBitmap, x, y, width, height);
        } else {
            newImg = Bitmap.createBitmap(getBitmapFromAssets(ctx, fileName), x, y, width, height);
        }
        return newImg;
    }

    /**
     * 从Assets中读取图片
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap getGameBackground(Context ctx) {
        if (mBackgroundBm == null) {
            mBackgroundBm = getBitmapFromAssets(ctx, ImageConstants.SHOOT_BACKGROUND_IMG,
                    ImageConstants.GAME_BACKGROUND_IMG_POS_X,
                    ImageConstants.GAME_BACKGROUND_IMG_POS_Y,
                    ImageConstants.GAME_BACKGROUND_IMG_WIDTH,
                    ImageConstants.GAME_BACKGROUND_IMG_HEIGHT);
        }
        return mBackgroundBm;
    }

    /**
     * 从Assets中读取图片
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap getTextLog(Context ctx) {
        return getBitmapFromAssets(ctx, ImageConstants.SHOOT_BACKGROUND_IMG,
                ImageConstants.GAME_LOADING_TEXT_IMG_POS_X,
                ImageConstants.GAME_LOADING_TEXT_IMG_POS_Y,
                ImageConstants.GAME_LOADING_TEXT_IMG_WIDTH,
                ImageConstants.GAME_LOADING_TEXT_IMG_HEIGHT);
    }

    /**
     * 从Assets中读取图片
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getLoadingPlanes(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[3];
        bitmaps[0] = getBitmapFromAssets(ctx, ImageConstants.SHOOT_BACKGROUND_IMG,
                ImageConstants.GAME_LOADING_PLANE_1_POS_X,
                ImageConstants.GAME_LOADING_PLANE_1_POS_Y,
                ImageConstants.GAME_LOADING_PLANE_1_WIDTH,
                ImageConstants.GAME_LOADING_PLANE_1_HEIGHT);
        bitmaps[1] = getBitmapFromAssets(ctx, ImageConstants.SHOOT_BACKGROUND_IMG,
                ImageConstants.GAME_LOADING_PLANE_2_POS_X,
                ImageConstants.GAME_LOADING_PLANE_2_POS_Y,
                ImageConstants.GAME_LOADING_PLANE_2_WIDTH,
                ImageConstants.GAME_LOADING_PLANE_2_HEIGHT);
        bitmaps[2] = getBitmapFromAssets(ctx, ImageConstants.SHOOT_BACKGROUND_IMG,
                ImageConstants.GAME_LOADING_PLANE_3_POS_X,
                ImageConstants.GAME_LOADING_PLANE_3_POS_Y,
                ImageConstants.GAME_LOADING_PLANE_3_WIDTH,
                ImageConstants.GAME_LOADING_PLANE_3_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 炸弹降落伞
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getBomb(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[1];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.BOMB_POS_X,
                ImageConstants.BOMB_POS_Y, ImageConstants.BOMB_WIDTH, ImageConstants.BOMB_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 双子弹降落伞
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getDoubleBullet(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[1];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.DOUBLE_LASER_POS_X,
                ImageConstants.DOUBLE_LASER_POS_Y, ImageConstants.DOUBLE_LASER_WIDTH,
                ImageConstants.DOUBLE_LASER_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 我的战斗机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getMyPlane(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[2];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.MY_PLANE_FLYING_POS_X,
                ImageConstants.MY_PLANE_FLYING_POS_Y, ImageConstants.MY_PLANE_FLYING_WIDTH,
                ImageConstants.MY_PLANE_HEIGHT);
        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.MY_PLANE_POS_X,
                ImageConstants.MY_PLANE_POS_Y, ImageConstants.MY_PLANE_WIDTH,
                ImageConstants.MY_PLANE_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 我的战斗机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getMyPlaneKilled(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[3];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.MY_PLANE_BOMB_POS_X_1,
                ImageConstants.MY_PLANE_BOMB_POS_Y_1, ImageConstants.MY_PLANE_BOMB_WIDTH_1,
                ImageConstants.MY_PLANE_BOMB_HEIGHT_1);
        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.MY_PLANE_BOMB_POS_X_2,
                ImageConstants.MY_PLANE_BOMB_POS_Y_2, ImageConstants.MY_PLANE_BOMB_WIDTH_2,
                ImageConstants.MY_PLANE_BOMB_HEIGHT_2);
        bitmaps[2] = getBitmapFromAssets(ctx, "", ImageConstants.MY_PLANE_BOMB_POS_X_3,
                ImageConstants.MY_PLANE_BOMB_POS_Y_3, ImageConstants.MY_PLANE_BOMB_WIDTH_3,
                ImageConstants.MY_PLANE_BOMB_HEIGHT_3);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 敌机中的小飞机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getSmallPlane(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[1];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.SMALL_PLANE_POS_X,
                ImageConstants.SMALL_PLANE_POS_Y, ImageConstants.SMALL_PLANE_WIDTH,
                ImageConstants.SMALL_PLANE_HEIGHT);
        return bitmaps;
    }

    public static Bitmap[] getSmallPlaneKilled(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[4];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.SMALL_PLANE_FIGHTING_POS_X,
                ImageConstants.SMALL_PLANE_FIGHTING_POS_Y,
                ImageConstants.SMALL_PLANE_FIGHTING_WIDTH,
                ImageConstants.SMALL_PLANE_FIGHTING_HEIGHT);

        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.SMALL_PLANE_KILLED_POS_X,
                ImageConstants.SMALL_PLANE_KILLED_POS_Y, ImageConstants.SMALL_PLANE_KILLED_WIDTH,
                ImageConstants.SMALL_PLANE_KILLED_HEIGHT);

        bitmaps[2] = getBitmapFromAssets(ctx, "", ImageConstants.SMALL_PLANE_KILLED_POS_X_3,
                ImageConstants.SMALL_PLANE_KILLED_POS_Y_3,
                ImageConstants.SMALL_PLANE_KILLED_WIDTH_3,
                ImageConstants.SMALL_PLANE_KILLED_HEIGHT_3);

        bitmaps[3] = getBitmapFromAssets(ctx, "", ImageConstants.SMALL_PLANE_ASHED_POS_X,
                ImageConstants.SMALL_PLANE_ASHED_POS_Y, ImageConstants.SMALL_PLANE_ASHED_WIDTH,
                ImageConstants.SMALL_PLANE_ASHED_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 敌机中的大飞机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getBigPlane(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[2];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_POS_X,
                ImageConstants.BIG_PLANE_POS_Y, ImageConstants.BIG_PLANE_WIDTH,
                ImageConstants.BIG_PLANE_HEIGHT);

        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_FIGHTING_POS_X,
                ImageConstants.BIG_PLANE_FIGHTING_POS_Y, ImageConstants.BIG_PLANE_FIGHTING_WIDTH,
                ImageConstants.BIG_PLANE_FIGHTING_HEIGHT);
        return bitmaps;
    }

    public static Bitmap[] getBigPlaneKilled(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[4];

        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_HITTED_POS_X,
                ImageConstants.BIG_PLANE_HITTED_POS_Y, ImageConstants.BIG_PLANE_HITTED_WIDTH,
                ImageConstants.BIG_PLANE_HITTED_HEIGHT);
        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_BADDLY_WOUNDED_POS_X,
                ImageConstants.BIG_PLANE_BADDLY_WOUNDED_POS_Y,
                ImageConstants.BIG_PLANE_BADDLY_WOUNDED_WIDTH,
                ImageConstants.BIG_PLANE_BADDLY_WOUNDED_HEIGHT);
        bitmaps[2] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_KILLED_POS_X,
                ImageConstants.BIG_PLANE_KILLED_POS_Y, ImageConstants.BIG_PLANE_KILLED_WIDTH,
                ImageConstants.BIG_PLANE_KILLED_HEIGHT);
        bitmaps[3] = getBitmapFromAssets(ctx, "", ImageConstants.BIG_PLANE_ASHED_POS_X,
                ImageConstants.BIG_PLANE_ASHED_POS_Y, ImageConstants.BIG_PLANE_ASHED_WIDTH,
                ImageConstants.BIG_PLANE_ASHED_HEIGHT);
        return bitmaps;
    }

    /**
     * 从Assets中读取图片 敌机中的BOSS
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap[] getBossPlane(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[3];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_POS_X,
                ImageConstants.BOSS_PLANE_POS_Y, ImageConstants.BOSS_PLANE_WIDTH,
                ImageConstants.BOSS_PLANE_HEIGHT);
        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_POS_X_2,
                ImageConstants.BOSS_PLANE_POS_Y_2, ImageConstants.BOSS_PLANE_WIDTH_2,
                ImageConstants.BOSS_PLANE_HEIGHT_2);
        bitmaps[2] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_FIGHTING_POS_X,
                ImageConstants.BOSS_PLANE_FIGHTING_POS_Y, ImageConstants.BOSS_PLANE_FIGHTING_WIDTH,
                ImageConstants.BOSS_PLANE_FIGHTING_HEIGHT);
        return bitmaps;
    }

    public static Bitmap[] getBossPlaneKilled(Context ctx) {
        Bitmap[] bitmaps = new Bitmap[6];
        bitmaps[0] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_HITTED_POS_X,
                ImageConstants.BOSS_PLANE_HITTED_POS_Y, ImageConstants.BOSS_PLANE_HITTED_WIDTH,
                ImageConstants.BOSS_PLANE_HITTED_HEIGHT);

        bitmaps[1] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_HITTED_POS_X_2,
                ImageConstants.BOSS_PLANE_HITTED_POS_Y_2, ImageConstants.BOSS_PLANE_HITTED_WIDTH_2,
                ImageConstants.BOSS_PLANE_HITTED_HEIGHT_2);
        bitmaps[2] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_HITTED_POS_X_3,
                ImageConstants.BOSS_PLANE_HITTED_POS_Y_3, ImageConstants.BOSS_PLANE_HITTED_WIDTH_3,
                ImageConstants.BOSS_PLANE_HITTED_HEIGHT_3);

        bitmaps[3] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_BADDLY_WOUNDED_POS_X,
                ImageConstants.BOSS_PLANE_BADDLY_WOUNDED_POS_Y,
                ImageConstants.BOSS_PLANE_BADDLY_WOUNDED_WIDTH,
                ImageConstants.BOSS_PLANE_BADDLY_WOUNDED_HEIGHT);
        bitmaps[4] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_KILLED_POS_X,
                ImageConstants.BOSS_PLANE_KILLED_POS_Y, ImageConstants.BOSS_PLANE_KILLED_WIDTH,
                ImageConstants.BOSS_PLANE_KILLED_HEIGHT);
        bitmaps[5] = getBitmapFromAssets(ctx, "", ImageConstants.BOSS_PLANE_ASHED_POS_X,
                ImageConstants.BOSS_PLANE_ASHED_POS_Y, ImageConstants.BOSS_PLANE_ASHED_WIDTH,
                ImageConstants.BOSS_PLANE_ASHED_HEIGHT);

        return bitmaps;
    }

    /**
     * 从Assets中读取图片 我的战斗机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap getYellowBullet(Context ctx) {
        return getBitmapFromAssets(ctx, "", ImageConstants.YELLOW_BULLET_POS_X,
                ImageConstants.YELLOW_BULLET_POS_Y, ImageConstants.YELLOW_BULLET_WIDTH,
                ImageConstants.YELLOW_BULLET_HEIGHT);
    }

    /**
     * 从Assets中读取图片 我的战斗机
     * 
     * @param ctx 上下文对象
     * @param fileName 图片路径
     * @param x 起点 x
     * @param y 起点 y
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static Bitmap getBlueBullet(Context ctx) {
        return getBitmapFromAssets(ctx, "", ImageConstants.BLUE_BULLET_POS_X,
                ImageConstants.BLUE_BULLET_POS_Y, ImageConstants.BLUE_BULLET_WIDTH,
                ImageConstants.BLUE_BULLET_HEIGHT);
    }

    /**
     * 释放bitmap资源
     * 
     * @param bitmap bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 取得屏幕宽高比
     * 
     * @return
     */
    public static float getScreenRatio() {
        return Utils.DEVICE_WIDTH / (float)Utils.DEVICE_HEIGHT;
    }

    /**
     * 获取设备的密度
     * 
     * @param context 上下文对象
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * 
     * @param pxValue
     * @param scale （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dp(Context ctx, float pxValue) {
        return (int)(pxValue / getDensity(ctx) + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     * @param scale （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(Context ctx, float dipValue) {
        return (int)(dipValue * getDensity(ctx) + 0.5f);
    }

    /**
     * 缩放比例
     * 
     * @return
     */
    public static float getScale() {
        return Utils.DEVICE_WIDTH / (float)ImageConstants.GAME_BACKGROUND_IMG_WIDTH;
    }

    /**
     * 按比例缩放图片
     * 
     * @param mWidth
     * @return
     */
    public static int getNewWidth(int mWidth) {
        return (int)(mWidth * getScale());
    }

    /**
     * 发送声音广播
     * 
     * @param handler
     * @param resId
     */
    public static void sendSoundMsg(Handler handler, int resId) {
        Message msg = handler.obtainMessage();
        msg.what = MainActivity.MSG_SOUND;
        msg.arg1 = resId;
        handler.sendMessage(msg);
    }

    /**
     * 格式化Html
     * 
     * @param text
     * @return
     */
    public static Spanned htmlFormat(String text) {
        return Html.fromHtml(text);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean CheckNetworkConnection(Context context) {
        ConnectivityManager con = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // 当前网络不可用
            return false;
        }
        return true;
    }

    /**
     * 判断wifi网络是否可用
     */
    public static boolean IsWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 获取缓存文件夹路径
     * 
     * @return
     */
    public static String getPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = new File(Environment.getExternalStorageDirectory() + "/images/");
            if (!path.exists()) {
                path.mkdirs();
            }
            return path.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取缓存文件夹路径
     * 
     * @return
     */
    public static File getCacheFile(String name) {
        if (TextUtils.isEmpty(getPath())) {
            return null;
        }
        File f = new File(getPath() + "/" + name);
        if (f.exists()) {
            f.delete();
        }
        return f;
    }

    /**
     * 讲图片缓存到本地文件
     * 
     * @param inputStream 输入流
     */
    public static boolean writeCacheFile(String url, InputStream inputStream) {
        DataInputStream in = new DataInputStream(inputStream);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Utils.getCacheFile(MD5.getMD5(url)));
            byte[] buffer = new byte[1024];
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
