
package com.likebamboo.util;

import android.graphics.Rect;

import com.likebamboo.sprite.Pos;

/**
 * 通过点计算精灵所在矩形的工具类
 * 
 * @author likebamboo
 */
public class PosUtils {

    /*
     * Calculate rect by center point
     * @param[in, out] mRect rect to calc
     * @param[in] mPos center point
     */
    public static void calRectByPos(Rect mRect, Pos mPos, int width, int height) {
        if (mPos == null) {
            mRect.left = 0;
            mRect.right = 100;
            mRect.top = 0;
            mRect.bottom = 100;
        }
        mRect.left = (int)(mPos.x - width / 2);
        mRect.right = mRect.left + width;
        mRect.top = (int)(mPos.y - height / 2);
        mRect.bottom = mRect.top + height;
    }

}
