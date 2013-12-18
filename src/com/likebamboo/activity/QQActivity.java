
package com.likebamboo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.likebamboo.pref.GamePref;
import com.likebamboo.shoot.R;
import com.likebamboo.task.GetImgTask;
import com.likebamboo.util.Utils;
import com.likebamboo.widget.roundbt.RoundTableView;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * 登录页面
 * 
 * @author likebamboo
 */
public class QQActivity extends Activity {

    /**
     * QQ登录后的 open_id
     */
    public static final String CONFIG_OPEN_ID = "openid";

    /**
     * QQ登录后的 access_token.
     */
    public static final String CONFIG_ACCESS_TOKEN = "access_token";

    /**
     * QQ登录后token有效时间
     */
    public static final String CONFIG_EXPIRES_IN = "expires_in";

    /**
     * 获取信息成功
     */
    public static final int GET_USER_INFO_OK = 0x10000;

    /**
     * 获取信息失败
     */
    public static final int GET_USER_INFO_FAILED = 0x10001;

    /**
     * QQ登录接口
     */
    private Tencent mTencent = null;

    /**
     * 返回按钮
     */
    private Button mBackBt = null;

    /**
     * openId
     */
    private String mOpenId = "";

    /**
     * accessToken
     */
    private String mAccessToken = "";

    /**
     * 有效期
     */
    private long mExpiresIn = 0L;

    /**
     * 游戏中数据对象
     */
    private GamePref mPref = null;

    /**
     * handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // super.handleMessage(msg);
            switch (msg.what) {
                case GET_USER_INFO_OK:
                    String json = msg.getData().getString("data");
                    if (!TextUtils.isEmpty(json)) {
                        showUserInfo(json);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 登录界面
     */
    private LinearLayout mLoginLayout = null;

    /**
     * 登录按钮
     */
    private Button mLoginBt = null;

    /**
     * Loading界面
     */
    private LinearLayout mLoadingLayout = null;

    /**
     * 正在加载的信息
     */
    private TextView mLoadingTv = null;

    /**
     * 用户信息界面
     */
    private FrameLayout mUserLayout = null;

    /**
     * 用户信息
     */
    private RoundTableView mUserTable = null;

    /**
     * 用户名
     */
    private TextView mUserNameTv = null;

    /**
     * 用户头像
     */
    private ImageView mUserHeadIv = null;

    /**
     * 
     */
    private ImageView mUserQzIv = null;

    /**
     * 用户头像加载任务
     */
    private GetImgTask mUserHeadTask = null;

    /**
     * 用户头像加载任务
     */
    private GetImgTask mUserQzTask = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qq);

        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Utils.TENCENT_ID, getApplicationContext());

        // 初始化界面
        initView();
        // 添加监听器
        addListener();

        // 获取保存的Token和OpenId
        mPref = GamePref.getInstance(this);
        mOpenId = mPref.loadString(CONFIG_OPEN_ID);
        mAccessToken = mPref.loadString(CONFIG_ACCESS_TOKEN);
        mExpiresIn = mPref.loadLong(CONFIG_EXPIRES_IN, 0L);

        // checkConfig
        checkConfig();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 返回按钮
        mBackBt = (Button)findViewById(R.id.title_back_bt);
        // 标题
        ((TextView)findViewById(R.id.title_title_tv)).setText(R.string.qq_login);

        mLoginLayout = (LinearLayout)findViewById(R.id.qq_login_layout);
        mLoginBt = (Button)findViewById(R.id.qq_login_bt);
        mLoadingLayout = (LinearLayout)findViewById(R.id.qq_loading_layout);
        mLoadingTv = (TextView)findViewById(R.id.qq_loading_tv);

        mUserLayout = (FrameLayout)findViewById(R.id.qq_user_info_layout);
        mUserTable = (RoundTableView)findViewById(R.id.qq_user_info_round_table);
        mUserNameTv = (TextView)findViewById(R.id.qq_user_name_tv);
        mUserHeadIv = (ImageView)findViewById(R.id.qq_user_info_qq_iv);
        mUserQzIv = (ImageView)findViewById(R.id.qq_user_info_qz_iv);

    }

    /**
     * 添加监听器
     */
    private void addListener() {
        // 返回按钮事件监听
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        // 登录按钮事件监听
        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 重新走登录流程
                mTencent.login(QQActivity.this, "all", new LoginUiListener());
            }
        });
    }

    /**
     * 显示某个布局
     * 
     * @param v
     */
    private void showView(View v) {
        mLoadingLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.GONE);
        mUserLayout.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    /**
     * 验证是否已经登录
     */
    private void checkConfig() {
        // 首先验证token是否已经失效
        if (mExpiresIn - System.currentTimeMillis() <= 0) {
            // 显示登录按钮
            showView(mLoginLayout);
        } else {
            // 没有失效，设置OpenId，token等
            mTencent.setOpenId(mOpenId);
            mTencent.setAccessToken(mAccessToken, "" + (mExpiresIn - System.currentTimeMillis())
                    / 1000L);
            // 获取用户信息
            getUserInfo();
        }
    }

    /**
     * 异步请求用户基本信息
     */
    private void getUserInfo() {
        showView(mLoadingLayout);
        mLoadingTv.setText(R.string.loading_user_info);
        mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null, Constants.HTTP_GET,
                new BaseApiListener(true), null);

    }

    /**
     * 显示用户信息
     * 
     * @param userJson
     */
    private void showUserInfo(final String userJson) {
        showView(mUserLayout);
        try {
            JSONObject user = new JSONObject(userJson);
            // QQ 昵称
            if (user.has("nickname")) {
                mUserTable.addBasicItem(getString(R.string.user_name), user.getString("nickname"));
                mUserNameTv.setText(user.getString("nickname"));
            }
            // 性别
            if (user.has("gender")) {
                mUserTable.addBasicItem(getString(R.string.user_gender), user.getString("gender"));
            }
            // VIP
            if (user.has("vip")) {
                if ("0".equals(user.getString("vip"))) {
                    mUserTable.addBasicItem(getString(R.string.user_vip), getString(R.string.no));
                } else {
                    mUserTable.addBasicItem(getString(R.string.user_vip), getString(R.string.yes));
                }
            }
            // 黄钻
            if (user.has("is_yellow_vip")) {
                if ("0".equals(user.getString("is_yellow_vip"))) {
                    mUserTable.addBasicItem(getString(R.string.user_yellow_vip),
                            getString(R.string.no));
                } else {
                    mUserTable.addBasicItem(getString(R.string.user_yellow_vip),
                            getString(R.string.yes));
                    // 黄钻等级
                    if (user.has("level")) {
                        mUserTable.addBasicItem(getString(R.string.user_level),
                                user.getString("level"));
                    }
                }
            }
            mUserTable.commit();

            // 加载用户图片
            if (user.has("figureurl_qq_2")) {
                showUserHead(user.getString("figureurl_qq_2"));
            }

            if (user.has("figureurl_2")) {
                showUserQz(user.getString("figureurl_2"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json parse error");
        }
    }

    /**
     * 显示用户头像
     * 
     * @param user
     */
    @SuppressLint("NewApi")
    private void showUserHead(String url) {
        if (mUserHeadTask != null && mUserHeadTask.isRunning()) {
            mUserHeadTask.cancel();
        }
        // 用户头像
        mUserHeadTask = new GetImgTask(this, url, new GetImgTask.IOnResultListener() {
            @Override
            public void onResult(boolean success, String error, final Drawable retDrawable) {
                // TODO Auto-generated method stub
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mUserHeadIv.setImageDrawable(retDrawable);
                    }
                });
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mUserHeadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mUserHeadTask.execute();
        }
    }

    /**
     * 显示空间头像
     * 
     * @param user
     */
    @SuppressLint("NewApi")
    private void showUserQz(String url) {
        if (mUserQzTask != null && mUserQzTask.isRunning()) {
            mUserQzTask.cancel();
        }
        // 用户空间头像
        mUserQzTask = new GetImgTask(this, url, new GetImgTask.IOnResultListener() {
            @Override
            public void onResult(boolean success, String error, final Drawable retDrawable) {
                // TODO Auto-generated method stub
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mUserQzIv.setImageDrawable(retDrawable);
                        mUserQzIv.setScaleType(ScaleType.FIT_XY);
                    }
                });
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mUserQzTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mUserQzTask.execute();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mUserHeadTask != null && mUserHeadTask.isRunning()) {
            mUserHeadTask.cancel();
        }
        if (mUserQzTask != null && mUserQzTask.isRunning()) {
            mUserQzTask.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取用户信息接口
     */
    private class BaseApiListener implements IRequestListener {
        /**
         * 是否要重新验证
         */
        private Boolean mNeedReAuth = false;

        public BaseApiListener(boolean needReAuth) {
            mNeedReAuth = needReAuth;
        }

        protected void doComplete(final JSONObject response, Object state) {
            try {
                int ret = response.getInt("ret");
                // 验证失败，需重新获取token
                if (ret == 100030) {
                    if (mNeedReAuth) {
                        Runnable r = new Runnable() {
                            public void run() {
                                // 重新取token
                                mTencent.reAuth(QQActivity.this, "all", new LoginUiListener());
                            }
                        };
                        QQActivity.this.runOnUiThread(r);
                    }
                } else if (ret == 0) {// 验证成功
                    Message msg = mHandler.obtainMessage();
                    msg.getData().putString("data", response.toString());
                    msg.what = GET_USER_INFO_OK;
                    mHandler.sendMessage(msg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("toddtest", response.toString());
            }

        }

        @Override
        public void onComplete(final JSONObject response, Object state) {
            doComplete(response, state);
        }

        @Override
        public void onIOException(final IOException e, Object state) {
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e, Object state) {
        }

        @Override
        public void onJSONException(final JSONException e, Object state) {
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException arg0, Object arg1) {
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0, Object arg1) {
        }

        @Override
        public void onUnknowException(Exception arg0, Object arg1) {
        }

        @Override
        public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
        }

        @Override
        public void onNetworkUnavailableException(NetworkUnavailableException arg0, Object arg1) {
        }
    }

    private class LoginUiListener implements IUiListener {
        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
        }

        @Override
        public void onComplete(JSONObject json) {
            // TODO Auto-generated method stub
            /**
             * <p>
             * 登录验证返回信息
             */
            GamePref pref = GamePref.getInstance(QQActivity.this);

            try {
                if (json.has(CONFIG_OPEN_ID)) {
                    pref.putString(CONFIG_OPEN_ID, json.getString(CONFIG_OPEN_ID));
                }
                if (json.has(CONFIG_ACCESS_TOKEN)) {
                    pref.putString(CONFIG_ACCESS_TOKEN, json.getString(CONFIG_ACCESS_TOKEN));
                }
                if (json.has(CONFIG_EXPIRES_IN)) {
                    long expires = System.currentTimeMillis()
                            + Long.parseLong(json.getString(CONFIG_EXPIRES_IN));
                    pref.putLong(CONFIG_EXPIRES_IN, expires * 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            // 登录成功，获取用户信息
            getUserInfo();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            showToast(arg0.errorMessage);
        }
    }

    /**
     * showToast
     * 
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
