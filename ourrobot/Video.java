package com.example.hadas.ourrobot;

import android.support.annotation.RequiresApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

/**
 * Created by
 * Hadas Barel
 * Channa Goldberg
 * Meizy Gambash
 * on 10/09/2017.
 */
public class Video extends AppCompatActivity {
    private VideoView videoView;
    private static int wrongTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Uri uri;
        videoView = (VideoView) findViewById(R.id.videoView);
        final Boolean finish = getIntent().getExtras().getBoolean("finishGame");
        if (finish) {
            uri = Uri.parse("android.resource://com.example.hadas.ourrobot/" + R.raw.youwin);
            videoView.setVideoURI(uri);
            videoView.start();
            videoView.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                        finish();
                }
            }, 3000L);
        } else {
            /*if answer is true play the happy video and go to BigPicturePaint activity, shown the same picture with color,
             else if its wrong answer for three times play waiting video,
             if its only one/two times play the sad video*/
            Boolean answer = getIntent(/**
             * Created by
             * Hadas Barel
             * Channa Goldberg
             * Meizy Gambash
             * on 10/09/2017.
             */).getExtras().getBoolean("whatToDo");
            final Intent intent;
            int bigPicturePosition = getIntent().getExtras().getInt("pictureIndex");
            if (answer) {
                uri = Uri.parse("android.resource://com.example.hadas.ourrobot/" + R.raw.correct);
                intent = new Intent(this, BigPicturePaint.class);
                intent.putExtra("pictureIndex",bigPicturePosition);
                intent.putExtra("indexPaintPicture",getIntent().getExtras().getInt("indexPaintPicture"));
                /*send message to robot: happy*/
                new TaskTest().execute(new String[]{"2"});
                wrongTime =0;
            } else {
                intent = new Intent(this, BigPicture.class);
                wrongTime++;
                if(wrongTime==3){
                    uri = Uri.parse("android.resource://com.example.hadas.ourrobot/" + R.raw.waiting);
                    wrongTime=0;
                    intent.putExtra("activityIndex", -3);
                    /*send message to robot: waiting*/
                    new TaskTest().execute(new String[]{"4"});
                }else {
                    uri = Uri.parse("android.resource://com.example.hadas.ourrobot/" + R.raw.wrong);
                    intent.putExtra("activityIndex", bigPicturePosition);
                    /*send message to robot: sad*/
                    new TaskTest().execute(new String[]{"3"});
                }
            }

            videoView.setVideoURI(uri);
            videoView.start();
            videoView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, 5000L);

        }
    }

    }
