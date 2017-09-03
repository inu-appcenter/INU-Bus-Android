package com.bungabear.inubus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jaredrummler.android.widget.AnimatedSvgView;
/**
 * Created by minjae on 2017-08-09.
 * 스플래시 액티비티에서 타이포그래피 애니메이션을 보여주고 메인 액티비티로 넘어간다.
 *  inu 드로잉(i,n,u 순차적) -> 앱 아이콘 페이드인 -> 액티비티 페이드전환
 */

public class SplashActivity extends AppCompatActivity {

    // inu 텍스트 그리는 애니메이션 뷰
    private AnimatedSvgView logo_i, logo_n, logo_u;
    // 텍스트에서 아이콘으로 가는 애니메이션 뷰
    private ImageView bus_logo;
    // inu위에 앱 로고가 alpha animate하는 시간.
    private long fade_duration = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bus_logo = (ImageView) findViewById(R.id.iv_logo);
        bus_logo.setAlpha(0f);
        logo_i = (AnimatedSvgView) findViewById(R.id.animsvg_logo_i);
        logo_n = (AnimatedSvgView) findViewById(R.id.animsvg_logo_n);
        logo_u = (AnimatedSvgView) findViewById(R.id.animsvg_logo_u);

        logo_i.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(@AnimatedSvgView.State int state) {
                if(state == AnimatedSvgView.STATE_FILL_STARTED){
                    logo_n.start();
                }
            }
        });

        logo_n.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(@AnimatedSvgView.State int state) {
                if(state == AnimatedSvgView.STATE_FILL_STARTED){
                    logo_u.start();
                }
            }
        });

        logo_u.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(@AnimatedSvgView.State int state) {
                if(state == AnimatedSvgView.STATE_FILL_STARTED){

                    //텍스트 -> 아이콘 뷰를 fade in
                    bus_logo.animate()
                            .alpha(1f)
                            .setDuration(fade_duration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    finish();
                                    // 액티비티 전환 애니메이션을 페이드로 변경한다
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }
                            );
                }
            }
        });

        logo_i.start();
    }
}
