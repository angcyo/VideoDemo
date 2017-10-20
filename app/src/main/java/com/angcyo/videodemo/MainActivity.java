package com.angcyo.videodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    /**
     * 使用方法1, 还是方法2
     */
    int method = 2;

    /**
     * 视频播放进度
     */
    int process = 0;

    Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!MainActivity.this.isDestroyed()) {
                try {
                    VideoView videoView = (VideoView) findViewById(R.id.video_view);
                    process = videoView.getCurrentPosition();
                    L.e("call: run([])-> " + process);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    private OrientationEventListener mOrientationEventListener;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationEventListener.disable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("call: onCreate([savedInstanceState])-> ");

        if (method == 1) {
            initView();
            initVideoView();

            mThread.start();
        } else if (method == 2) {
            setContentView(R.layout.activity_main2);
            initView2();
            initVideoView();
        }

        startOrientationEventListener();
    }

    private void initVideoView() {
        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoPath("/sdcard/123_4.mp4");
        videoView.seekTo(process);
        videoView.start();
    }

    private void initView() {
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.button_land).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
            });

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Button1", Toast.LENGTH_LONG).show();
                }
            });
            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Button2", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initView2() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.button1).setVisibility(View.GONE);
            findViewById(R.id.button2).setVisibility(View.GONE);
            findViewById(R.id.button_land).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.text_view)).setText("竖屏");

            findViewById(R.id.button_land).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
            });

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            findViewById(R.id.button1).setVisibility(View.VISIBLE);
            findViewById(R.id.button2).setVisibility(View.VISIBLE);
            findViewById(R.id.button_land).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.text_view)).setText("横屏");

            findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Button1", Toast.LENGTH_LONG).show();
                }
            });
            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Button2", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.e("call: onConfigurationChanged([newConfig])-> ");

        if (method == 1) {
            initView();
            initVideoView();
        } else if (method == 2) {
            initView2();
        }
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 监听手机方向
     */
    private void startOrientationEventListener() {
        mOrientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                L.e("call: onOrientationChanged([orientation])-> 方向:" + orientation);
                if (30 < orientation && orientation < 50) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                }
            }
        };
        mOrientationEventListener.enable();
    }
}
