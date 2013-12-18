
package com.likebamboo.task;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.likebamboo.shoot.R;
import com.likebamboo.util.ImageCache;
import com.likebamboo.util.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author likebamboo
 */
public class GetImgTask extends AsyncTask<Void, Void, Boolean> {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 错误信息
     */
    private String mError = "";

    /**
     * 返回的数据
     */
    private Drawable retDrawable = null;

    /**
     * 是否取消运行
     */
    private Status mStatus = AsyncTask.Status.PENDING;

    /**
     * url
     */
    private String mUrl = "";

    /**
     * 返回结果监听器
     */
    private IOnResultListener mListener = null;

    public interface IOnResultListener {
        void onResult(final boolean success, final String error, final Drawable retDrawable);
    }

    /**
     * 取消
     */
    public void cancel() {
        mStatus = Status.FINISHED;
    }

    /**
     * 进程是否在运行
     * 
     * @return
     */
    public boolean isRunning() {
        return mStatus != Status.FINISHED;
    }

    public GetImgTask(Context mContext, String url, IOnResultListener listener) {
        super();
        this.mContext = mContext;
        this.mListener = listener;
        this.mUrl = url;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO Auto-generated method stub
        // 正在运行
        mStatus = Status.RUNNING;

        // url为空
        if (TextUtils.isEmpty(mUrl)) {
            return false;
        }

        // 没网络
        if (!Utils.CheckNetworkConnection(mContext) && !Utils.IsWifiEnable(mContext)) {
            mError = mContext.getString(R.string.network_disable);
            return false;
        }

        // 先从缓存的文件中加载
        retDrawable = ImageCache.getInstance().loadImg(mUrl);
        if (retDrawable != null) {
            return true;
        }

        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        HttpGet post = new HttpGet(mUrl);
        try {
            HttpResponse response = httpClient.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return false;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = entity.getContent();
                    // 将图片缓存到本地
                    if (Utils.writeCacheFile(mUrl, inputStream)) {
                        retDrawable = ImageCache.getInstance().loadImg(mUrl);
                    } else {
                        retDrawable = Drawable.createFromStream(inputStream, "src");
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    mError = mContext.getString(R.string.unknow_error);
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            post.abort();
            mError = mContext.getString(R.string.unknow_error);
            return false;
        }

        mError = mContext.getString(R.string.time_out);
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // TODO Auto-generated method stub
        if (mListener != null && mStatus != Status.FINISHED) {// 如果操作没有被取消
            System.out.println("return:" + result);
            mListener.onResult(result, mError, retDrawable);
        }
        mStatus = Status.FINISHED;
        super.onPostExecute(result);
    }

}
