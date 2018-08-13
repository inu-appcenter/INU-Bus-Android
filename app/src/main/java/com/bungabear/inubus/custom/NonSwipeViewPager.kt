package com.bungabear.inubus.custom

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator


/**
 * Created by Minjae Son on 2018-08-13.
 */
class NonSwipeViewPager(context : Context, attrs : AttributeSet? = null) : ViewPager(context, attrs) {

    init {
        postInitViewPager()
    }
    private var mEnabled = false
    private var mScroller: ScrollerCustomDuration? = null

    override fun onInterceptTouchEvent(ev : MotionEvent) : Boolean {
        return if (mEnabled) {
            super.onInterceptTouchEvent(ev)
        } else {
            if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_MOVE) {
                // ignore move action
            } else {
                if (super.onInterceptTouchEvent(ev)) {
                    super.onTouchEvent(ev)
                }
            }
            false
        }
    }

    override fun onTouchEvent(ev : MotionEvent) : Boolean{
        if (mEnabled) {
            return super.onTouchEvent(ev)
        } else {
            return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev)
        }
    }

    fun setPagingEnabled(enabled    : Boolean) {
        this.mEnabled = enabled
    }

    private fun postInitViewPager() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = viewpager.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(context,
                    interpolator.get(null) as Interpolator)
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }

    }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller?.setScrollDurationFactor(scrollFactor)
    }


}
