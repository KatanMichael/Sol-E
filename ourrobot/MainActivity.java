package com.example.hadas.ourrobot;

import android.support.annotation.RequiresApi;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by
 * Hadas Barel
 * Channa Goldberg
 * Meizy Gambash
 * on 10/09/2017.
 */

public class MainActivity extends AppCompatActivity implements ErrorMsg,TextToSpeech.OnInitListener {

    private VideoView videoView;
    private TextToSpeech t = null;
    private static final String TAG = "MainActivity";

    static{
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV not loaded");
        }else {
            Log.d(TAG,"OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView)findViewById(R.id.video);
        Uri uri = Uri.parse("android.resource://com.example.hadas.ourrobot/" + R.raw.start);
        TaskTest.error=this;
        t = new TextToSpeech(this,this);
        startRobot();
        videoView.setVideoURI(uri);
        videoView.start();

    }

    /*This function create Toast to user,
     this is how we taking care of error*/
    @Override
    public void setError(String str) {
        Toast toast = Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG);
        toast.show();
    }

    public void startRobot(){
        speakOut();
        /*send message to robot: hello*/
        new TaskTest().execute(new String[]{"1"});
        /*create 4 arrays:
        * integers- the original pictures
        * text- the text for textToSpeech
        * color- the right color for each picture
        * integersPaint- the painting pictures*/
        ArrayList<Integer> integers = new ArrayList<Integer>(Arrays.asList(R.drawable.sun,R.drawable.flag,
                R.drawable.carrot,R.drawable.leaf, R.drawable.mouse, R.drawable.strawberry));
        ArrayList<String> text = new ArrayList<String>(Arrays.asList("sun","flag","carrot","leaf","mouse","strawberry"));
        ArrayList<String> color = new ArrayList<String>(Arrays.asList("yellow","blue","orange","green","gray","red"));
        ArrayList<Integer> integersPaint = new ArrayList<Integer>(Arrays.asList(R.drawable.sunp,R.drawable.flagp,
                R.drawable.carrotp, R.drawable.leafp,R.drawable.mousep, R.drawable.strawberryp));
        final Intent intant = new Intent(MainActivity.this,BigPicture.class);
        intant.putExtra("activityIndex",-1);
        intant.putExtra("arrayPicture",integers);
        intant.putExtra("arrayText",text);
        intant.putExtra("arrayColor",color);
        intant.putExtra("arrayPicturePaint",integersPaint);
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intant);
            }
        },15000L);
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    public void onInit(int status) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            setError("cannot sleep");
        }
        if(status==TextToSpeech.SUCCESS){
            int result = t.setLanguage(Locale.US);
            if(result== TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED) {
                setError("error speak");
            }
            else {
                speakOut();
            }
        }
        else{
            setError("status fail");
        }
    }
    public void speakOut(){
        t.speak("hello my friend, my name is sol-E",TextToSpeech.QUEUE_ADD,null);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            setError("cannot sleep");
        }
        t.speak("let's play",TextToSpeech.QUEUE_ADD,null);
    }

    @Override
    public void onDestroy(){
        if(t!=null) {
            t.stop();
            t.shutdown();
        }
        System.exit(0);
    }
}