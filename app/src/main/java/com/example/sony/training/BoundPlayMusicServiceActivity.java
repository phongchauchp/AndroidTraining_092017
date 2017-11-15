package com.example.sony.training;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sony.training.Service.BoundPlayMusicService;

/**
 * Created by phong on 11/15/17.
 */

public class BoundPlayMusicServiceActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView tvShowTimeProcess;
    private TextView tvShowTimeTotal;
    private SeekBar sbShowProcess;
    private ImageView ivPreviousMusicButton;
    private ImageView ivPlayMusicButton;
    private ImageView ivNextMusicButton;

    private boolean isBound;
    private boolean isPlaying = false;
    private boolean songProgressBarIsBeingTouch;

    private Handler mHandler;
    private Runnable mRunnable;

    private BoundPlayMusicService mBoundPlayMusicService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            BoundPlayMusicService.LocalBinder binder = (BoundPlayMusicService.LocalBinder) service;
            mBoundPlayMusicService = binder.getService();
            initData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private void initData() {
        tvShowTimeTotal.setText(Util.milliSecondsToTimer(mBoundPlayMusicService.getMediaPlayer().getDuration()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        initViews();
        initListener();

        Intent intent = new Intent(this, BoundPlayMusicService.class);

        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = mBoundPlayMusicService.getMediaPlayer().getCurrentPosition();
                int duration = mBoundPlayMusicService.getMediaPlayer().getDuration();
                String currentTime = Util.milliSecondsToTimer(currentPosition);
                String totalTime = Util.milliSecondsToTimer(duration);
                tvShowTimeTotal.setText(totalTime);
                tvShowTimeProcess.setText(currentTime);
                sbShowProcess.setProgress(currentPosition);
                //Het time chuyen sang bai moi
                if (currentTime.equals(totalTime)) {
                    sbShowProcess.setProgress(0);
                    mBoundPlayMusicService.nextMusic();
                    sbShowProcess.setMax(mBoundPlayMusicService.getMediaPlayer().getDuration());
                    tvShowTimeTotal.setText(Util.milliSecondsToTimer(
                            mBoundPlayMusicService.getMediaPlayer().getDuration()));
                    ivPlayMusicButton.setImageResource(R.drawable.pause);
                }
                mHandler.postDelayed(mRunnable, 100);
            }
        };
    }


    private void initListener() {
        tvShowTimeProcess.setOnClickListener(this);
        tvShowTimeTotal.setOnClickListener(this);
        sbShowProcess.setOnSeekBarChangeListener(this);
        ivPreviousMusicButton.setOnClickListener(this);
        ivPlayMusicButton.setOnClickListener(this);
        ivNextMusicButton.setOnClickListener(this);
    }

    private void initViews() {
        tvShowTimeProcess = (TextView) findViewById(R.id.tv_show_time_process);
        sbShowProcess = (SeekBar) findViewById(R.id.sb_show_process);
        tvShowTimeTotal = (TextView) findViewById(R.id.tv_show_time_total);
        ivPreviousMusicButton = (ImageView) findViewById(R.id.iv_previous_music_button);
        ivPlayMusicButton = (ImageView) findViewById(R.id.iv_play_music_button);
        ivNextMusicButton = (ImageView) findViewById(R.id.iv_next_music_button);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_previous_music_button:
                if (isBound) {
                    sbShowProcess.setProgress(0);
                    mBoundPlayMusicService.previouseMusic();
                    sbShowProcess.setMax(mBoundPlayMusicService.getMediaPlayer().getDuration());
                    tvShowTimeTotal.setText(Util.milliSecondsToTimer(
                            mBoundPlayMusicService.getMediaPlayer().getDuration()));
                }
                break;
            case R.id.iv_play_music_button:
                if (isBound) {
                    if (!isPlaying) {
                        mBoundPlayMusicService.playMusic();
                        sbShowProcess.setMax(mBoundPlayMusicService.getMediaPlayer().getDuration());
                        tvShowTimeTotal.setText(Util.milliSecondsToTimer(
                                mBoundPlayMusicService.getMediaPlayer().getDuration()));
                        ivPlayMusicButton.setImageResource(R.drawable.pause);
                        isPlaying = !isPlaying;
                    } else {
                        mBoundPlayMusicService.pauseMusic();
                        ivPlayMusicButton.setImageResource(R.drawable.play);
                        isPlaying = !isPlaying;
                    }
                }
                break;
            case R.id.iv_next_music_button:
                if (isBound) {
                    mBoundPlayMusicService.nextMusic();
                    sbShowProcess.setProgress(0);
                    sbShowProcess.setMax(mBoundPlayMusicService.getMediaPlayer().getDuration());
                    tvShowTimeTotal.setText(Util.milliSecondsToTimer(
                            mBoundPlayMusicService.getMediaPlayer().getDuration()));
                }
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mBoundPlayMusicService.seekTo(progress);
            seekBar.setProgress(progress);
        } else {
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        songProgressBarIsBeingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        songProgressBarIsBeingTouch = false;
    }
}
