package com.bungabear.inubus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by minjae on 2017-08-09.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView splashLogo;
    private TextView splashText;

    private long fade_duration = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLogo = (ImageView) findViewById(R.id.iv_splash_logo);
        splashLogo.setAlpha(0f);
        splashText = (TextView) findViewById(R.id.iv_splash_text);
        splashText.setAlpha(0f);

        splashText.animate()
                .alpha(1f)
                .setDuration(fade_duration);

        splashLogo.animate()
                .alpha(1f)
                .setDuration(fade_duration)
                .setListener(new AnimatorListenerAdapter() {
                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     startActivity(new Intent(getApplicationContext(), BusInfoActivity.class));
                                     finish();
                                     // 액티비티 전환 애니메이션을 페이드로 변경한다
                                     overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                 }
                             }
                );
    }
}
