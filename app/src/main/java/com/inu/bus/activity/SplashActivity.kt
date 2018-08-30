package com.inu.bus.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.inu.bus.MyService
import com.inu.bus.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Minjae Son on 2018-08-07.
 */

class SplashActivity : AppCompatActivity(){

    private var mPaused = false
    private var isAnimationEnd = false
    private var mShouldFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(applicationContext, MyService::class.java))
        setContentView(R.layout.activity_splash)
        activity_splash_layout.alpha = 0f
        activity_splash_layout
                .animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(animationListener)
                .start()


    }

    private var animationListener = object : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) { }

        override fun onAnimationEnd(animation: Animator?) {
            if(!mPaused){
                nextActivity()
            }
            isAnimationEnd = true
        }

        override fun onAnimationCancel(animation: Animator?) { }

        override fun onAnimationStart(animation: Animator?) { }
    }

    private fun nextActivity(){
        startActivity(Intent(applicationContext, MainActivity::class.java))
        mShouldFinish = true
    }

    override fun onResume() {
        super.onResume()
        mPaused = false
        if(isAnimationEnd){
            nextActivity()
        }
    }

    override fun onPause() {
        super.onPause()
        mPaused = true
        if(mShouldFinish){
            finish()
        }
    }

}