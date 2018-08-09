package com.bungabear.inubus.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bungabear.inubus.MyService
import com.bungabear.inubus.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Minjae Son on 2018-08-07.
 */

class SplashActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        activity_splash_layout.alpha = 0f
        activity_splash_layout
                .animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(animationListener)
                .start()

        startService(Intent(applicationContext, MyService::class.java))
    }


    var animationListener = object : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    }
}