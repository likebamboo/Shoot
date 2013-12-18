
package com.likebamboo.factory;

import android.content.Context;
import android.graphics.Canvas;

import com.likebamboo.sprite.Bullet;

import java.util.ArrayList;

public class BulletFactory {

    private static int POOL_SIZE = 10;

    private static ArrayList<Bullet> mBullets = new ArrayList<Bullet>();

    public static Bullet createBullet(Context ctx, int type, float x, float y) {
        if (mBullets.size() < POOL_SIZE) {
            Bullet bullet = null;
            bullet = new Bullet(ctx, type);
            bullet.setPos(x, y);
            mBullets.add(bullet);
            return bullet;
        } else {
            for (Bullet b : mBullets) {
                if (b.isGone() && b.getType() == type) {
                    b.setPos(x, y);
                    return b;
                }
            }
        }
        POOL_SIZE++;
        return createBullet(ctx, type, x, y);
    }

    /**
     * 取得所有有用的子弹
     * 
     * @return
     */
    public static ArrayList<Bullet> getAllBullet() {
        ArrayList<Bullet> result = new ArrayList<Bullet>();
        for (Bullet b : mBullets) {
            if (!b.isGone()) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * 绘制所有有用的子弹
     * 
     * @param canvas 画笔
     */
    public static void drawAllBullet(Canvas canvas) {
        for (Bullet b : mBullets) {
            if (!b.isGone()) {
                b.draw(canvas);
            }
        }
    }

    /**
     * 更新所有有用的子弹的位置
     */
    public static void updateAllBullet() {
        // TODO Auto-generated method stub
        for (Bullet b : mBullets) {
            if (!b.isGone()) {
                b.updatePos();
            }
        }
    }

    /**
     * 重置所有子弹
     */
    public static void resetAllBullet() {
        for (Bullet b : mBullets) {
            b.reset();
        }
    }
}
