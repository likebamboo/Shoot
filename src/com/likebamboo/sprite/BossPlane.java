
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;

import com.likebamboo.shoot.R;
import com.likebamboo.util.Collision;
import com.likebamboo.util.Utils;

import java.util.Random;

/**
 * @desc 敌机中大的飞机
 * @author <a href="mailto:likebamboo@163.com"
 *         target="_blank">likebamboo@163.com</a>
 * @create 2013-9-23
 * @Copyright 2013 <a href="http://liwentao.sinaapp.com"
 *            target="_blank">likebamboo@163.com</a> Inc. All rights reserved.
 */
public class BossPlane extends EnemyPlane {

    public static final int KILLED_SCORE = 8000;

    public static final int KILLED_COUNT = 15;

    private static int WIDTH = 0;

    private static int HEIGHT = 0;

    private Random rand = new Random();

    private static Animation mKilledAnim = null;

    private static int mKilledAnimSize = -1;

    public BossPlane(Context ctx, EnemyType mType, Handler handler) {
        super(ctx, mType, handler);
        // TODO Auto-generated constructor stub
        setKilledScore(KILLED_SCORE);
        setKillCount(KILLED_COUNT);
        mSpeed = SPEED_NORMAL_BOSS * Utils.getScale();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mRect = new Rect();

        mBitmaps = Utils.getBossPlane(mContext);
        WIDTH = Utils.getNewWidth(mBitmaps[0].getWidth());
        HEIGHT = (int)(WIDTH * mBitmaps[0].getHeight() / (float)mBitmaps[0].getWidth());
    }

    @Override
    public void reset() {
        mDrawNext = 0;
        setDead(true);

        int i = WIDTH / 2 + rand.nextInt(Utils.DEVICE_WIDTH - WIDTH);
        int j = -HEIGHT / 2 - (rand.nextInt(HEIGHT));

        if (mPos == null) {
            mPos = new Pos(i, j);
        } else {
            mPos.x = i;
            mPos.y = j;
        }

        calDrawRectByPos();
    }

    public Pos getPos() {
        return mPos;
    }

    @Override
    public boolean checkCollision(Canvas canvas, Sprite s) {
        // TODO Auto-generated method stub
        if (s instanceof Bullet) { // 子弹与小飞机碰撞检测
            if (Collision.isRectCls(mRect, ((Bullet)s).getRect())) {
                addHitted();
                // 播放被打击的声音
                Utils.sendSoundMsg(mHandler, R.raw.button);
                if (isKilled()) {
                    bomb(canvas);
                }
                s.reset();
                return true;
            }
        } else if (s instanceof Plane) {
            if (!((Plane)s).isDead()
                    && Collision.isRectCls(mRect, ((Plane)s).getCollisionRect())) {
                ((Plane)s).setDead(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public void drawKilled(Canvas canvas) {
        // TODO Auto-generated method stub
        if (mKilledAnim == null) {
            Bitmap[] bitmaps = Utils.getBossPlaneKilled(mContext);
            mKilledAnim = new Animation(bitmaps, false);
            mKilledAnimSize = bitmaps.length;
        }
        if (mKilledAnimIdx < mKilledAnimSize) {
            mKilledAnim.drawFrame(canvas, mPaint, mRect, mKilledAnimIdx);
        }
        if (mDrawNext % 2 == 0) {
            mKilledAnimIdx++;
        }
        mDrawNext++;
        if (mKilledAnimIdx >= mKilledAnimSize) {
            mKilledAnimIdx = IDX_KILLED_DEFAULT;
            reset();
        }
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return WIDTH;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return HEIGHT;
    }

}
