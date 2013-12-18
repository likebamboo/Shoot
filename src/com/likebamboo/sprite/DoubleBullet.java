
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;

import com.likebamboo.util.Utils;

/**
 * 双子弹降落伞
 * 
 * @author likebamboo
 */
public class DoubleBullet extends CatchWeapon {

    public DoubleBullet(Context context, Handler handler) {
        super(context, handler);
        // TODO Auto-generated constructor stub]
        mType = TYPE_DOUBLE_BULLET;
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        mPaint = new Paint();
        mRect = new Rect();

        mBitmaps = Utils.getDoubleBullet(mContext);
        WIDTH = Utils.getNewWidth(mBitmaps[0].getWidth());
        HEIGHT = (int)(WIDTH * mBitmaps[0].getHeight() / (float)mBitmaps[0].getWidth());

        int i = WIDTH / 2 + rand.nextInt(Utils.DEVICE_WIDTH - WIDTH);
        int j = -HEIGHT / 2 - (rand.nextInt(HEIGHT));
        setPos(i, j);
    }

}
