
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import com.likebamboo.activity.MainActivity;
import com.likebamboo.shoot.R;
import com.likebamboo.util.Collision;
import com.likebamboo.util.PosUtils;
import com.likebamboo.util.Utils;

import java.util.Random;

/**
 * 降落伞武器
 * 
 * @author likebamboo
 */
public abstract class CatchWeapon extends Sprite {

    protected static final int TYPE_BOMB = 0x10001;

    protected static final int TYPE_DOUBLE_BULLET = 0x10002;

    protected Context mContext = null;

    protected Paint mPaint = null;

    protected int WIDTH = 0;

    protected int HEIGHT = 0;

    protected Random rand = new Random();

    protected boolean isGone = false;

    protected int mType = TYPE_BOMB;

    public CatchWeapon(Context context, Handler handler) {
        super();
        mContext = context;
        mSpeed = Utils.DEVICE_HEIGHT / 80;
        mHandler = handler;
        init();
        isGone = false;
    }

    protected abstract void init();

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

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        calDrawRectByPos();
        canvas.drawBitmap(mBitmaps[mDrawWhich], null, mRect, mPaint);
    }

    public void updatePos() {
        mPos.y += mSpeed;
        if (mDrawNext % 2 == 0) {
            if (mSpeed < Utils.DEVICE_HEIGHT / 40) {
                mSpeed += 1;
            }
        }
        mDrawNext++;
        if (mPos.y > Utils.DEVICE_HEIGHT + HEIGHT / 2) {
            isGone = true;
        }
    }

    protected void setPos(float i, float j) {
        // TODO Auto-generated method stub
        mPos = new Pos(i, j);
    }

    public boolean isGone() {
        return isGone;
    }

    public void setGone(boolean gone) {
        isGone = gone;
        mDrawNext = 0;
    }

    public void checkCollision(Sprite s) {
        if (s instanceof Plane) {
            if (Collision.isRectCls(mRect, ((Plane)s).getRect())) {
                setGone(true);
                switch (mType) {
                    case TYPE_BOMB:
                        // 炸弹数量更新
                        Message msg = mHandler.obtainMessage();
                        msg.what = MainActivity.MSG_BOMB_CHANGE;
                        mHandler.sendMessage(msg);
                        // 获得炸弹
                        Utils.sendSoundMsg(mHandler, R.raw.get_bomb);

                        break;
                    case TYPE_DOUBLE_BULLET:
                        ((Plane)s).catchDoubleBullet();
                        // 获得双子弹
                        Utils.sendSoundMsg(mHandler, R.raw.get_double_laser);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}
