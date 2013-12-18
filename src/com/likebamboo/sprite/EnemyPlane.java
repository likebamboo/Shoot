
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;

import com.likebamboo.activity.MainActivity;
import com.likebamboo.shoot.R;
import com.likebamboo.util.PosUtils;
import com.likebamboo.util.Utils;

/**
 * @desc 敌机父类
 * @author <a href="mailto:likebamboo@163.com"
 *         target="_blank">likebamboo@163.com</a>
 * @create 2013-9-22
 * @Copyright 2013 <a href="http://liwentao.sinaapp.com"
 *            target="_blank">liwentao.sinaapp.com</a> Inc. All rights reserved.
 */
public abstract class EnemyPlane extends Sprite {

    public enum EnemyType {
        SMALL_ENEMY_PLANE, BIG_ENEMY_PLANE, BOSS_ENEMY_PLANE
    }

    public static final int IDX_KILLED_DEFAULT = -1;

    public static final int SPEED_NORMAL_SMALL = 8;

    public static final int SPEED_NORMAL_BIG = 6;

    public static final int SPEED_NORMAL_BOSS = 5;

    protected Context mContext = null;

    protected Paint mPaint = null;

    protected int mKilledAnimIdx = IDX_KILLED_DEFAULT;

    /**
     * 敌机的类型
     */
    private EnemyType mEnemyType;

    /**
     * 打中了几枪
     */
    private int mHitCount = 0;

    /**
     * 绘制击打图片。
     */
    private int mDrawHitAnim = 0;

    /**
     * 需要打击的枪数
     */
    private int mKillCount = 0;

    /**
     * 击落获得的分数
     */
    private int mKilledScore = 0;

    /**
     * 是否已经消失(或死去)
     */
    private boolean mDead = false;

    public EnemyPlane(Context ctx, EnemyType mType, Handler handler) {
        super();
        this.mContext = ctx;
        this.mEnemyType = mType;
        this.mHandler = handler;
        this.mHitCount = 0;
    }

    public int getKillCount() {
        return mKillCount;
    }

    public void setKillCount(int killedCount) {
        this.mKillCount = killedCount;
    }

    public void addHitted() {
        this.mHitCount++;
        mDrawHitAnim++;
    }

    public boolean isKilled() {
        return this.mHitCount >= this.mKillCount;
    }

    public EnemyType getEnemyType() {
        return mEnemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.mEnemyType = enemyType;
    }

    public void setKilledScore(int killedScore) {
        this.mKilledScore = killedScore;
    }

    public int getKilledScroe() {
        return mKilledScore;
    }

    public boolean isDead() {
        return mDead;
    }

    public void setDead(boolean dead) {
        mHitCount = 0;
        mDead = dead;
    }

    /**
     * 是否是最初状态 .
     * <P>
     * fix bug 有的情况下会出现 isGone = false, 而 mKilledAnimeIdx == 2 的情况。 所以在此增加判断。
     * 
     * @return
     */
    public boolean isDefState() {
        return isDead() && mKilledAnimIdx == IDX_KILLED_DEFAULT;
    }

    public void updatePos() {
        // TODO Auto-generated method stub
        if (mPos.y > Utils.DEVICE_HEIGHT + getHeight() / 2) {
            reset();
            return;
        }
        mPos.y += mSpeed;
    }

    /**
     * 计算绘制的位置(通过图片的中心点计算)
     */
    protected void calDrawRectByPos() {
        // TODO Auto-generated method stub
        PosUtils.calRectByPos(mRect, mPos, getWidth(), getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (!isDead() && mDrawHitAnim == 0) { // 敌机未死亡,而且处于正常状态。
            calDrawRectByPos();
            canvas.drawBitmap(mBitmaps[mDrawWhich], null, mRect, mPaint);
            if (mDrawNext % 3 == 0) {
                mDrawWhich++;
            }
            if (mDrawWhich >= mBitmaps.length - 1) {// 最后一个是 飞机 被打击时的图画
                mDrawWhich = 0;
            }
            mDrawNext++;
        } else if (!isDead() && mDrawHitAnim > 0) {// 未死亡，处于被打击状态
            Bitmap bitmap = mBitmaps[mBitmaps.length - 1];
            calDrawRectByPos();
            canvas.drawBitmap(bitmap, null, mRect, mPaint);
            mDrawNext = 0;
            mDrawWhich = 0;
            mDrawHitAnim++;
            if (mDrawHitAnim > 1) {
                mDrawHitAnim = 0;
            }
        } else if (mKilledAnimIdx > IDX_KILLED_DEFAULT) {// 敌机死亡
            drawKilled(canvas);
        }
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    public abstract void reset();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void drawKilled(Canvas canvas);

    /**
     * 爆炸
     * 
     * @param canvas
     */
    public void bomb(Canvas canvas) {
        mKilledAnimIdx = 0;
        // 分数改变
        Message msg = mHandler.obtainMessage();
        msg.what = MainActivity.MSG_SCORE_CHANGE;
        msg.arg1 = getKilledScroe();
        mHandler.sendMessage(msg);
        // 播放爆炸的声音
        sendBombMsg();

        drawKilled(canvas);
        setDead(true);
    }

    /**
     * handler发送爆炸声音
     */
    private void sendBombMsg() {
        int resId = 0;
        switch (mEnemyType) {
            case SMALL_ENEMY_PLANE: // 小飞机
                resId = R.raw.enemy1_down;
                break;
            case BIG_ENEMY_PLANE: // 打飞机
                resId = R.raw.enemy2_down;
                break;
            case BOSS_ENEMY_PLANE:// boss
                resId = R.raw.enemy3_down;
                break;
            default:
                resId = R.raw.enemy1_down;
                break;
        }
        Utils.sendSoundMsg(mHandler, resId);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.likebamboo.sprite.Sprite#checkCollision(com.likebamboo.sprite.Sprite)
     */
    public abstract boolean checkCollision(Canvas canvas, Sprite s);

}
