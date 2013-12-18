
package com.likebamboo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.likebamboo.factory.BulletFactory;
import com.likebamboo.factory.EnemyPlaneFactory;
import com.likebamboo.shoot.R;
import com.likebamboo.sprite.Bomb;
import com.likebamboo.sprite.Bullet;
import com.likebamboo.sprite.CatchWeapon;
import com.likebamboo.sprite.DoubleBullet;
import com.likebamboo.sprite.EnemyPlane;
import com.likebamboo.sprite.EnemyPlane.EnemyType;
import com.likebamboo.sprite.Plane;
import com.likebamboo.util.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @desc 主界面
 * @author <a href="mailto:likebamboo" target="_blank">likebamboo@163.com</a>
 * @create 2013-9-15
 * @Copyright 2013 <a href="http://likebamboo.sinaapp.com"
 *            target="_blank">likebamboo@163.com</a> Inc. All rights reserved.
 */
public class ShootSurface extends SurfaceView implements Callback, Runnable, OnTouchListener {

    /**
     * 游戏每秒绘制的帧数
     */
    public static final int FPS = 30;

    private long time = 0L;

    private Context mContext = null;

    /**
     * 画布宽度
     */
    @SuppressWarnings("unused")
    private int mWidth = 0;

    /**
     * 画布高度
     */
    private int mHeight = 0;

    /**
     * 视图
     */
    private SurfaceHolder mSurfaceHolder = null;

    /**
     * 背景图片0
     */
    private Bitmap mBackground0 = null;

    /**
     * 背景图片1
     */
    private Bitmap mBackground1 = null;

    /**
     * 背景图片高度
     */
    private float mBgHeight = 0;

    /**
     * 背景0所在位置
     */
    private float mBgPosY0 = 0;

    /**
     * 背景1所在位置
     */
    private float mBgPosY1 = 0;

    /**
     * 游戏是否在运行
     */
    private boolean toRun = false;

    /**
     * 游戏是否更新界面
     */
    private boolean isUpdate = true;

    /**
     * 
     */
    private boolean restore = false;

    /**
     * 游戏主线程
     */
    private Thread mainThread = null;

    /**
     * 游戏主画板
     */
    private Canvas mCanvas = null;

    /**
     * 画笔
     */
    private Paint mPaint = null;

    /**
     * 我的战斗机
     */
    private Plane mPlane = null;

    /**
     * 
     */
    private ArrayList<EnemyPlane> enemyPlans = new ArrayList<EnemyPlane>();

    /**
     * 每隔18帧产生一个小飞机
     */
    private static int proSmallTimes = FPS / 3;

    /**
     * 每隔60帧(2s)产生一个big飞机
     */
    private static int proBigTimes = FPS * 2;

    /**
     * 每隔210(7s)帧产生一个boss飞机
     */
    private static int proBossTimes = FPS * 7;

    /**
     * 每隔300帧(60s)产生一个降落伞
     */
    private static int proWeapon = FPS * 60;

    private CatchWeapon mWeapon = null;

    /**
     * 周期计数
     */
    private int times = 0;

    /**
     * handler
     */
    private Handler mHandler = null;

    public ShootSurface(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        mWidth = Utils.DEVICE_WIDTH;
        mHeight = Utils.DEVICE_HEIGHT;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);
    }

    public ShootSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        mWidth = Utils.DEVICE_WIDTH;
        mHeight = Utils.DEVICE_HEIGHT;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);
    }

    public void init() {
        /** 游戏背景 **/
        mBackground0 = Utils.getGameBackground(mContext);
        mBackground1 = Utils.getGameBackground(mContext);

        // 使背景填充
        mBgHeight = Utils.DEVICE_WIDTH * mBackground0.getHeight() / mBackground0.getWidth();

        /** 第一张图片贴在屏幕00点，第二张图片在第一张图片上方 **/
        mBgPosY0 = 0;
        mBgPosY1 = -mBgHeight;

        mPaint = new Paint();
        mPlane = new Plane(mContext, mHandler);
    }

    /**
     * 设置Handler
     * 
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        /** 启动游戏主线程 **/
        toRun = true;
        restore = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (toRun) {
            try {
                // 在这里加上线程安全锁
                synchronized (mSurfaceHolder) {
                    long cTime = System.currentTimeMillis();
                    if (cTime - time > 1000 / FPS) {
                        times++;
                        if (times > 420 * FPS) {
                            times = 1;
                        }
                        if (isUpdate || restore) {
                            /** 拿到当前画布 然后锁定 **/
                            mCanvas = mSurfaceHolder.lockCanvas();
                            // 开始游戏
                            if (restore) {
                                draw();
                            } else {
                                startGame();
                            }
                            /** 绘制结束后解锁显示在屏幕上 **/
                            if (mCanvas != null) {
                                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                            }
                        }
                        restore = false;
                        time = cTime;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        draw();// 绘制对象
        collision();// 碰撞检测
        update();// 更新界面
    }

    /**
     * 绘制
     */
    public void draw() {
        // 第0张背景应该画的位置
        RectF mBgRectF0 = new RectF(0, mBgPosY0, Utils.DEVICE_WIDTH, mBgPosY0 + mBgHeight);
        // 第1张背景应该画的位置
        RectF mBgRectF1 = new RectF(0, mBgPosY1, Utils.DEVICE_WIDTH, mBgPosY1 + mBgHeight);
        if (mCanvas != null) {
            try {
                /** 绘制游戏地图 **/
                mCanvas.drawBitmap(mBackground0, null, mBgRectF0, mPaint);
                mCanvas.drawBitmap(mBackground1, null, mBgRectF1, mPaint);

                mPlane.draw(mCanvas);

                for (EnemyPlane enemy : enemyPlans) {
                    enemy.draw(mCanvas);
                }

                if (mWeapon != null && !mWeapon.isGone()) {
                    mWeapon.draw(mCanvas);
                }

                if (times % proSmallTimes == 0) { // 生成一个小飞机
                    createNewPlane(EnemyPlane.EnemyType.SMALL_ENEMY_PLANE);
                }
                if (times % proBigTimes == 0) { // 生成一个大飞机
                    createNewPlane(EnemyPlane.EnemyType.BIG_ENEMY_PLANE);
                }
                if (times % proBossTimes == 0) { // 生成一个boss飞机
                    createNewPlane(EnemyPlane.EnemyType.BOSS_ENEMY_PLANE);
                }

                if (times % proWeapon == 0) { // 生成一个降落伞
                    Random rand = new Random();
                    int which = rand.nextInt(2);
                    switch (which) {
                        case 0:
                            mWeapon = new Bomb(mContext, mHandler);
                            break;
                        case 1:
                            mWeapon = new DoubleBullet(mContext, mHandler);
                            break;
                        default:
                            mWeapon = new Bomb(mContext, mHandler);
                            break;
                    }
                    // 播放降落伞的声音。
                    Utils.sendSoundMsg(mHandler, R.raw.out_porp);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 产生新的飞机
     */
    private void createNewPlane(EnemyType enemyType) {
        int i = 0;
        for (; i < enemyPlans.size(); i++) {
            // 首先判断是否有没用的飞机，如果有的话，重置该飞机的状态，当做新飞机使用。
            if (enemyPlans.get(i).isDefState() && enemyPlans.get(i).getEnemyType() == enemyType) {
                enemyPlans.get(i).setDead(false);
                break;
            }
        }
        // 如果没有没用的飞机，那么创建新飞机
        if (i >= enemyPlans.size()) {
            enemyPlans.add(EnemyPlaneFactory.createEnemyPlane(mContext, enemyType, mHandler));
        }
    }

    /**
     * 碰撞检测
     */
    private void collision() {
        // TODO Auto-generated method stub
        for (EnemyPlane enemy : enemyPlans) {
            if (!enemy.isDead()) {
                // 每一个敌人和我机进行碰撞检测
                enemy.checkCollision(mCanvas, mPlane);
                // 每一个敌人和所有进行碰撞检测
                ArrayList<Bullet> bullets = BulletFactory.getAllBullet();
                for (Bullet bullet : bullets) {
                    enemy.checkCollision(mCanvas, bullet);
                }
            }
        }

        if (mWeapon != null && !mWeapon.isGone() && !mPlane.isDead()) {
            mWeapon.checkCollision(mPlane);
        }
    }

    /**
     * 更新界面
     */
    private void update() {
        /** 更新游戏背景图片实现向下滚动效果 **/
        mBgPosY0 += 4;
        mBgPosY1 += 4;
        if (mBgPosY0 >= mHeight) {
            mBgPosY0 = mBgPosY1 - mBgHeight;
        }
        if (mBgPosY1 >= mHeight) {
            mBgPosY1 = mBgPosY0 - mBgHeight;
        }

        /** 更新自己飞机的子弹的位置 */
        mPlane.update();
        /** 更新敌机的位置 */
        for (EnemyPlane enemy : enemyPlans) {
            if (!enemy.isDead()) {
                enemy.updatePos();
            }
        }

        if (mWeapon != null && !mWeapon.isGone()) {
            mWeapon.updatePos();
        }
    }

    /**
     * 重置游戏(重新开始)
     */
    public void resetGame() {
        // 允许更新界面
        isUpdate = true;
        // 运行游戏
        toRun = true;
        // 周期计数清空
        times = 1;
        // 上次绘制帧的时间清空
        time = 0L;
        // 重置自己的飞机与子弹
        mPlane.reset();
        // 重置敌机
        for (EnemyPlane enemy : enemyPlans) {
            enemy.reset();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceView销毁的时候
        toRun = false;
    }

    public void setRunning(boolean run) {
        this.toRun = run;
        if (toRun) {
            mainThread = new Thread(this);
            mainThread.start();
        }
    }

    public boolean getRunning() {
        return toRun;
    }

    /**
     * 暂停更新界面
     */
    public void startUpdate() {
        isUpdate = true;
    }

    /**
     * 允许更新界面
     */
    public void stopUpdate() {
        isUpdate = false;
    }

    /**
     * 是否处于更新状态。
     * 
     * @return
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    /**
     * 炸死所有敌机。
     */
    public void bombAll() {
        for (EnemyPlane enemy : enemyPlans) {
            if (!enemy.isDead()) {
                enemy.bomb(mCanvas);
            }
        }
    }

    // **********************************有关拖动自己的飞机的*****************************
    /**
     * 点击点
     */
    private Point point = new Point();

    /**
     * 我的飞机的rect
     */
    private Rect rect = null;

    /**
     * 判断是否点击在图片上，否则拖动无效
     */
    private boolean canDrag = false;

    /**
     * 点击点离飞机中心点的距离
     */
    private float offsetX = 0F, offsetY = 0F;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                point.x = (int)event.getX();
                point.y = (int)event.getY();
                rect = mPlane.getRect();
                if (rect.contains(point.x, point.y)) {
                    canDrag = true;
                    offsetX = point.x - mPlane.getPos().x;
                    offsetY = point.y - mPlane.getPos().y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (canDrag) {
                    mPlane.setPos(event.getX() - offsetX, event.getY() - offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                canDrag = false;
                break;
            default:
                break;
        }
        return true;
    }

}
