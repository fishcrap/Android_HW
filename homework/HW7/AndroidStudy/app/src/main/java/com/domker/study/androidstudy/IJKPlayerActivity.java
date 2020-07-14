package com.domker.study.androidstudy;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.domker.study.androidstudy.player.VideoPlayerIJK;
import com.domker.study.androidstudy.player.VideoPlayerListener;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 使用开源IjkPlayer播放视频
 */
public class IJKPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoPlayerIJK ijkPlayer;
    private MediaPlayer player;
    private SurfaceHolder holder;
    AudioManager audioManager;
    private Button btnPause;
    private Button btnStop;
    private Button btnSetting;
    private SeekBar seekBarTime;
    private SeekBar seekBarVolume;
    private RelativeLayout rlPlayer;
    private RelativeLayout rlVolume;
    private RelativeLayout rlLoading;
    private RelativeLayout rlMenu;
    private ImageButton imageButton;
    private TextView tvTime;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

//    private boolean isPlayFinish = false;
    private boolean isPortrait = true;
    private boolean menu_visible = true;

    private static final int MSG_REFRESH = 1234;
    VolumeReceiver receiver;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ijkplayer);
        setTitle("ijkPlayer");

        initView();
    }

    @SuppressLint("HandlerLeak")
    private void initView() {
        ijkPlayer = findViewById(R.id.ijkPlayer);
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }
        ijkPlayer.setListener(new VideoPlayerListener());
        ijkPlayer.setVideoResource(R.raw.bytedance);
//        ijkPlayer.setVideoPath(getVideoPath());
        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                seekBarTime.setProgress(100);
                btnPause.setText("播放");
                btnStop.setText("播放");
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                refresh();
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
//                isPlayFinish = false;
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                videoScreenInit();
                //toggle();
                mp.start();
                rlLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
            }
        });

        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnSetting = findViewById(R.id.btn_setting);
        seekBarTime = findViewById(R.id.seekBar_time);
        seekBarVolume = findViewById(R.id.seekBar_volume);
        rlPlayer = findViewById(R.id.rl_video);
        rlVolume = findViewById(R.id.rl_volume);
        rlLoading = findViewById(R.id.rl_loading);
        rlMenu = findViewById(R.id.rl_menu);
        imageButton = findViewById(R.id.image_volume);
        tvTime = findViewById(R.id.tv_time);
        VideoPlayerIJK ijkPlayerView = findViewById(R.id.ijkPlayer);

        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        ijkPlayerView.setOnClickListener(this);

        rlVolume.setVisibility(View.INVISIBLE);

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    ijkPlayer.seekTo(ijkPlayer.getDuration() * progress / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacksAndMessages(null);
                ijkPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ijkPlayer.seekTo(ijkPlayer.getDuration() * seekBar.getProgress() / 100);
                ijkPlayer.start();
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
            }
        });

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_REFRESH:
                        if (ijkPlayer.isPlaying()) {
                            refresh();
                            handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                        }
                        break;
                }

            }
        };

        receiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(receiver, filter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_REFRESH, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkPlayer != null && ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        IjkMediaPlayer.native_profileEnd();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ijkPlayer != null) {
            ijkPlayer.stop();
            ijkPlayer.release();
            ijkPlayer = null;
        }

        unregisterReceiver(receiver);
    }

    private class VolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        }
    }

    private void refresh() {
        long current = ijkPlayer.getCurrentPosition() / 1000;
        long duration = ijkPlayer.getDuration() / 1000;
        long current_second = current % 60;
        long current_minute = current / 60;
        long total_second = duration % 60;
        long total_minute = duration / 60;
        String time = String.format(Locale.getDefault(),"%02d:%02d/%02d:%02d",current_minute, current_second, total_minute, total_second);
        tvTime.setText(time);
        if (duration != 0) {
            seekBarTime.setProgress((int) (current * 100 / duration));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                if(btnPause.getText().toString().equals(getResources().getString(R.string.pause))) {
                    ijkPlayer.pause();
                    btnPause.setText(getResources().getString(R.string.play));
                }
                else {
                    ijkPlayer.start();

                    btnPause.setText(getResources().getString(R.string.pause));
                }
                break;
            case R.id.btn_stop:
                if(btnStop.getText().toString().equals(getResources().getString(R.string.stop))) {
                    ijkPlayer.stop();
                    btnStop.setText(getResources().getString(R.string.play));
                }
                else {
//                    ijkPlayer.setVideoPath(getVideoPath());
                    ijkPlayer.setVideoResource(R.raw.bytedance);
//                    ijkPlayer.start();
                    btnStop.setText(getResources().getString(R.string.stop));
                }
                break;
            case R.id.btn_setting:
                toggle();
                break;
            case R.id.image_volume:
                rlVolume.setVisibility(rlVolume.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                break;
            case R.id.ijkPlayer:
                if (menu_visible == false) {
                    rlMenu.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_bottom);
                    rlMenu.startAnimation(animation);
                    menu_visible = true;
                } else {
                    rlMenu.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bottom);
                    rlMenu.startAnimation(animation);
                    rlMenu.setVisibility(View.INVISIBLE);
                    menu_visible = false;
                }
            default:
                 break;

        }
    }

    private void videoScreenInit() {
        if (isPortrait) {
            portrait();
        } else {
            landscape();
        }
    }

    private void toggle() {
        if (!isPortrait) {
            portrait();
        } else {
            landscape();
        }
    }

    private void portrait() {
        ijkPlayer.pause();
        isPortrait = true;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        float width = wm.getDefaultDisplay().getWidth();
//        float height = wm.getDefaultDisplay().getHeight();
//        float ratio = width / height;
//        if (width < height) {
//            ratio = height/width;
//        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float windowWidth  = metrics.widthPixels;
        float windowHeight = metrics.heightPixels;
        if(windowWidth > windowHeight)
            windowWidth = windowHeight;
        float ratio = windowWidth / ijkPlayer.getVideoWidth();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();
        layoutParams.height = (int) (ijkPlayer.getVideoHeight() * ratio);
        layoutParams.width = (int) (ijkPlayer.getVideoWidth() * ratio);
        rlPlayer.setLayoutParams(layoutParams);
        btnSetting.setText(getResources().getString(R.string.fullScreen));
        imageButton.setVisibility(View.GONE);
        rlVolume.setVisibility(View.INVISIBLE);
        ijkPlayer.start();
    }

    private void landscape() {
        ijkPlayer.pause();
        isPortrait = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();

        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        rlPlayer.setLayoutParams(layoutParams);
        btnSetting.setText(getResources().getString(R.string.smallScreen));
        imageButton.setVisibility(View.VISIBLE);
        ijkPlayer.start();
    }

    private String getVideoPath() {
        return "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
//        return "android.resource://" + this.getPackageName() + "/" + R.raw.bytedance;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ijkPlayer == null) {
            return;
        }
        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            landscape();
        } else {
            // 竖屏
            portrait();
        }
    }


}
