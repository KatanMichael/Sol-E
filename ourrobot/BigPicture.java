package com.example.hadas.ourrobot;

import android.support.annotation.RequiresApi;

/*import for imageView*/
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/*import for camera*/
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/*import for pictures*/
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by
 * Hadas Barel
 * Channa Goldberg
 * Meizy Gambash
 * on 10/09/2017.
 */

public class BigPicture extends AppCompatActivity implements TextToSpeech.OnInitListener, CameraBridgeViewBase.CvCameraViewListener2 {
    private TextToSpeech t = null;
    private String str;
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;
    private String color;

    //Green
    private final double greenG=151;
    private final double greenR=135;
    private final double greenB=64;
    //Red
    private final double redG=92;
    private final double redR=123;
    private final double redB=183;
    //Yellow
    private final double yellowG=141;
    private final double yellowR=90;
    private final double yellowB=173;
    //Orange
    private final double orangeG=112;
    private final double orangeR=89;
    private final double orangeB=168;
    //Gray
    private final double grayG=155;
    private final double grayR=155;
    private final double grayB=154;
    //Blue
    private final double blueG=139;
    private final double blueR=200;
    private final double blueB=6;

    /*Array for the original picture*/
    public static ArrayList<Integer> integers;
    /*Array for the textToSpeech*/
    public static ArrayList<String> textArray;
    /*Array for the correct color for each picture*/
    public static ArrayList<String> colorArray;
    /*Array for the painting picture*/
    public static ArrayList<Integer> integersPaint;
    public static int amount = 6;

    public ImageView imageView;
    private static int pictureIndex;
    private static int onCameraFrameIttr = 1;
    private boolean finishFlag;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        imageView = (ImageView)findViewById(R.id.image);
        if(imageView == null) {
               setError("Image id is null");
        }
        /*The BigPicture activity progress like controller, everything pass through him.
         To deal with every situation we decide each activity that came to BigPicture will send a different index*/
        int activityIndex = getIntent().getExtras().getInt("activityIndex");
        /*came from main Activity*/
        if(activityIndex == -1) {
            integers = getIntent().getExtras().getIntegerArrayList("arrayPicture");
            textArray = getIntent().getExtras().getStringArrayList("arrayText");
            colorArray = getIntent().getExtras().getStringArrayList("arrayColor");
            integersPaint =getIntent().getExtras().getIntegerArrayList("arrayPicturePaint");
            Random random = new Random();
            pictureIndex = random.nextInt(amount);
            imageView.setImageResource(integers.get(pictureIndex).intValue());

            /*came from BigPicturePaint Activity correct answer*/
        }else if(activityIndex == -2) {
            int indexPicture = getIntent().getExtras().getInt("indexPaintPicture");
            amount--;
            integers.remove(indexPicture);
            integersPaint.remove(indexPicture);
            textArray.remove(indexPicture);
            colorArray.remove(indexPicture);

            /*Game is over*/
            if (amount == 0) {
                /*send message to robot: finish*/
                new TaskTest().execute(new String[]{"5"});
                finishFlag = true;
            } else {
                Random random = new Random();
                pictureIndex = random.nextInt(amount);
                imageView.setImageResource(integers.get(pictureIndex));
            }
        }
        /*came from video Activity wrong answer*/
        else {
            /*came from wrong picture 3 times*/
            if(activityIndex==-3){
                Random random = new Random();
                pictureIndex = random.nextInt(amount);
                imageView.setImageResource(integers.get(pictureIndex));
            }else {
                pictureIndex = activityIndex;
                imageView.setImageResource(integers.get(pictureIndex));
            }

        }
        if(finishFlag){
            Intent fin = new Intent(this,Video.class);
            fin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            fin.putExtra("finishGame",true);
            startActivity(fin);
            finish();
        }else {
            t = new TextToSpeech(this, this);
            Intent i = getIntent();
            color = colorArray.get(pictureIndex);
            str = textArray.get(pictureIndex);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);
            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            int result = t.setLanguage(Locale.US);
            if(result== TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED) {
                setError("error speak");
            }
            else {
                speakOut(str);
            }
        }
        else{
            setError("status fail");
        }
    }

    /*This function create Toast to user,
     this is how we taking care of error*/
    public void setError(String str){
        Toast toast = Toast.makeText(this,str,Toast.LENGTH_LONG);
        toast.show();
    }

    public void speakOut(String str){
        if(!str.equals("null")) {
            t.speak("please show me the color of the " + str, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    public double[] sumColor(){
        double [] bgrcolor;
        double [][] aR = new double[mRgba.width()][mRgba.height()];
        double [][] aG = new double[mRgba.width()][mRgba.height()];
        double [][] aB  =new double[mRgba.width()][mRgba.height()];
        double sumB=0;double sumG=0;double sumR=0;
        for(int i = 0; i< mRgba.width()/2; i++) {
            for (int j = 0; j < mRgba.height() / 2; j++) {
                bgrcolor = mRgba.get(i, j);
                aB[i][j] = bgrcolor[0];
                sumB += aB[i][j];
                aG[i][j] = bgrcolor[1];
                sumG += aG[i][j];
                aR[i][j] = bgrcolor[2];
                sumR += aR[i][j];
            }
        }
        double[] s = new double[3];
        s[0]= sumB; s[1]=sumG; s[2]=sumR;
        return s;
    }

    private boolean getColor(String color){
        /*sumColor take the picture and separate it to three mat(RGB), so in every pixel we know how much red,green,blue there is.
         * its return array contains three numbers, average for each color */
        double [] s = sumColor();
        boolean answer = false;
        int size = mRgba.width()/2 *(mRgba.height()/2);
            double avgB = s[0]/size; double avgG =s[1]/size ; double avgR =s[2]/size ;
            switch (color){
                case "blue":
                    if((blueR-30<avgR && avgR<blueR+30)&&(blueB-30<avgB && avgB<blueB+30)&&(blueG-30<avgG && avgG<blueG+30))
                        answer = true;
                    break;
                case "red":
                    if((redR-30<avgR && avgR<redR+30)&&(redB-30<avgB && avgB<redB+30)&&(redG-30<avgG && avgG<redG+30))
                        answer = true;
                    break;
                case "yellow":
                    if((yellowR-30<avgR && avgR<yellowR+30)&&(yellowB-30<avgB && avgB<yellowB+30)&&(yellowG-30<avgG && avgG<yellowG+30))
                        answer = true;
                    break;
                case "green":
                    if((greenR-30<avgR && avgR<greenR+30)&&(greenB-30<avgB && avgB<greenB+30)&&(greenG-30<avgG && avgG<greenG+30))
                        answer = true;
                    break;
                case "orange":
                    if((orangeR-30<avgR && avgR<orangeR+30)&&(orangeB-30<avgB && avgB<orangeB+30)&&(orangeG-30<avgG && avgG<orangeG+30))
                        answer = true;
                    break;
                case "gray":
                    if((grayR-30<avgR && avgR<grayR+30)&&(grayB-30<avgB && avgB<grayB+30)&&(grayG-30<avgG && avgG<grayG+30))
                        answer = true;
                    break;
                default:
                    answer = false;
            }
    return answer;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width,height, CvType.CV_8UC4);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        /*Only after 150 frame, the picture send to color diagnose*/
        onCameraFrameIttr++;
        mRgba = inputFrame.rgba();
        if(onCameraFrameIttr %150==0) {
            takePicture();
        }
        return mRgba;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void takePicture() {
        /*getColor get as parameter the correct color of the picture and check,
         if this color is what user show to camera return true, else return false,
          in both cases we create intent that lead to Video activity and send with him: "finishGame", false and the index of the picture*/
        boolean val =getColor(color);
        Intent intent = new Intent(this,Video.class);
        intent.putExtra("finishGame",false);
        intent.putExtra("pictureIndex",pictureIndex);
        if(val) {
            intent.putExtra("whatToDo",true);
            intent.putExtra("indexPaintPicture",integersPaint.get(pictureIndex));//push index of paint picture
        }else{
            intent.putExtra("whatToDo",false);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        t.shutdown();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        t.shutdown();
    }
}
