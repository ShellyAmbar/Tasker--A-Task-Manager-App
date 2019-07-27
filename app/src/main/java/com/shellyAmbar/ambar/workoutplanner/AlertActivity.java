package com.shellyAmbar.ambar.workoutplanner;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class AlertActivity extends AppCompatActivity {

    ImageView imageView1,imageView2,imageView3,imageView4;
    TextView textViewName,textViewBody;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        imageView1=findViewById(R.id.image1);
        imageView2=findViewById(R.id.image2);
        imageView3=findViewById(R.id.image3);
        imageView4=findViewById(R.id.image4);
        textViewBody=findViewById(R.id.alert_body);
        textViewName=findViewById(R.id.alert_name);
        String taskName=getIntent().getStringExtra("taskName");
        String taskBody=getIntent().getStringExtra("taskBody");
        String taskDate=getIntent().getStringExtra("date");
        final MediaPlayer notify_sound = MediaPlayer.create(AlertActivity.this,R.raw.alarm);
        notify_sound.start();
        textViewName.setText(taskName +" "+ taskDate);
        textViewBody.setText(taskBody);


        YoYo.with(Techniques.Tada)
                .repeat(2)
                .duration(700)
                .playOn(textViewName);

        YoYo.with(Techniques.Tada)
                .repeat(4)
                .duration(700)
                .playOn(imageView1);
        YoYo.with(Techniques.Shake)
                .repeat(4)
                .duration(700)
                .playOn(imageView2);
        YoYo.with(Techniques.Shake)
                .repeat(4)
                .duration(700)
                .playOn(imageView3);
        YoYo.with(Techniques.Shake)
                .repeat(4)
                .duration(700)
                .playOn(imageView4);




    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
