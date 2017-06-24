package com.jxyy.videoview.videoview;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private boolean isone = true;
    private boolean isaudio = true;
    private boolean isst = true;
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;
    private MediaController media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        initview();
    }

    Handler mhandler = new Handler() {

    };

    Runnable rn = new Runnable() {
        @Override
        public void run() {

            if (isone) {
                mhandler.postDelayed(rn, 500);
                isone = false;
            } else {
                if (media.isShowing())
                    media.hide();
                else
                    media.show(10000);
                isone = true;
            }

        }
    };

    private void initview() {
        videoView = (VideoView) findViewById(R.id.videoview);
        media = new MediaController(this);
        videoView.setMediaController(media);
        videoView.setFocusable(false);
        
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.qqq));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == event.ACTION_DOWN) {
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == event.ACTION_UP) {
                    x2 = event.getX();
                    y2 = event.getY();

                    if (isaudio) {
                        if (x2 - x1 > 300) {
                            videoView.seekTo(videoView.getCurrentPosition() + 10 * 1000);
                            Toast.makeText(getApplicationContext(), "快进10秒", Toast.LENGTH_SHORT).show();
                        } else if (x1 - x2 > 300) {
                            videoView.seekTo(videoView.getCurrentPosition() - 10 * 1000);
                            Toast.makeText(getApplicationContext(), "快退10秒", Toast.LENGTH_SHORT).show();
                        } else {

                            if (isone) {
                                mhandler.post(rn);
                            } else {
                                if (videoView.isPlaying())
                                    videoView.pause();
                                else
                                    videoView.start();
                                isone = true;
                                mhandler.removeCallbacks(rn);
                            }
                        }
                    }


                    isaudio = true;
                    isst = true;

                }

                if(isaudio)
                if(Math.abs(x1-event.getX())>100){
                    isst = false;
                }

                if (isst) {

                    if (event.getY() - y1 > 50) {
                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
                        y1 = event.getY();
                        isaudio = false;
                    } else if (y1 - event.getY() > 50) {
                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                        y1 = event.getY();
                        isaudio = false;
                    }
                }

                return true;
            }
        });

        videoView.start();

    }

}
