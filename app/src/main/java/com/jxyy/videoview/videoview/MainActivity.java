package com.jxyy.videoview.videoview;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        initview();
    }

    private void initview() {
        videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.qqq));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

        videoView.start();

    }

    float x1 = 0;
    float x2 = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==event.ACTION_DOWN){
            x1 = event.getX();
        }
        if(event.getAction()==event.ACTION_UP){
            x2 = event.getX();
            if(x2-x1>300){
                videoView.seekTo(videoView.getCurrentPosition()+10*1000);
                Toast.makeText(this, "快进10秒", Toast.LENGTH_SHORT).show();
            }else if (x1-x2>300){
                videoView.seekTo(videoView.getCurrentPosition()-10*1000);
                Toast.makeText(this, "快退10秒", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }
}
