package com.bungabear.inubus;

import android.content.Context;

/**
 * Created by Bunga on 2018-01-28.
 */


public class DpPxCalc {

    public static float Dp2Px(float dp, Context context)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static float Px2Dp(int px, Context context)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
