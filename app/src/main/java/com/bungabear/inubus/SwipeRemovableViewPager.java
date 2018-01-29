package com.bungabear.inubus;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Bunga on 2018-01-29.
 * TabLayout의 기본 Swipe 전환을 제어하기 위해 작성.
 */

public class SwipeRemovableViewPager extends ViewPager {

    private boolean enabled = true;

    public SwipeRemovableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (this.enabled) {
                return super.onTouchEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setSwipeable(boolean enabled){
        this.enabled = enabled;
    }
}
