package com.inu.bus.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator


/**
 * Created by Minjae Son on 2018-08-13.
 */

class ManualSwipeViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var mEnabled = false
    private var mScroller: ScrollerCustomDuration? = null
    private var isScrolling = false

    init {
        postInitViewPager()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mEnabled && super.onInterceptTouchEvent(event)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return mEnabled && super.canScrollHorizontally(direction)
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
        addOnPageChangeListener(object : OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                // 2 : Swiping, 0 : Stop
                isScrolling = (state != 0)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}

        })
    }

    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller?.setScrollDurationFactor(scrollFactor)
    }

}
