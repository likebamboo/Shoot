
package com.likebamboo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.likebamboo.pref.GamePref;
import com.likebamboo.shoot.R;
import com.likebamboo.util.Utils;

/**
 * 设置界面
 * 
 * @author likebamboo
 */
public class SettingsActivity extends Activity {

    /**
     * 开始游戏
     */
    private Button mStartGameBt = null;

    // 是否开启背景音乐
    private Button mBackMusicBt = null;

    // 是否开启游戏音效
    private Button mGameMusicBt = null;

    /**
     * 测试QQ登录
     */
    private Button mQQLoginBt = null;

    /**
     * 关于作者
     */
    private Button mAboutMeBt = null;

    private GamePref mGameSettings = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);

        mGameSettings = GamePref.getInstance(this);

        initView();

        addListener();
    }

    /**
     * 初始化布局界面
     */
    private void initView() {
        mStartGameBt = (Button)findViewById(R.id.settings_start_bt);
        mBackMusicBt = (Button)findViewById(R.id.settings_back_music_bt);
        mGameMusicBt = (Button)findViewById(R.id.settings_game_music_bt);
        mQQLoginBt = (Button)findViewById(R.id.settings_QQ_bt);
        mAboutMeBt = (Button)findViewById(R.id.settings_about_me_bt);
    }

    /**
     * 添加监听器
     */
    private void addListener() {
        // 开始游戏
        mStartGameBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        // 背景音乐
        mBackMusicBt.setText(getBackMusicStr());
        mBackMusicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGameSettings.setBackMusic(!mGameSettings.getBackMusic());
                mBackMusicBt.setText(getBackMusicStr());
            }
        });

        // 背景音乐
        mBackMusicBt.setText(getBackMusicStr());
        mBackMusicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGameSettings.setBackMusic(!mGameSettings.getBackMusic());
                mBackMusicBt.setText(getBackMusicStr());
            }
        });

        // 游戏音效
        mGameMusicBt.setText(geGameMusicStr());
        mGameMusicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGameSettings.setGameMusic(!mGameSettings.getGameMusic());
                mGameMusicBt.setText(geGameMusicStr());
            }
        });

        // QQ登录测试
        mQQLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SettingsActivity.this, QQActivity.class);
                startActivity(i);
            }
        });

        // 关于作者
        mAboutMeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SettingsActivity.this, AboutMeActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * 获得背景音乐字符串
     * 
     * @return
     */
    private Spanned getBackMusicStr() {
        boolean backMusic = mGameSettings.getBackMusic();
        if (backMusic) {
            return Utils.htmlFormat(getString(R.string.back_music, "<font color='green'>"
                    + getString(R.string.turn_on) + "</font>"));
        } else {
            return Utils.htmlFormat(getString(R.string.back_music, "<font color='red'>"
                    + getString(R.string.turn_off) + "</font>"));
        }
    }

    /**
     * 获得游戏音效字符串
     * 
     * @return
     */
    private Spanned geGameMusicStr() {
        boolean gameMusic = mGameSettings.getGameMusic();
        if (gameMusic) {
            return Utils.htmlFormat(getString(R.string.game_music, "<font color='green'>"
                    + getString(R.string.turn_on) + "</font>"));
        } else {
            return Utils.htmlFormat(getString(R.string.game_music, "<font color='red'>"
                    + getString(R.string.turn_off) + "</font>"));
        }
    }
}
