package com.example.hadas.ourrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by hadas on 24/09/2017.
 */

public class BigPicturePaint extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture_paint);
        imageView = (ImageView)findViewById(R.id.imageView);
        /*set the correct paint picture*/
        imageView.setImageResource(getIntent().getExtras().getInt("indexPaintPicture"));
        int indexPaintPicture = getIntent().getExtras().getInt("pictureIndex");
        final Intent i = new Intent(BigPicturePaint.this,BigPicture.class);
        /*send to BigPicture*/
        i.putExtra("activityIndex", -2);
        /*Send picture index, so in BigPicture activity its removed from the arrays*/
        i.putExtra("indexPaintPicture",indexPaintPicture);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        },3000L);

    }
}
