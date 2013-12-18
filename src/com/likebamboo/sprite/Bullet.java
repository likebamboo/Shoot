
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.likebamboo.util.PosUtils;
import com.likebamboo.util.Utils;

/**
 * @desc 子弹类
 * @author <a href="mailto:likebamboo@163.com"
 *         target="_blank">likebamboo@163.com</a>
 * @create 2013-9-16
 * @Copyright 2013 <a href="http://liwentao.sinaapp.com"
 *            target="_blank">likebamboo@163.com</a> Inc. All rights reserved.
 */
public class Bullet extends Sprite {

    public static int TYPE_BULLET_SINGLE = 0x10001;

    public static int TYPE_BULLET_DOUBLE = 0x10002;

    private static final float BULLET_SPEED = 50F;

    private Context mContext = null;

    private static Bitmap mBitmapSingle = null;

    private static Bitmap mBitmapDouble = null;

    private Paint mPaint = null;

    public static int WIDTH = 0;

    public static int HEIGHT = 0;

    private int mType = TYPE_BULLET_SINGLE;

    /**
     * 是否已经超出屏幕
     */
    private boolean isGone = false;

    public Bullet(Context context, int type) {
        super();
        mContext = context;
        mPaint = new Paint();
        mRect = new Rect();
        mSpeed = BULLET_SPEED * Utils.getScale();

        mType = type;
        // 双枪
        if (type == TYPE_BULLET_DOUBLE) {
            if (mBitmapDouble == null) {
                mBitmapDouble = Utils.getBlueBullet(mContext);
            }
        } else {// 单枪
            if (mBitmapSingle == null) {
                mBitmapSingle = Utils.getYellowBullet(mContext);
            }
        }
        WIDTH = mBitmapSingle.getWidth();
        HEIGHT = mBitmapSingle.getHeight();
    }

    public void setPos(float i, float j) {
        if (mPos == null) {
            mPos = new Pos(i, j);
        } else {
            mPos.x = i;
            mPos.y = j;
        }
        isGone = false;
    }

    public void setGone(boolean gone) {
        isGone = gone;
    }

    /**
     * 计算绘制的位置(通过图片的中心点计算)
     */
    protected void calDrawRectByPos() {
        // TODO Auto-generated method stub
        PosUtils.calRectByPos(mRect, mPos, WIDTH, HEIGHT);
    }

    public Rect getRect() {
        return mRect;
    }

    public void updatePos() {
        mPos.y -= mSpeed;
        if (mPos.y < 0) {
            isGone = true;
        }
    }

    public boolean isGone() {
        return isGone;
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        calDrawRectByPos();
        if (mType == TYPE_BULLET_SINGLE) {
            canvas.drawBitmap(mBitmapSingle, null, mRect, mPaint);
        } else {
            canvas.drawBitmap(mBitmapDouble, null, mRect, mPaint);
        }
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        // Utils.recycleBitmap(mBitmap);
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        setGone(true);
    }

    /**
     * 获得子弹的类型
     * 
     * @return
     */
    public int getType() {
        return mType;
    }
}
