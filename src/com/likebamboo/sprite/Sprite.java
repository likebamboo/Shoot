
package com.likebamboo.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;

/**
 * @author likebamboo
 */
public abstract class Sprite {

    /**
     * 精灵所在矩形
     */
    protected Rect mRect;

    /**
     * 精灵图片(包括活动时的图片(可能为多张)以及被打击时的图片(一张))
     */
    protected Bitmap[] mBitmaps;

    /**
     * 当前绘制是哪张图片
     */
    protected int mDrawWhich = 0;

    /**
     * 是否该绘制下一张图，
     */
    protected int mDrawNext = 0;

    /**
     * 精灵中心点的位置
     */
    protected Pos mPos;

    /**
     * 精灵的速度
     */
    protected float mSpeed; // Move n pixels per step

    protected Handler mHandler;

    public abstract void draw(Canvas canvas);

    public abstract void clear();

    public abstract void reset();// 重置
}
