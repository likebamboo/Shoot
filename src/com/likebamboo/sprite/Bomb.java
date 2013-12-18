
package com.likebamboo.sprite;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;

import com.likebamboo.util.Utils;

/**
 * 炸弹降落伞
 * 
 * @author likebamboo
 */
public class Bomb extends CatchWeapon {

    public Bomb(Context context, Handler handler) {
        super(context, handler);
        // TODO Auto-generated constructor stub]
        mType = TYPE_BOMB;
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        mPaint = new Paint();
        mRect = new Rect();

        mBitmaps = Utils.getBomb(mContext);
        WIDTH = Utils.getNewWidth(mBitmaps[0].getWidth());
        HEIGHT = (int)(WIDTH * mBitmaps[0].getHeight() / (float)mBitmaps[0].getWidth());

        int i = WIDTH / 2 + rand.nextInt(Utils.DEVICE_WIDTH - WIDTH);
        int j = -HEIGHT / 2 - (rand.nextInt(HEIGHT));
        setPos(i, j);
    }
}
