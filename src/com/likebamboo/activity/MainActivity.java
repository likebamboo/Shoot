
package com.likebamboo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.likebamboo.pref.GamePref;
import com.likebamboo.shoot.R;
import com.likebamboo.sprite.Sound;
import com.likebamboo.util.Utils;
import com.likebamboo.view.ShootSurface;

/**
 * 游戏主界面
 * 
 * @author likebamboo
 * @create 2013-09-09
 */
public class MainActivity extends Activity {
    public static final int MSG_GAME_OVER = 0x1000;

    public static final int MSG_SCORE_CHANGE = 0x1001;

    public static final int MSG_BOMB_CHANGE = 0x1002;

    public static final int MSG_SOUND = 0x2001;

    private static final int SOUNDS_COUNT = 12;

    /**
     * 主界面surface
     */
    private ShootSurface mainSurface = null;

    /**
     * 分数最 高位
     */
    private ImageView mScoreIv0 = null;

    /**
     * 分数 高位
     */
    private ImageView mScoreIv1 = null;

    /**
     * 分数 中间位
     */
    private ImageView mScoreIv2 = null;

    /**
     * 分数 最低位
     */
    private ImageView mScoreIv3 = null;

    /**
     * 炸弹相关
     */
    private RelativeLayout mBombLayout = null;

    /**
     * 炸弹
     */
    private ImageView mBombIv1 = null;

    /**
     * 炸弹 X
     */
    private ImageView mBombIv2 = null;

    /**
     * 炸弹个数
     */
    private ImageView mBombIv3 = null;

    /**
     * 暂停按钮
     */
    private RelativeLayout mPauseLayout = null;

    /**
     * 暂停对话框
     */
    private Dialog pauseDlg = null;

    /**
     * 游戏结束对话框
     */
    private Dialog gameOverDialog = null;

    /**
     * gameOver?
     */
    private boolean gameOver = false;

    /**
     * 获得的炸弹数量
     */
    private int mBombCount = 0;

    private int mScore = 0;

    private static final int[] nums = {
            R.drawable.num0, R.drawable.num1, R.drawable.num2, R.drawable.num3, R.drawable.num4,
            R.drawable.num5, R.drawable.num6, R.drawable.num7, R.drawable.num8, R.drawable.num9
    };

    /**
     * 游戏背景音乐
     */
    private Sound mBgSound = null;

    /**
     * 游戏声音池
     */
    private SoundPool mSoundPool = null;

    /**
     * 设置
     */
    private ImageView mSettingIv = null;

    /**
     * 背景音乐
     */
    private boolean mBackMusicOn = true;

    /**
     * 游戏音效
     */
    private boolean mGameMusicOn = true;

    /**
     * 声音ID与资源ID的对应关系
     */
    private SparseIntArray mSoundIds = new SparseIntArray();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SCORE_CHANGE:// 分数改变
                    mScore += msg.arg1;
                    showScore(mScore);
                    break;
                case MSG_BOMB_CHANGE:// 炸弹数量改变
                    mBombCount++;
                    showBomb(mBombCount);
                    break;
                case MSG_GAME_OVER:// 游戏结束
                    gameOver = true;
                    gameOver();
                    break;
                case MSG_SOUND:
                    playSoundEffect(msg.arg1);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 全屏显示窗口
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initView();
        addListener();

        // 初始化背景音乐
        mBgSound = new Sound(this);
        // 初始化声音池
        initSoundPool();
    }

    /**
     * 初始化声音池
     */
    private void initSoundPool() {
        // 初始化声音池
        mSoundPool = new SoundPool(SOUNDS_COUNT, AudioManager.STREAM_MUSIC, 100);
        // 建立声音load id与资源ID的关系
        mSoundIds.put(R.raw.achievement, mSoundPool.load(this, R.raw.achievement, 1));
        mSoundIds.put(R.raw.big_spaceship_flying,
                mSoundPool.load(this, R.raw.big_spaceship_flying, 1));
        // 发子弹的声音
        mSoundIds.put(R.raw.bullet, mSoundPool.load(this, R.raw.bullet, 1));
        // 打击敌机的声音
        mSoundIds.put(R.raw.button, mSoundPool.load(this, R.raw.button, 1));
        mSoundIds.put(R.raw.enemy1_down, mSoundPool.load(this, R.raw.enemy1_down, 1));
        mSoundIds.put(R.raw.enemy2_down, mSoundPool.load(this, R.raw.enemy2_down, 1));
        mSoundIds.put(R.raw.enemy3_down, mSoundPool.load(this, R.raw.enemy3_down, 1));
        mSoundIds.put(R.raw.game_over, mSoundPool.load(this, R.raw.game_over, 1));
        mSoundIds.put(R.raw.get_bomb, mSoundPool.load(this, R.raw.get_bomb, 1));
        mSoundIds.put(R.raw.get_double_laser, mSoundPool.load(this, R.raw.get_double_laser, 1));
        // 产生一个降落伞
        mSoundIds.put(R.raw.out_porp, mSoundPool.load(this, R.raw.out_porp, 1));
        mSoundIds.put(R.raw.use_bomb, mSoundPool.load(this, R.raw.use_bomb, 1));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mBackMusicOn = GamePref.getInstance(this).getBackMusic();
        mGameMusicOn = GamePref.getInstance(this).getGameMusic();

        playBgSound();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        // 初始化游戏画布
        mainSurface = (ShootSurface)findViewById(R.id.main_view);
        mainSurface.setHandler(mHandler);
        mainSurface.init();
        // 与分数相关
        mScoreIv0 = (ImageView)findViewById(R.id.score0_iv);
        mScoreIv1 = (ImageView)findViewById(R.id.score1_iv);
        mScoreIv2 = (ImageView)findViewById(R.id.score2_iv);
        mScoreIv3 = (ImageView)findViewById(R.id.score3_iv);

        // 暂停
        mPauseLayout = (RelativeLayout)findViewById(R.id.game_pause_layout);

        // 与炸弹相关
        mBombIv1 = (ImageView)findViewById(R.id.bomb_iv);
        mBombIv2 = (ImageView)findViewById(R.id.bomb_x_iv);
        mBombIv3 = (ImageView)findViewById(R.id.bomb_count_iv);
        mBombLayout = (RelativeLayout)findViewById(R.id.game_bomb_board);

        // 初始化暂停对话框
        initPauseDialog();

        // 设置
        mSettingIv = (ImageView)findViewById(R.id.splash_settings_iv);
    }

    /**
     * 初始化游戏暂停的对话框。
     */
    private void initPauseDialog() {
        pauseDlg = new Dialog(this, R.style.dialog);
        View view = View.inflate(this, R.layout.game_pause_dialog, null);
        // 回到游戏
        view.findViewById(R.id.pause_dialog_start_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pauseDlg.dismiss();
                mainSurface.startUpdate();
            }
        });

        // 退出游戏
        view.findViewById(R.id.pause_dialog_stop_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pauseDlg.dismiss();
                mainSurface.setRunning(false);
                finish();
            }
        });

        // 重新开始
        view.findViewById(R.id.pause_dialog_restart_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 清空分数
                mScore = 0;
                showScore(0);

                // 清空炸弹
                mBombCount = 0;
                showBomb(mBombCount);

                // 重置游戏界面
                mainSurface.resetGame();
                pauseDlg.dismiss();
            }
        });

        pauseDlg.setContentView(view);
        WindowManager.LayoutParams lp = pauseDlg.getWindow().getAttributes();
        // 设置Dialog的宽度
        lp.width = (int)(Utils.DEVICE_WIDTH * 0.8);
        pauseDlg.getWindow().setAttributes(lp);
        // 屏蔽返回键
        pauseDlg.setCancelable(false);
    }

    /**
     * 初始化游戏暂停的对话框。
     */
    private void initGameOverDialog() {
        gameOverDialog = new Dialog(this, R.style.dialog);
        View v = View.inflate(this, R.layout.game_over_dialog, null);

        // 退出游戏
        v.findViewById(R.id.game_over_dialog_stop_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mainSurface.stopUpdate();
                mainSurface.setRunning(false);
                gameOverDialog.dismiss();
                finish();
            }
        });

        // 重新开始
        v.findViewById(R.id.game_over_dialog_restart_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gameOverDialog.dismiss();

                // 清空分数
                mScore = 0;
                showScore(0);

                // 清空炸弹
                mBombCount = 0;
                showBomb(mBombCount);

                gameOver = false;

                // 重置游戏界面
                mainSurface.resetGame();
                playBgSound();
            }
        });

        gameOverDialog.setContentView(v);
        WindowManager.LayoutParams lp = gameOverDialog.getWindow().getAttributes();
        // 设置Dialog的宽度
        lp.width = (int)(Utils.DEVICE_WIDTH * 0.8);
        gameOverDialog.getWindow().setAttributes(lp);
        // 屏蔽返回键
        gameOverDialog.setCancelable(false);
    }

    /**
     * 添加监听器
     */
    private void addListener() {
        // 游戏暂停
        mPauseLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mainSurface.stopUpdate();
                // TODO 显示对话框。
                pauseDlg.show();
            }
        });

        // 炸弹
        mBombLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showBomb(--mBombCount);
                mainSurface.bombAll();
            }
        });

        // 设置
        mSettingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 播放游戏声音
     * 
     * @param id 资源Id
     */
    private void playSoundEffect(int id) {
        if (!mGameMusicOn) {
            return;
        }
        try {
            if (mSoundPool != null) {
                mSoundPool.play(mSoundIds.get(id), 13, 15, 1, 0, 1f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示游戏结束对话框
     */
    private void gameOver() {
        if (gameOverDialog == null) {
            initGameOverDialog();
        }
        if (!gameOverDialog.isShowing()) {
            gameOverDialog.show();
        }
        // 停止背景音乐
        stopBgSound();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        // 如果surfaceView 正处于运行更新状态，暂停之。
        if (mainSurface.isUpdate() && !gameOver) {
            mPauseLayout.performClick();
        }
        // 停止游戏背景音乐
        stopBgSound();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /**
     * 播放背景音乐
     */
    private void playBgSound() {
        if (mBackMusicOn) {
            mBgSound.play(R.raw.game_music, true);
        }
    }

    /**
     * 停止游戏背景音乐
     */
    private void stopBgSound() {
        if (mBgSound != null) {
            mBgSound.stop();
        }
    }

    /**
     * 显示分数
     * 
     * @param score
     */
    private void showScore(int score) {
        int num = score / 1000;
        viewScoreNum(num);
    }

    /**
     * 显示炸弹数
     * 
     * @param bomb
     */
    private void showBomb(int bomb) {
        if (bomb <= 0) {
            mBombLayout.setVisibility(View.GONE);
            return;
        }
        mBombLayout.setVisibility(View.VISIBLE);
        mBombIv1.setVisibility(View.VISIBLE);
        if (bomb == 1) {
            mBombIv2.setVisibility(View.GONE);
            mBombIv3.setVisibility(View.GONE);
            return;
        }

        mBombIv2.setVisibility(View.VISIBLE);
        mBombIv3.setVisibility(View.VISIBLE);
        if (bomb < nums.length) {
            mBombIv3.setImageResource(nums[bomb]);
        } else {
            mBombIv3.setImageResource(nums[nums.length - 1]);
        }
    }

    /*
     * Set number from 0~99
     */
    public void viewScoreNum(int num) {
        if (num > 9999) {
            mScoreIv0.setImageResource(nums[9]);
            mScoreIv1.setImageResource(nums[9]);
            mScoreIv2.setImageResource(nums[9]);
            mScoreIv3.setImageResource(nums[9]);
            return;
        }

        int top = num / 1000;
        int high = (num / 100) % 10;
        int midium = (num / 10) % 10;
        int low = num % 10;

        if (top != 0) {
            mScoreIv0.setImageResource(nums[top]);
        } else {// 没有最高位
            mScoreIv0.setImageDrawable(null);
        }
        if (high != 0) {
            mScoreIv1.setImageResource(nums[high]);
        } else if (top != 0) {// 最高位不为0
            mScoreIv1.setImageResource(nums[0]);
        } else {// 最高位、高位都为0
            mScoreIv1.setImageDrawable(null);
        }

        if (midium != 0) {
            mScoreIv2.setImageResource(nums[midium]);
        } else if (high != 0 || top != 0) {
            mScoreIv2.setImageResource(nums[0]);
        } else {// 最高位、高位、中位都为0
            mScoreIv2.setImageDrawable(null);
        }

        if (low != 0) {
            mScoreIv3.setImageResource(nums[low]);
        } else if (midium != 0) {
            mScoreIv3.setImageResource(nums[0]);
        } else {
            mScoreIv3.setImageDrawable(null);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // ScorePref.getInstance(this).removeScore(Utils.userName);
        mPauseLayout.performClick();
    }

}
