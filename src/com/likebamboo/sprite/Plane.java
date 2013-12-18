
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import com.likebamboo.activity.MainActivity;
import com.likebamboo.factory.BulletFactory;
import com.likebamboo.shoot.R;
import com.likebamboo.util.PosUtils;
import com.likebamboo.util.Utils;
import com.likebamboo.view.ShootSurface;

public class Plane extends Sprite {

    /**
     * (surfaceView,FPS=30,也就是每33ms绘制一帧，198=6*33，每6帧发送一个子弹)
     */
    private final static int SHOOT_TIME = ShootSurface.FPS * 6;

    private final static int DOUBLE_BULLET_TIME = ShootSurface.FPS * 33 * 30;

    private Context mContext = null;

    private Paint mPaint = null;

    public static int WIDTH = 0;

    public static int HEIGHT = 0;

    /** 上一颗子弹发射的时间 **/
    private long mSendTime = 0L;

    /**
     * 获得双子弹的时间
     */
    private long mDoubleBulletTime = 0L;

    /**
     * 是否是双子弹
     */
    private boolean isDoubleBullet = false;

    /**
     * 死亡时的动画
     */
    private static Animation mKilledAnim = null;

    /**
     * 死亡动画的帧数
     */
    private static int mKilledAnimSize = -1;

    /**
     * 是否已死亡
     */
    private boolean isDead = false;

    /**
     * 死亡动画第几帧
     */
    private int mKilledAnimIdx = 0;

    public Plane(Context mContext, Handler handler) {
        super();
        this.mContext = mContext;
        this.mHandler = handler;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mRect = new Rect();
        loadPlane();
    }

    private void loadPlane() {
        mBitmaps = Utils.getMyPlane(mContext);
        WIDTH = Utils.getNewWidth(mBitmaps[0].getWidth());
        HEIGHT = (int)(WIDTH * mBitmaps[0].getHeight() / (float)mBitmaps[0].getWidth());
        setPos(Utils.DEVICE_WIDTH / 2, Utils.DEVICE_HEIGHT - HEIGHT / 2);
    }

    public void setPos(float i, float j) {
        if (i < WIDTH / 2) {// 飞机不能超出屏幕
            i = WIDTH / 2;
        }
        if (i > Utils.DEVICE_WIDTH - WIDTH / 2) {// 飞机不能超出屏幕
            i = Utils.DEVICE_WIDTH - WIDTH / 2;
        }

        if (j < HEIGHT / 2) {// 飞机不能超出屏幕
            j = HEIGHT / 2;
        }
        if (j > Utils.DEVICE_HEIGHT - HEIGHT / 2) {// 飞机中心不能超出屏幕
            j = Utils.DEVICE_HEIGHT - HEIGHT / 2;
        }
        if (mPos == null) {
            mPos = new Pos(i, j);
        } else {
            mPos.x = i;
            mPos.y = j;
        }
    }

    public Pos getPos() {
        return mPos;
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

    /**
     * 用于碰撞检测的Rect
     * 
     * @return
     */
    public Rect getCollisionRect() {
        int left = (int)(mPos.x - WIDTH / 5);
        int top = (int)(mPos.y - HEIGHT * 0.35);
        int right = (int)(mPos.x + WIDTH / 5);
        int bottom = (int)(mPos.y + HEIGHT * 0.25);
        return new Rect(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        // 如果本机已经死亡
        if (isDead) {
            // 如果死亡动画为空，先初始化
            if (mKilledAnim == null) {
                Bitmap[] bitmaps = Utils.getMyPlaneKilled(mContext);
                mKilledAnim = new Animation(bitmaps, false);
                mKilledAnimSize = bitmaps.length;
            }
            if (mKilledAnimIdx < mKilledAnimSize) {
                drawKilled(canvas);
            }
            return;
        }
        // TODO Auto-generated method stub
        calDrawRectByPos();
        canvas.drawBitmap(mBitmaps[mDrawWhich], null, mRect, mPaint);
        if (mDrawNext % 2 == 0) {
            mDrawWhich++;
        }
        if (mDrawWhich >= mBitmaps.length) {
            mDrawWhich = 0;
        }
        mDrawNext++;

        long now = System.currentTimeMillis();
        if (now - mSendTime > SHOOT_TIME) {
            if (isDoubleBullet) {
                BulletFactory.createBullet(mContext, Bullet.TYPE_BULLET_DOUBLE, mPos.x + WIDTH / 4,
                        mRect.top - Bullet.HEIGHT / 2).draw(canvas);
                BulletFactory.createBullet(mContext, Bullet.TYPE_BULLET_DOUBLE, mPos.x - WIDTH / 4,
                        mRect.top - Bullet.HEIGHT / 2).draw(canvas);
                if (now - mDoubleBulletTime > DOUBLE_BULLET_TIME) {
                    cancelDoubleBullet();
                }
            } else {
                BulletFactory.createBullet(mContext, Bullet.TYPE_BULLET_SINGLE, mPos.x,
                        mRect.top - Bullet.HEIGHT / 2).draw(canvas);
            }
            // 产生子弹发出的声音
            Utils.sendSoundMsg(mHandler, R.raw.bullet);

            mSendTime = now;
        }
        BulletFactory.drawAllBullet(canvas);
    }

    /**
     * 设置死亡状态
     * 
     * @param dead
     */
    public void setDead(boolean dead) {
        isDead = dead;
        // 通知主界面myplan 挂了
        Utils.sendSoundMsg(mHandler, R.raw.use_bomb);
    }

    /**
     * 获取死亡状态。
     * 
     * @return
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * 绘制死亡动画
     * 
     * @param canvas
     */
    public void drawKilled(Canvas canvas) {
        // TODO Auto-generated method stub
        if (mKilledAnimIdx < mKilledAnimSize) {
            mKilledAnim.drawFrame(canvas, mPaint, mRect, mKilledAnimIdx);
        }
        if (mDrawNext % 5 == 0) {
            mKilledAnimIdx++;
        }
        mDrawNext++;
        if (mKilledAnimIdx >= mKilledAnimSize) {
            // 通知主界面game over
            Message msg = mHandler.obtainMessage();
            msg.what = MainActivity.MSG_GAME_OVER;
            mHandler.sendMessage(msg);

            // 播放 game over 声音
            Utils.sendSoundMsg(mHandler, R.raw.game_over);
        }
    }

    /**
     * 重置本飞机
     */
    @Override
    public void reset() {
        setPos(Utils.DEVICE_WIDTH / 2, Utils.DEVICE_HEIGHT - HEIGHT / 2);
        BulletFactory.resetAllBullet();
        cancelDoubleBullet();
        isDead = false;
        mKilledAnimIdx = 0;
    }

    /**
     * 更新子弹的位置
     */
    public void update() {
        BulletFactory.updateAllBullet();
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    /**
     * 获得双子弹
     */
    public void catchDoubleBullet() {
        isDoubleBullet = true;
        mDoubleBulletTime = System.currentTimeMillis();
    }

    /**
     * 取消双子弹
     */
    private void cancelDoubleBullet() {
        isDoubleBullet = false;
        mDoubleBulletTime = 0L;
    }

}
