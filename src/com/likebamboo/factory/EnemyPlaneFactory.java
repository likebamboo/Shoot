
package com.likebamboo.factory;

import android.content.Context;
import android.os.Handler;

import com.likebamboo.sprite.BigPlane;
import com.likebamboo.sprite.BossPlane;
import com.likebamboo.sprite.EnemyPlane;
import com.likebamboo.sprite.SmallPlane;

public class EnemyPlaneFactory {

    public static final EnemyPlane createEnemyPlane(Context ctx,
            EnemyPlane.EnemyType enemyPlaneType, Handler handler) {
        EnemyPlane enemyPlane = null;
        switch (enemyPlaneType) {
            case SMALL_ENEMY_PLANE:
                enemyPlane = new SmallPlane(ctx, enemyPlaneType, handler);
                break;
            case BIG_ENEMY_PLANE:
                enemyPlane = new BigPlane(ctx, enemyPlaneType, handler);
                break;
            case BOSS_ENEMY_PLANE:
                enemyPlane = new BossPlane(ctx, enemyPlaneType, handler);
                break;
        }

        enemyPlane.reset();
        return enemyPlane;
    }
}
