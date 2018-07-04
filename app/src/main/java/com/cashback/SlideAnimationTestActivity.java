package com.cashback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;



public class SlideAnimationTestActivity extends AppCompatActivity {

    Animation slideUpAnimation, slideDownAnimation;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_animation_test);

        imageView = (ImageView) findViewById(R.id.imageView);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void startSlideUpAnimation(View view) {
        imageView.startAnimation(slideUpAnimation);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void startSlideDownAnimation(View view) {
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(slideDownAnimation);
    }
}
