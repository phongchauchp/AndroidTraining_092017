package com.example.sony.training.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.sony.training.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phong on 11/13/17.
 */

public class BoundPlayMusicService extends Service {

    private List<Integer> listMusics = new ArrayList<>();
    private int position = 0;

    private MediaPlayer mMediaPlayer;

    private IBinder mIBinder = new LocalBinder();

    private int mediaLengthWhenPause;

    public class LocalBinder extends Binder {
        public BoundPlayMusicService getService() {
            return BoundPlayMusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listMusics.add(R.raw.play1);
        listMusics.add(R.raw.play2);
        listMusics.add(R.raw.play3);

        mMediaPlayer = MediaPlayer.create(this, listMusics.get(position));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        playMusic();
        return mIBinder;
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.release();
        super.onDestroy();
    }

    public void playMusic() {
        mMediaPlayer.start();
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mediaLengthWhenPause = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(mediaLengthWhenPause);
            mMediaPlayer.start();
        }
    }

    public void stopMusic() {
        mediaLengthWhenPause = 0;
        mMediaPlayer.stop();

        try {
            mMediaPlayer.prepare();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public void previouseMusic() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        position--;
        if (position >= 0) {
            mMediaPlayer = MediaPlayer.create(this, listMusics.get(position));
        } else {
            position = listMusics.size() - 1;
            mMediaPlayer = MediaPlayer.create(this, listMusics.get(position));
        }
        mMediaPlayer.start();
    }
    public void nextMusic() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        position++;
        if (position < listMusics.size()) {
            mMediaPlayer = MediaPlayer.create(this, listMusics.get(position));
        } else {
            position = 0;
            mMediaPlayer = MediaPlayer.create(this, listMusics.get(position));
        }
        mMediaPlayer.start();
    }

    public void seekTo(int currentDuration) {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(currentDuration);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }
}
