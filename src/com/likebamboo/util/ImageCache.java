
package com.likebamboo.util;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;

/**
 * 图片缓存工具类
 * 
 * @author likebamboo
 */
public class ImageCache {
    private static ImageCache mInstance = null;

    private ImageCache() {
    }

    public static ImageCache getInstance() {
        if (mInstance == null) {
            mInstance = new ImageCache();
        }
        return mInstance;
    }

    /**
     * 加载图片(从图片缓存中加载)
     * 
     * @param url 图片URL
     * @return
     */
    public Drawable loadImg(final String url) {
        String path = Utils.getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        final File f = new File(path + "/" + MD5.getMD5(url));

        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                Drawable d = Drawable.createFromStream(fis, "src");
                if (d != null) {
                    return d;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
