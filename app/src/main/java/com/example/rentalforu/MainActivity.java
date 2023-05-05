package com.example.rentalforu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.rentalforu.Utils.Util;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    Animation fade_in;
    ImageView img;

    String s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();



        fade_in = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in);
        fade_in.setAnimationListener(this);
        start();
    }

    private void start(){
    img = findViewById(R.id.img);
        img.startAnimation(fade_in);

        s = Util.getData(MainActivity.this,"token");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    System.out.println("welcome "+s);
                    if(s.equals("null") || s.equals(null) || s==null){
                        navigate(new Intent(MainActivity.this,Login.class));
                    }else{
                        navigate(new Intent(MainActivity.this,HomePage.class));
                    }
            }
        }, 2000);
//      new Thread(new Runnable() {
//           @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }finally {
//
//                    System.out.println("welcome "+s);
//                    if(!s.equals(null) || s!=null){
//                        navigate(new Intent(MainActivity.this,HomePage.class));
//                    }else{
//                        navigate(new Intent(MainActivity.this,Login.class));
//                    }
//                }
//            }
//        }).start();
    }

    public void navigate(Intent i){
        startActivity(i);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}