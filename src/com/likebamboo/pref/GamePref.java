
package com.likebamboo.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 与xml数据文件相关的操作
 * 
 * @author likebamboo
 */
public class GamePref {
    public static final String BACK_MUSIC = "back_music";

    public static final String GAME_MUSIC = "game_music";

    private static GamePref mInstance = null;

    private Context mContext = null;

    private static String PREF_NAME = "game_pref";

    private SharedPreferences mSettings = null;

    private SharedPreferences.Editor mEditor = null;

    private GamePref(Context ctx) {
        mContext = ctx.getApplicationContext();
        mSettings = mContext.getSharedPreferences(PREF_NAME, 0);
        mEditor = mSettings.edit();
    }

    public static GamePref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GamePref(context);
        }
        return mInstance;
    }

    /**
     * @param user
     * @return
     */
    public boolean contains(String user) {
        return mSettings.contains(user);
    }

    /**
     * 背景音乐是否开，默认为开
     * 
     * @return
     */
    public boolean getBackMusic() {
        return mSettings.getBoolean(BACK_MUSIC, true);
    }

    /**
     * 设置背景音乐
     * 
     * @param on
     */
    public void setBackMusic(boolean on) {
        mEditor.putBoolean(BACK_MUSIC, on);
        mEditor.commit();
    }

    /**
     * 游戏音效是否开，默认为开
     * 
     * @return
     */
    public boolean getGameMusic() {
        return mSettings.getBoolean(GAME_MUSIC, true);
    }

    /**
     * 设置游戏音效
     * 
     * @param on
     */
    public void setGameMusic(boolean on) {
        mEditor.putBoolean(GAME_MUSIC, on);
        mEditor.commit();
    }

    /**
     * 获取字符串
     * 
     * @param key key值
     * @return
     */
    public String loadString(String key) {
        return mSettings.getString(key, "");
    }

    /**
     * 保存字符串
     * 
     * @param key key值
     * @param value value值
     * @return
     */
    public boolean putString(String key, String value) {
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    /**
     * 获取整型值
     * 
     * @param key key值
     * @return
     */
    public int loadInt(String key) {
        return mSettings.getInt(key, -1);
    }

    /**
     * 获取整型值
     * 
     * @param key key值
     * @return
     */
    public long loadLong(String key, long defValue) {
        return mSettings.getLong(key, defValue);
    }

    /**
     * 保存整型值
     * 
     * @param key key值
     * @param value value值
     * @return
     */
    public boolean putLong(String key, long value) {
        mEditor.putLong(key, value);
        return mEditor.commit();
    }
}
