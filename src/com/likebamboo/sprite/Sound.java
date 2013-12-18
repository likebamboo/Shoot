
package com.likebamboo.sprite;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * 音乐类
 * 
 * @author likebamboo
 */
public class Sound {

    private static final String TAG = "AudioPlay";

    private Context mContext = null;

    private MediaPlayer mPlayer = null;

    public Sound(Context ctx) {
        mContext = ctx;
    }

    public void play(int res, boolean loop) {
        Log.i(TAG, "in play,audio res id:" + res);
        mPlayer = MediaPlayer.create(mContext, res);
        if (mPlayer != null) {
            mPlayer.setLooping(loop);
            mPlayer.start();
            mPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "onCompletion==========================");
            stop();
        }
    };

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
